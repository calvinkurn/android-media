package com.tokopedia.topads.headline.view.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
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
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.common.data.model.CountDataItem
import com.tokopedia.topads.dashboard.data.model.KeywordsResponse
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.adapter.keyword.KeywordAdapter
import com.tokopedia.topads.dashboard.view.adapter.keyword.KeywordAdapterTypeFactoryImpl
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewmodel.KeywordEmptyModel
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewmodel.KeywordItemModel
import com.tokopedia.topads.dashboard.viewmodel.GroupDetailViewModel
import com.tokopedia.topads.dashboard.view.sheet.TopadsGroupFilterSheet
import com.tokopedia.topads.headline.view.activity.TopAdsHeadlineAdDetailViewActivity
import com.tokopedia.unifycomponents.*
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

/**
 * Created by Pika on 19/10/20.
 */

private const val click_tambah_iklan = "click - tambah keyword on detail iklan toko"
private const val CLICK_AKTIFKAN_LONG_PRESS = "click - aktifkan iklan on long press keyword"
private const val CLICK_NONAKTIFKAN_LONG_PRESS = "click - nonaktifkan iklan on long press keyword"
private const val CLICK_HAPUS_LONG_PRESS = "click - hapus iklan on long press keyword"
private const val CLICK_YA_HAPUS_LONG_PRESS = "click - ya hapus iklan on long press keyword"
private const val CLICK_TERAPKAN = "click - terapkan"

class TopAdsHeadlineKeyFragment : BaseDaggerFragment() {

    private var loader: LoaderUnify? = null
    private var actionbar: ConstraintLayout? = null
    private var closeButton: UnifyImageButton? = null
    private var activate: Typography? = null
    private var deactivate: Typography? = null
    private var movetogroup: Typography? = null
    private var delete: UnifyImageButton? = null
    private var searchBar: SearchBarUnify? = null
    private var btnFilter: UnifyImageButton? = null
    private var filterCount: Typography? = null
    private var btnAddItem: UnifyImageButton? = null
    private var recyclerView: RecyclerView? = null

