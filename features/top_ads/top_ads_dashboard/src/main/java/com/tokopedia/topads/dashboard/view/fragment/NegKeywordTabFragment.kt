package com.tokopedia.topads.dashboard.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION_DELETE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TOASTER_DURATION
import com.tokopedia.topads.dashboard.data.model.KeywordsResponse
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupDetailViewActivity
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.NegKeywordAdapter
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.NegKeywordAdapterTypeFactoryImpl
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewmodel.NegKeywordEmptyModel
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewmodel.NegKeywordItemModel
import com.tokopedia.topads.dashboard.view.model.GroupDetailViewModel
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.topads_dash_fragment_neg_keyword_list.loader
import kotlinx.android.synthetic.main.topads_dash_fragment_non_group_list.actionbar
import kotlinx.android.synthetic.main.topads_dash_layout_common_action_bar.*
import kotlinx.android.synthetic.main.topads_dash_layout_common_searchbar_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Pika on 15/5/20.
 */

private const val CLICK_TAMBAH_KATA_KUNCI_NEGATIVE = "click - tambah kata kunci negatif"

class NegKeywordTabFragment : BaseDaggerFragment() {

    private lateinit var adapter: NegKeywordAdapter

    companion object {

        fun createInstance(bundle: Bundle): NegKeywordTabFragment {
            val frag = NegKeywordTabFragment()
            frag.arguments = bundle
            return frag
        }
    }

    private var deleteCancel = false
    private lateinit var recyclerviewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private var totalPage = 0
    private var currentPageNum = 1
    private var max = 0
    private var perPage = 0

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy {
        ViewModelProviders.of(this, viewModelFactory)
    }
    private val viewModel by lazy {
        viewModelProvider.get(GroupDetailViewModel::class.java)
    }

    override fun getScreenName(): String {
        return NegKeywordTabFragment::class.java.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = NegKeywordAdapter(NegKeywordAdapterTypeFactoryImpl(::setSelectMode, ::startEditActivity))
    }

    private fun onSuccessAction() {
        setSelectMode(false)
        fetchData()
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

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(resources.getLayout(R.layout.topads_dash_fragment_neg_keyword_list), container, false)
        recyclerView = view.findViewById(R.id.neg_key_list)
        setAdapterView()
        return view
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
                if (max == perPage) {
                    currentPageNum++
                    fetchNextPage(currentPageNum)
                }
            }
        }
    }

    private fun fetchNextPage(currentPage: Int) {
        viewModel.getGroupKeywordData(resources, 0, arguments?.getInt(TopAdsDashboardConstant.GROUP_ID)
                ?: 0, searchBar.searchBarTextField.text.toString(), null, null, currentPage, ::onSuccessKeyword, ::onEmpty)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        close_butt?.setOnClickListener { setSelectMode(false) }
        setSearchBar()
        fetchData()
        delete.setOnClickListener {
            showConfirmationDialog(context!!)
        }
        btnAddItem.setOnClickListener {
            startEditActivity()
        }
        Utils.setSearchListener(context, view, ::fetchData)
    }

    private fun startEditActivity() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_EDIT_ADS)?.apply {
            putExtra(TopAdsDashboardConstant.TAB_POSITION, 1)
            putExtra(TopAdsDashboardConstant.GROUPID, arguments?.getInt(TopAdsDashboardConstant.GROUP_ID).toString())
        }
        startActivityForResult(intent, TopAdsDashboardConstant.EDIT_GROUP_REQUEST_CODE)
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsDashboardEvent(CLICK_TAMBAH_KATA_KUNCI_NEGATIVE, "")
    }

    private fun setSearchBar() {
        movetogroup.visibility = View.GONE
        activate.visibility = View.GONE
        deactivate.visibility = View.GONE
        btnFilter.visibility = View.GONE
        btnAddItem.visibility = View.VISIBLE
    }

    private fun performAction(actionActivate: String) {
        if (actionActivate == ACTION_DELETE) {
            view.let {
                Toaster.make(it!!, String.format(getString(R.string.topads_neg_keyword_del_toaster), getAdIds().size),
                        TOASTER_DURATION.toInt(), Toaster.TYPE_NORMAL, getString(com.tokopedia.topads.common.R.string.topads_common_batal), View.OnClickListener {
                    deleteCancel = true
                })
            }
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            coroutineScope.launch {
                delay(TOASTER_DURATION)
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

    private fun fetchData() {
        currentPageNum = 1
        loader.visibility = View.VISIBLE
        adapter.items.clear()
        adapter.notifyDataSetChanged()
        viewModel.getGroupKeywordData(resources, 0, arguments?.getInt(TopAdsDashboardConstant.GROUP_ID)
                ?: 0, searchBar.searchBarTextField.text.toString(), null, null, currentPageNum, ::onSuccessKeyword, ::onEmpty)
    }

    private fun showConfirmationDialog(context: Context) {
        val dialog = DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setDescription(context.getString(R.string.topads_dash_confirm_delete_negkey_desc))
        dialog.setTitle(String.format(context.getString(R.string.topads_dash_confirm_delete_negkey), adapter.getSelectedItems().size))
        dialog.setPrimaryCTAText(context.getString(com.tokopedia.topads.common.R.string.topads_common_cancel_btn))
        dialog.setSecondaryCTAText(context.getString(R.string.topads_dash_ya_hapus))
        dialog.setPrimaryCTAClickListener {
            dialog.dismiss()
        }
        dialog.setSecondaryCTAClickListener {
            dialog.dismiss()
            performAction(ACTION_DELETE)
        }
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TopAdsDashboardConstant.EDIT_GROUP_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK)
                fetchData()
        }
    }

    private fun getAdIds(): MutableList<String> {
        val ads: MutableList<String> = mutableListOf()
        adapter.getSelectedItems().forEach {
            ads.add(it.result.keywordId.toString())
        }
        return ads
    }

    private fun onEmpty() {
        adapter.items.add(NegKeywordEmptyModel())
        if (searchBar?.searchBarTextField?.text.toString().isEmpty()) {
            adapter.setEmptyView(!TopAdsDashboardConstant.EMPTY_SEARCH_VIEW)
        } else {
            adapter.setEmptyView(TopAdsDashboardConstant.EMPTY_SEARCH_VIEW)
        }
        (activity as TopAdsGroupDetailViewActivity).setNegKeywordCount(0)
    }

    private fun onSuccessKeyword(response: KeywordsResponse.GetTopadsDashboardKeywords) {
        loader.visibility = View.GONE
        max = response.meta.page.max
        perPage = response.meta.page.perPage
        totalPage = if (max % response.meta.page.perPage == 0) {
            max / response.meta.page.perPage
        } else
            (max / response.meta.page.perPage) + 1
        recyclerviewScrollListener.updateStateAfterGetData()
        response.data.forEach { result ->
            adapter.items.add(NegKeywordItemModel(result))
        }
        adapter.notifyDataSetChanged()
        (activity as TopAdsGroupDetailViewActivity).setNegKeywordCount(adapter.itemCount)
    }
}