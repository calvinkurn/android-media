package com.tokopedia.topads.headline.view.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.model.CountDataItem
import com.tokopedia.topads.dashboard.data.model.KeywordsResponse
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.adapter.keyword.KeywordAdapter
import com.tokopedia.topads.dashboard.view.adapter.keyword.KeywordAdapterTypeFactoryImpl
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewmodel.KeywordEmptyViewModel
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewmodel.KeywordItemViewModel
import com.tokopedia.topads.dashboard.view.sheet.TopadsGroupFilterSheet
import com.tokopedia.topads.headline.view.activity.TopAdsHeadlineAdDetailViewActivity
import com.tokopedia.topads.headline.view.model.HeadlineDetailViewModel
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.topads_dash_fragment_keyword_list.*
import kotlinx.android.synthetic.main.topads_dash_group_empty_state.*
import kotlinx.android.synthetic.main.topads_dash_layout_common_action_bar.*
import kotlinx.android.synthetic.main.topads_dash_layout_common_searchbar_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Pika on 19/10/20.
 */

class TopAdsHeadlineKeyFragment : BaseDaggerFragment() {


    private lateinit var recyclerviewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private var totalCount = 0
    private var totalPage = 0
    private var currentPageNum = 1
    private var deleteCancel = false
    private var singleAction = false

    private lateinit var adapter: KeywordAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy {
        ViewModelProviders.of(this, viewModelFactory)
    }
    private val viewModel by lazy {
        viewModelProvider.get(HeadlineDetailViewModel::class.java)
    }

    private val groupFilterSheet: TopadsGroupFilterSheet by lazy {
        context.run {
            TopadsGroupFilterSheet.newInstance(context!!)
        }
    }

