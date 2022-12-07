package com.tokopedia.topads.dashboard.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.EMPTY_SEARCH_VIEW
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.GROUP_ID
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TOASTER_DURATION
import com.tokopedia.topads.dashboard.data.model.CountDataItem
import com.tokopedia.topads.dashboard.data.model.KeywordsResponse
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupDetailViewActivity
import com.tokopedia.topads.dashboard.view.adapter.keyword.KeywordAdapter
import com.tokopedia.topads.dashboard.view.adapter.keyword.KeywordAdapterTypeFactoryImpl
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewmodel.KeywordEmptyModel
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewmodel.KeywordItemModel
import com.tokopedia.topads.dashboard.view.sheet.TopadsGroupFilterSheet
import com.tokopedia.topads.dashboard.viewmodel.GroupDetailViewModel
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyImageButton
import com.tokopedia.unifyprinciples.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Pika on 7/6/20.
 */

private const val CLICK_TAMBAH_KATA_KUNCI = "click - tambah kata kunci"

class KeywordTabFragment : BaseDaggerFragment() {

    private var actionbar: ConstraintLayout? = null
    private var closeButt: UnifyImageButton? = null
    private var activate: Typography? = null
    private var deactivate: Typography? = null
    private var movetogroup: Typography? = null
    private var delete: UnifyImageButton? = null
    private var searchBar: SearchBarUnify? = null
    private var btnFilter: UnifyImageButton? = null
    private var btnAddItem: UnifyImageButton? = null
    private var loader: LoaderUnify? = null
    private var filterCount: Typography? = null
    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: KeywordAdapter
    private var deleteCancel = false
    private var singleAction = false
    private var currentPageNum = 1
    private val groupId by lazy(LazyThreadSafetyMode.NONE) { arguments?.getInt(GROUP_ID, 0).toString() }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var recyclerviewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var layoutManager: LinearLayoutManager
    private var totalCount = 0
    private var totalPage = 0
    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }
    private val viewModel by lazy {
        viewModelProvider.get(GroupDetailViewModel::class.java)
    }

    companion object {

        fun createInstance(bundle: Bundle): KeywordTabFragment {
            val frag = KeywordTabFragment()
            frag.arguments = bundle
            return frag
        }
    }

    private val groupFilterSheet: TopadsGroupFilterSheet by lazy {
        context.run {
            TopadsGroupFilterSheet.newInstance(requireContext())
        }
    }

    override fun getScreenName(): String {
        return KeywordTabFragment::class.java.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = KeywordAdapter(KeywordAdapterTypeFactoryImpl(::onCheckedChange,
            ::setSelectMode, ::startEditActivity))
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(context?.resources?.getLayout(R.layout.topads_dash_fragment_keyword_list),
            container, false)
        recyclerView = view.findViewById(R.id.key_list)
        actionbar = view.findViewById(R.id.actionbar)
        closeButt = view.findViewById(R.id.close_butt)
        activate = view.findViewById(R.id.activate)
        deactivate = view.findViewById(R.id.deactivate)
        movetogroup = view.findViewById(R.id.movetogroup)
        delete = view.findViewById(R.id.delete)
        searchBar = view.findViewById(R.id.searchBar)
        btnFilter = view.findViewById(R.id.btnFilter)
        btnAddItem = view.findViewById(R.id.btnAddItem)
        loader = view.findViewById(R.id.loader)
        filterCount = view.findViewById(R.id.filterCount)
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
                if (currentPageNum < totalPage) {
                    currentPageNum++
                    fetchNextPage(currentPageNum)
                }
            }
        }
    }

    private fun fetchNextPage(currentPage: Int) {
        val resources = context?.resources ?: return
        viewModel.getGroupKeywordData(resources, 1, arguments?.getInt(GROUP_ID) ?: 0,
            searchBar?.searchBarTextField?.text.toString(),
            groupFilterSheet.getSelectedSortId(), groupFilterSheet.getSelectedStatusId(),
            currentPage, ::onSuccessKeyword, ::onEmpty)
    }

    private fun onCheckedChange(pos: Int, isChecked: Boolean) {
        val resources = context?.resources ?: return
        singleAction = true
        val actionActivate: String = if (isChecked)
            TopAdsDashboardConstant.ACTION_ACTIVATE
        else
            TopAdsDashboardConstant.ACTION_DEACTIVATE
        viewModel.setKeywordActionForGroup(groupId, actionActivate,
            listOf((adapter.items[pos] as KeywordItemModel).result.keywordId),
            resources, ::onSuccessAction)
    }

    private fun onSuccessAction() {
        if (!singleAction) {
            setSelectMode(false)
            fetchData()
        }
        singleAction = false
    }

    private fun setSelectMode(select: Boolean) {
        if (select) {
            adapter.setSelectMode(true)
            actionbar?.visibility = View.VISIBLE
        } else {
            adapter.setSelectMode(false)
            actionbar?.visibility = View.GONE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnFilter?.setOnClickListener {
            groupFilterSheet.show(childFragmentManager, "")
            groupFilterSheet.onSubmitClick = { fetchData() }
        }
        fetchData()
        setSearchBar()
        closeButt?.setOnClickListener { setSelectMode(false) }
        activate?.setOnClickListener { performAction(TopAdsDashboardConstant.ACTION_ACTIVATE) }
        deactivate?.setOnClickListener { performAction(TopAdsDashboardConstant.ACTION_DEACTIVATE) }
        delete?.setOnClickListener {
            showConfirmationDialog(requireContext())
        }
        Utils.setSearchListener(context, view, ::fetchData)
        btnAddItem?.setOnClickListener {
            startEditActivity()
        }
    }

    private fun successCount(list: List<CountDataItem>) {
        totalCount = list[0].totalKeywords
        (activity as TopAdsGroupDetailViewActivity).setKeywordCount(totalCount)
    }

    private fun startEditActivity() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_EDIT_ADS)?.apply {
            putExtra(TopAdsDashboardConstant.TAB_POSITION, 1)
            putExtra(TopAdsDashboardConstant.GROUPID, arguments?.getInt(GROUP_ID).toString())
        }
        startActivityForResult(intent, TopAdsDashboardConstant.EDIT_GROUP_REQUEST_CODE)
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsDashboardEvent(CLICK_TAMBAH_KATA_KUNCI,
            "")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TopAdsDashboardConstant.EDIT_GROUP_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                fetchData()
                (activity as TopAdsGroupDetailViewActivity).loadChildStatisticsData()
            }
        }
    }

    private fun showConfirmationDialog(context: Context) {
        val dialog = DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setDescription(context.getString(R.string.topads_dash_confirm_delete_key_desc))
        dialog.setTitle(String.format(context.getString(R.string.topads_dash_confirm_delete_key),
            adapter.getSelectedItems().size))
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

    private fun setSearchBar() {
        view?.findViewById<View>(R.id.divider)?.visibility = View.VISIBLE
        btnAddItem?.visibility = View.VISIBLE
        movetogroup?.visibility = View.GONE
    }

    private fun fetchData() {
        val resources = context?.resources ?: return
        viewModel.getCountProductKeyword(resources,
            listOf(arguments?.getInt(GROUP_ID).toString()), ::successCount)
        currentPageNum = 1
        loader?.visibility = View.VISIBLE
        adapter.items.clear()
        adapter.notifyDataSetChanged()
        viewModel.getGroupKeywordData(resources, 1, arguments?.getInt(GROUP_ID) ?: 0,
            searchBar?.searchBarTextField?.text.toString(), groupFilterSheet.getSelectedSortId(),
            groupFilterSheet.getSelectedStatusId(), currentPageNum, ::onSuccessKeyword, ::onEmpty)
    }

    private fun getAdIds(): MutableList<String> {
        val ads: MutableList<String> = mutableListOf()
        adapter.getSelectedItems().forEach {
            ads.add(it.result.keywordId)
        }
        return ads
    }

    private fun onSuccessKeyword(response: KeywordsResponse.GetTopadsDashboardKeywords) {
        totalPage = if (totalCount % response.meta.page.perPage == 0) {
            totalCount / response.meta.page.perPage
        } else
            (totalCount / response.meta.page.perPage) + 1
        recyclerviewScrollListener.updateStateAfterGetData()
        loader?.visibility = View.GONE
        recyclerviewScrollListener.updateStateAfterGetData()
        response.data.forEach { result ->
            adapter.items.add(KeywordItemModel(result))
        }
        adapter.notifyDataSetChanged()
        setFilterCount()
    }

    private fun setFilterCount() {
        if (!groupFilterSheet.getFilterCount().isZero()) {
            filterCount?.visibility = View.VISIBLE
            filterCount?.text = groupFilterSheet.getFilterCount().toString()
        } else
            filterCount?.visibility = View.GONE
    }

    private fun onEmpty() {
        adapter.items.add(KeywordEmptyModel())
        if (searchBar?.searchBarTextField?.text.toString().isEmpty()) {
            adapter.setEmptyView(!EMPTY_SEARCH_VIEW)
        } else {
            adapter.setEmptyView(EMPTY_SEARCH_VIEW)
        }
        setFilterCount()
        (activity as TopAdsGroupDetailViewActivity).setKeywordCount(0)
    }

    private fun performAction(actionActivate: String) {
        val resources = context?.resources ?: return
        if (actionActivate == TopAdsDashboardConstant.ACTION_DELETE) {
            view.let {
                Toaster.make(it!!,
                    String.format(getString(R.string.topads_keyword_del_toaster), getAdIds().size),
                    TOASTER_DURATION.toInt(),
                    Toaster.TYPE_NORMAL,
                    getString(com.tokopedia.topads.common.R.string.topads_common_batal)
                ) {
                    deleteCancel = true

                }
            }
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            coroutineScope.launch {
                delay(TOASTER_DURATION)
                if (activity != null && isAdded) {
                    if (!deleteCancel) {
                        viewModel.setKeywordActionForGroup(groupId,actionActivate, getAdIds(), resources, ::onSuccessAction)
                        activity?.setResult(Activity.RESULT_OK)
                    }
                    deleteCancel = false
                    setSelectMode(false)
                }
            }
        } else {
            viewModel.setKeywordActionForGroup(groupId,actionActivate, getAdIds(), resources, ::onSuccessAction)
        }
    }

}