    private lateinit var recyclerviewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var layoutManager: LinearLayoutManager
    private var totalCount = 0
    private var totalPage = 0
    private var currentPageNum = 1
    private var deleteCancel = false
    private var singleAction = false
    private lateinit var adapter: KeywordAdapter

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(GroupDetailViewModel::class.java)
    }

    private val groupFilterSheet: TopadsGroupFilterSheet by lazy {
        TopadsGroupFilterSheet.newInstance(context)
    }

    companion object {
        fun createInstance(bundle: Bundle): TopAdsHeadlineKeyFragment {
            val fragment = TopAdsHeadlineKeyFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(context?.resources?.getLayout(R.layout.topads_dash_fragment_keyword_list),
            container, false)
        adapter = KeywordAdapter(KeywordAdapterTypeFactoryImpl(::onCheckedChange,
            ::setSelectMode, ::startEditActivity, true))
        recyclerView = view.findViewById(R.id.key_list)
        loader = view.findViewById(R.id.loader)
        actionbar = view.findViewById(R.id.actionbar)
        closeButton = view.findViewById(R.id.close_butt)
        activate = view.findViewById(R.id.activate)
        deactivate = view.findViewById(R.id.deactivate)
        movetogroup = view.findViewById(R.id.movetogroup)
        delete = view.findViewById(R.id.delete)
        searchBar = view.findViewById(R.id.searchBar)
        btnFilter = view.findViewById(R.id.btnFilter)
        filterCount = view.findViewById(R.id.filterCount)
        btnAddItem = view.findViewById(R.id.btnAddItem)
        setAdapterView()
        return view
    }

    private fun startEditActivity() {
        val intent =
            RouteManager.getIntent(activity, ApplinkConstInternalTopAds.TOPADS_HEADLINE_ADS_EDIT)
                ?.apply {
                    putExtra(TopAdsDashboardConstant.TAB_POSITION, 1)
                    putExtra(ParamObject.GROUP_ID,
                        arguments?.getInt(TopAdsDashboardConstant.GROUP_ID).toString())
                }
        startActivityForResult(intent, TopAdsDashboardConstant.EDIT_HEADLINE_REQUEST_CODE)
    }


    private fun onCheckedChange(pos: Int, isChecked: Boolean) {
        singleAction = true
        val actionActivate: String = if (isChecked)
            TopAdsDashboardConstant.ACTION_ACTIVATE
        else
            TopAdsDashboardConstant.ACTION_DEACTIVATE
        val resources = context?.resources ?: return
        viewModel.setKeywordAction(actionActivate,
            listOf((adapter.items[pos] as KeywordItemModel).result.keywordId),
            resources, ::onSuccessAction)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchData()
        setSearchBar()
        closeButton?.setOnClickListener { setSelectMode(false) }
        activate?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(
                CLICK_AKTIFKAN_LONG_PRESS,
                "{${userSession.shopId}} - {${TextUtils.join(",", getAdIds())}}",
                userSession.userId)
            performAction(TopAdsDashboardConstant.ACTION_ACTIVATE)
        }
        deactivate?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(
                CLICK_NONAKTIFKAN_LONG_PRESS,
                "{${userSession.shopId}} - {${TextUtils.join(",", getAdIds())}}",
                userSession.userId)
            performAction(TopAdsDashboardConstant.ACTION_DEACTIVATE)
        }
        delete?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(CLICK_HAPUS_LONG_PRESS,
                "{${userSession.shopId}} - {${TextUtils.join(",", getAdIds())}}",
                userSession.userId)
            context?.let {
                showConfirmationDialog(it)
            }
        }
        Utils.setSearchListener(context, view, ::fetchData)
        btnFilter?.setOnClickListener {
            groupFilterSheet.show(childFragmentManager, "")
            groupFilterSheet.onSubmitClick = {
                val eventLabel = "{${userSession.shopId}}" + "-" + "{${groupFilterSheet?.getSelectedText(context)}}" + "-" + "{${groupFilterSheet?.getSelectedSortId()}}"
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(CLICK_TERAPKAN, eventLabel, userSession.userId)
                fetchData()
            }
        }
        btnAddItem?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(click_tambah_iklan,
                "{${userSession.shopId}} - {$arguments?.getInt(TopAdsDashboardConstant.GROUP_ID).toString()}",
                userSession.userId)
            val intent =
                RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_HEADLINE_ADS_EDIT)
                    ?.apply {
                        putExtra(TopAdsDashboardConstant.TAB_POSITION, 1)
                        putExtra(ParamObject.GROUP_ID,
                            arguments?.getInt(TopAdsDashboardConstant.GROUP_ID).toString())
                    }
            activity?.startActivityForResult(intent,
                TopAdsDashboardConstant.EDIT_HEADLINE_REQUEST_CODE)
        }
    }

    private fun setSearchBar() {
        view?.findViewById<View>(R.id.divider)?.visibility = View.VISIBLE
        btnAddItem?.visibility = View.VISIBLE
        movetogroup?.visibility = View.GONE
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
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(
                CLICK_YA_HAPUS_LONG_PRESS,
                "{${userSession.shopId}} - {${TextUtils.join(",", getAdIds())}}",
                userSession.userId)
            dialog.dismiss()
            performAction(TopAdsDashboardConstant.ACTION_DELETE)
        }
        dialog.show()
    }


    private fun performAction(actionActivate: String) {
        activity?.setResult(Activity.RESULT_OK)
        val resources = context?.resources ?: return
        if (actionActivate == TopAdsDashboardConstant.ACTION_DELETE) {
            view?.let {
                Toaster.build(it,
                    String.format(getString(R.string.topads_keyword_del_toaster), getAdIds().size),
                    TopAdsDashboardConstant.TOASTER_DURATION.toInt(), Toaster.TYPE_NORMAL,
                    getString(com.tokopedia.topads.common.R.string.topads_common_batal),
                    View.OnClickListener {
                        deleteCancel = true
                    }).show()
            }
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            coroutineScope.launch {
                delay(TopAdsDashboardConstant.TOASTER_DURATION)
                if (activity != null && isAdded) {
                    if (!deleteCancel) {
                        viewModel.setKeywordAction(actionActivate,
                            getAdIds(), resources, ::onSuccessAction)
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
            ads.add(it.result.keywordId)
        }
        return ads
    }

    private fun fetchData() {
        val resources = context?.resources
        if (resources != null)
            viewModel.getCountProductKeyword(resources,
                listOf(arguments?.getInt(TopAdsDashboardConstant.GROUP_ID).toString()),
                ::successCount)
        currentPageNum = 1
        loader?.visibility = View.VISIBLE
        adapter.items.clear()
        adapter.notifyDataSetChanged()

        if(resources == null) return
        viewModel.getGroupKeywordData(resources,
            1, arguments?.getInt(TopAdsDashboardConstant.GROUP_ID) ?: 0,
            searchBar?.searchBarTextField?.text.toString(), groupFilterSheet.getSelectedSortId(),
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


    override fun getScreenName(): String {
        return TopAdsHeadlineKeyFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)

    }

    private fun setAdapterView() {
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerviewScrollListener = onRecyclerViewListener()
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = layoutManager
        recyclerView?.addOnScrollListener(recyclerviewScrollListener)
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
        viewModel.getGroupKeywordData(resources,
            1, arguments?.getInt(TopAdsDashboardConstant.GROUP_ID) ?: 0,
            searchBar?.searchBarTextField?.text.toString(), groupFilterSheet.getSelectedSortId(),
            groupFilterSheet.getSelectedStatusId(), currentPage, ::onSuccessKeyword, ::onEmpty)
    }

    private fun onEmpty() {
        adapter.items.add(KeywordEmptyModel())
        if (searchBar?.searchBarTextField?.text.toString().isEmpty()) {
            adapter.setEmptyView(!TopAdsDashboardConstant.EMPTY_SEARCH_VIEW, true)
        } else {
            adapter.setEmptyView(TopAdsDashboardConstant.EMPTY_SEARCH_VIEW, true)
        }
        setFilterCount()
        (activity as TopAdsHeadlineAdDetailViewActivity).setKeywordCount(0)
        adapter.notifyDataSetChanged()
    }

}