    companion object {
        fun createInstance(bundle: Bundle): TopAdsHeadlineKeyFragment {
            val fragment = TopAdsHeadlineKeyFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(resources.getLayout(R.layout.topads_dash_fragment_keyword_list), container, false)
        adapter = KeywordAdapter(KeywordAdapterTypeFactoryImpl(::onCheckedChange, ::setSelectMode, ::startEditActivity))
        recyclerView = view.findViewById(R.id.key_list)
        setAdapterView()
        return view
    }

    private fun startEditActivity() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_EDIT_ADS)?.apply {
            putExtra(TopAdsDashboardConstant.TAB_POSITION, 1)
            putExtra(TopAdsDashboardConstant.GROUPID, arguments?.getInt(TopAdsDashboardConstant.GROUP_ID).toString())
            putExtra(TopAdsDashboardConstant.GROUPNAME, arguments?.getString(TopAdsDashboardConstant.GROUP_NAME))
        }
        startActivityForResult(intent, TopAdsDashboardConstant.EDIT_GROUP_REQUEST_CODE)
    }


    private fun onCheckedChange(pos: Int, isChecked: Boolean) {
        singleAction = true
        val actionActivate: String = if (isChecked)
            TopAdsDashboardConstant.ACTION_ACTIVATE
        else
            TopAdsDashboardConstant.ACTION_DEACTIVATE
        viewModel.setKeywordAction(actionActivate,
                listOf((adapter.items[pos] as KeywordItemViewModel).result.keywordId.toString()), resources, ::onSuccessAction)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchData()
        setSearchBar()
        close_butt?.setOnClickListener { setSelectMode(false) }
        activate.setOnClickListener { performAction(TopAdsDashboardConstant.ACTION_ACTIVATE) }
        deactivate.setOnClickListener { performAction(TopAdsDashboardConstant.ACTION_DEACTIVATE) }
        delete.setOnClickListener {
            showConfirmationDialog(context!!)
        }
        Utils.setSearchListener(context, view, ::fetchData)
    }

    private fun setSearchBar() {
        divider.visibility = View.VISIBLE
        btnAddItem.visibility = View.GONE
        movetogroup.visibility = View.GONE
    }

    private fun setSelectMode(select: Boolean) {
        if (select) {
            adapter.setSelectMode(true)
            actionbar.visibility = View.VISIBLE
        } else {
            adapter.setSelectMode(false)
            actionbar.visibility = View.GONE
        }
    }

    private fun showConfirmationDialog(context: Context) {
        val dialog = DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setDescription(context.getString(R.string.topads_dash_confirm_delete_key_desc))
        dialog.setTitle(String.format(context.getString(R.string.topads_dash_confirm_delete_key), adapter.getSelectedItems().size))
        dialog.setPrimaryCTAText(context.getString(com.tokopedia.topads.common.R.string.topads_common_cancel_btn))
        dialog.setSecondaryCTAText(context.getString(R.string.topads_dash_ya_hapus))
        dialog.setPrimaryCTAClickListener {
            dialog.dismiss()
        }
        dialog.setSecondaryCTAClickListener {
            dialog.dismiss()
            performAction(TopAdsDashboardConstant.ACTION_DELETE)
        }
        dialog.show()
    }


    private fun performAction(actionActivate: String) {

        if (actionActivate == TopAdsDashboardConstant.ACTION_DELETE) {
            view.let {
                Toaster.make(it!!, String.format(getString(R.string.topads_keyword_del_toaster), getAdIds().size), TopAdsDashboardConstant.TOASTER_DURATION.toInt(), Toaster.TYPE_NORMAL, getString(com.tokopedia.topads.common.R.string.topads_common_batal), View.OnClickListener {
                    deleteCancel = true

                })
            }
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            coroutineScope.launch {
                delay(TopAdsDashboardConstant.TOASTER_DURATION)
                if (activity != null && isAdded) {
                    if (!deleteCancel) {
                        viewModel.setKeywordAction(actionActivate, getAdIds(), resources, ::onSuccessAction)
                        activity?.setResult(Activity.RESULT_OK)
                    }
                    deleteCancel = false
                    setSelectMode(false)
                }
            }
        } else {
            viewModel.setKeywordAction(actionActivate, getAdIds(), resources, ::onSuccessAction)
        }
    }

    private fun onSuccessAction() {
        if (!singleAction) {
            setSelectMode(false)
            fetchData()
        }
        singleAction = false
    }

    private fun getAdIds(): MutableList<String> {
        val ads: MutableList<String> = mutableListOf()
        adapter.getSelectedItems().forEach {
            ads.add(it.result.keywordId.toString())
        }
        return ads
    }

    private fun fetchData() {
        viewModel.getCountProductKeyword(resources, listOf(arguments?.getInt(TopAdsDashboardConstant.GROUP_ID).toString()), ::successCount)
        currentPageNum = 1
        loader.visibility = View.VISIBLE
        adapter.items.clear()
        adapter.notifyDataSetChanged()
        viewModel.getGroupKeywordData(resources, 1, arguments?.getInt(TopAdsDashboardConstant.GROUP_ID)
                ?: 0, searchBar.searchBarTextField.text.toString(), groupFilterSheet.getSelectedSortId(),
                groupFilterSheet.getSelectedStatusId(), currentPageNum, ::onSuccessKeyword, ::onEmpty)
    }

    private fun successCount(list: List<CountDataItem>) {
        totalCount = list.firstOrNull()?.totalKeywords ?: 0
        (activity as TopAdsHeadlineAdDetailViewActivity).setKeywordCount(totalCount)
    }

    private fun onSuccessKeyword(response: KeywordsResponse.GetTopadsDashboardKeywords) {
        totalPage = if (totalCount % response.meta.page.perPage == 0) {
            totalCount / response.meta.page.perPage
        } else
            (totalCount / response.meta.page.perPage) + 1
        recyclerviewScrollListener.updateStateAfterGetData()
        loader.visibility = View.GONE
        recyclerviewScrollListener.updateStateAfterGetData()
        response.data.forEach { result ->
            adapter.items.add(KeywordItemViewModel(result))
        }
        adapter.notifyDataSetChanged()
        setFilterCount()
    }

    private fun setFilterCount() {
        if (!groupFilterSheet.getFilterCount().isZero()) {
            filterCount.visibility = View.VISIBLE
            filterCount.text = groupFilterSheet.getFilterCount().toString()
        } else
            filterCount.visibility = View.GONE
    }


    override fun getScreenName(): String {
        return TopAdsHeadlineKeyFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)

    }

    private fun setAdapterView() {
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerviewScrollListener = onRecyclerViewListener()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        recyclerView.addOnScrollListener(recyclerviewScrollListener)
    }

    private fun onRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (currentPageNum < totalPage) {
                    currentPageNum++
                    fetchNextPage(currentPageNum)
                }
            }
        }
    }

    private fun fetchNextPage(currentPage: Int) {
        viewModel.getGroupKeywordData(resources, 1, arguments?.getInt(TopAdsDashboardConstant.GROUP_ID)
                ?: 0, searchBar.searchBarTextField.text.toString(), groupFilterSheet.getSelectedSortId(),
                groupFilterSheet.getSelectedStatusId(), currentPage, ::onSuccessKeyword, ::onEmpty)
    }

    private fun onEmpty() {
        adapter.items.add(KeywordEmptyViewModel())
        if (searchBar?.searchBarTextField?.text.toString().isEmpty()) {
            adapter.setEmptyView(!TopAdsDashboardConstant.EMPTY_SEARCH_VIEW)
            btn_submit?.isEnabled = false
            btnAddItem.visibility = View.GONE
        } else {
            adapter.setEmptyView(TopAdsDashboardConstant.EMPTY_SEARCH_VIEW)
        }
        setFilterCount()
        (activity as TopAdsHeadlineAdDetailViewActivity).setKeywordCount(0)
        adapter.notifyDataSetChanged()
    }

}