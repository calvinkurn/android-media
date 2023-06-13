package com.tokopedia.topads.headline.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import com.tokopedia.unifyprinciples.Typography
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.response.groupitem.GetTopadsDashboardGroupStatistics
import com.tokopedia.topads.common.data.response.groupitem.GroupItemResponse
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.model.CountDataItem
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.fragment.TopAdsBaseTabFragment
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.topads.dashboard.view.sheet.TopadsGroupFilterSheet
import com.tokopedia.topads.headline.view.activity.TopAdsHeadlineAdDetailViewActivity
import com.tokopedia.topads.headline.view.adapter.HeadLineAdItemsAdapterTypeFactoryImpl
import com.tokopedia.topads.headline.view.adapter.HeadLineAdItemsListAdapter
import com.tokopedia.topads.headline.view.adapter.viewmodel.HeadLineAdItemsEmptyModel
import com.tokopedia.topads.headline.view.adapter.viewmodel.HeadLineAdItemsItemModel
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyImageButton
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val CLICK_GRUP_CARD = "click - group ads card"
private const val CLICK_AKTIFKAN_LONG_PRESS = "click - aktifkan iklan on long press card"
private const val CLICK_NONAKTIFKAN_LONG_PRESS = "click - nonaktifkan iklan on long press card"
private const val CLICK_HAPUS_LONG_PRESS = "click - hapus iklan on long press card"
private const val CLICK_YA_HAPUS_LONG_PRESS = "click - ya hapus iklan on long press card"
private const val CUREENTY_ACTIVATED = 1

class TopAdsHeadlineShopFragment : BaseDaggerFragment() {

    private var searchBar: SearchBarUnify? = null
    private var btnFilter: UnifyImageButton? = null
    private var filterCount: Typography? = null
    private var btnAddItem: UnifyImageButton? = null
    private var closeButton: UnifyImageButton? = null
    private var activate: Typography? = null
    private var deactivate: Typography? = null
    private var movetogroup: Typography? = null
    private var delete: UnifyImageButton? = null
    private var actionbar: ConstraintLayout? = null
    private var recyclerView: RecyclerView? = null
    private var loader: LoaderUnify? = null

    private var adapter: HeadLineAdItemsListAdapter? = null
    private var recyclerviewScrollListener: EndlessRecyclerViewScrollListener? = null
    private var layoutManager: LinearLayoutManager? = null
    private var totalCount = 0
    private var totalPage = 0
    private var currentPageNum = 1
    private var singleDelGroupId = ""
    private var deleteCancel = false
    private val groupIds: MutableList<String> = mutableListOf()


    @Inject
    lateinit var presenter: TopAdsDashboardPresenter

    @Inject
    lateinit var userSession: UserSessionInterface

    companion object {
        fun createInstance(): TopAdsHeadlineShopFragment {
            return TopAdsHeadlineShopFragment()
        }
    }

    override fun getScreenName(): String {
        return TopAdsHeadlineShopFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = HeadLineAdItemsListAdapter(
            HeadLineAdItemsAdapterTypeFactoryImpl(
                ::startSelectMode, ::singleItemDelete, ::statusChange, ::editGroup, ::onGroupClicked
            )
        )
    }

    private fun editGroup(groupId: String) {
        val intent =
            RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_HEADLINE_ADS_EDIT)
                ?.apply {
                    putExtra(TopAdsDashboardConstant.TAB_POSITION, 0)
                    putExtra(ParamObject.GROUP_ID, groupId)
                }
        activity?.startActivityForResult(intent, TopAdsDashboardConstant.EDIT_HEADLINE_REQUEST_CODE)
    }

    private fun onGroupClicked(id: String, priceSpent: String) {
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(
            CLICK_GRUP_CARD,
            "{${userSession.shopId}} - {$id}",
            userSession.userId
        )
        val intent = Intent(context, TopAdsHeadlineAdDetailViewActivity::class.java)
        intent.putExtra(TopAdsDashboardConstant.GROUP_ID, id)
        intent.putExtra(TopAdsDashboardConstant.PRICE_SPEND, priceSpent)
        startActivityForResult(intent, TopAdsDashboardConstant.HEADLINE_UPADTED)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(
            context?.resources?.getLayout(R.layout.topads_dash_fragment_headline_group_list),
            container, false
        )
        initViews(view)
        setAdapter()
        return view
    }

    private fun setAdapter() {
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerviewScrollListener = onRecyclerViewListener()
        recyclerView?.isNestedScrollingEnabled = false
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = layoutManager
        recyclerviewScrollListener?.let { recyclerView?.addOnScrollListener(it) }

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

    private fun fetchNextPage(page: Int) {
        val startDate = Utils.format.format(
            (parentFragment as? TopAdsHeadlineBaseFragment)?.startDate ?: Utils.getStartDate()
        )
        val endDate = Utils.format.format(
            (parentFragment as? TopAdsHeadlineBaseFragment)?.endDate ?: Utils.getEndDate()
        )
        presenter.getGroupData(
            page,
            searchBar?.searchBarTextField?.text.toString(),
            groupFilterSheet.getSelectedSortId(),
            groupFilterSheet.getSelectedStatusId(),
            startDate,
            endDate,
            TopAdsDashboardConstant.GROUP_TYPE_HEADLINE,
            this::onSuccessGroupResult
        )

    }

    private fun onSuccessGroupResult(response: GroupItemResponse.GetTopadsDashboardGroups) {
        totalCount = response.meta.page.total
        totalPage = (totalCount / response.meta.page.perPage) + 1
        recyclerviewScrollListener?.updateStateAfterGetData()
        loader?.hide()
        response.data.forEach {
            groupIds.add(it.groupId)
            adapter?.items?.add(HeadLineAdItemsItemModel(it))
        }
        val resources = context?.resources
        if (adapter?.items?.size.isZero()) {
            onEmptyResult()
        } else if (groupIds.isNotEmpty() && resources != null) {
            presenter.getGroupStatisticsData(
                1, ",", "", 0,
                Utils.format.format((parentFragment as? TopAdsHeadlineBaseFragment)?.startDate?:Utils.getStartDate()),
                Utils.format.format((parentFragment as? TopAdsHeadlineBaseFragment)?.endDate?:Utils.getEndDate()),
                groupIds, ::onSuccessStatistics
            )
            presenter.getCountProductKeyword(resources, groupIds, ::onSuccessCount)
        }
        setFilterCount()
        (parentFragment as TopAdsBaseTabFragment).setGroupCount(totalCount)
    }

    private fun onEmptyResult() {
        adapter?.items?.add(HeadLineAdItemsEmptyModel())
        if (searchBar?.searchBarTextField?.text.toString().isEmpty()) {
            adapter?.setEmptyView(
                !TopAdsDashboardConstant.EMPTY_SEARCH_VIEW,
                groupFilterSheet.getSelectedText(context)
            )
        } else {
            adapter?.setEmptyView(TopAdsDashboardConstant.EMPTY_SEARCH_VIEW)
        }
    }

    private fun onSuccessStatistics(statistics: GetTopadsDashboardGroupStatistics) {
        adapter?.setstatistics(statistics.data)
    }

    private fun onSuccessCount(countList: List<CountDataItem>) {
        adapter?.setItemCount(countList)
        loader?.visibility = View.GONE
    }

    private fun setFilterCount() {
        if (!groupFilterSheet.getFilterCount().isZero()) {
            filterCount?.visibility = View.VISIBLE
            filterCount?.text = groupFilterSheet.getFilterCount().toString()
        } else
            filterCount?.visibility = View.GONE
    }

    private fun startSelectMode(select: Boolean) {
        if (select) {
            adapter?.setSelectMode(true)
            actionbar?.visibility = View.VISIBLE
            movetogroup?.visibility = View.GONE
            btnAddItem?.visibility = View.VISIBLE
        } else {
            adapter?.setSelectMode(false)
            actionbar?.visibility = View.GONE
            btnAddItem?.visibility = View.GONE
        }
    }

    private fun singleItemDelete(pos: Int) {
        singleDelGroupId =
            (adapter?.items?.getOrNull(pos) as? HeadLineAdItemsItemModel)?.data?.groupId ?: ""
        performAction(TopAdsDashboardConstant.ACTION_DELETE)
    }

    private fun performAction(actionActivate: String) {
        val resources = context?.resources ?: return
        if (actionActivate == TopAdsDashboardConstant.ACTION_DELETE) {
            view?.let {
                val desc = if (getAdIds().size > 1)
                    String.format(
                        getString(R.string.topads_dash_headline_delete_toast_multiple),
                        getAdIds().size
                    )
                else
                    getString(R.string.topads_dash_headline_delete_toast)
                Toaster.build(
                    it,
                    desc,
                    TopAdsDashboardConstant.TOASTER_DURATION.toInt(),
                    Toaster.TYPE_NORMAL,
                    getString(com.tokopedia.topads.common.R.string.topads_common_batal)
                ) { deleteCancel = true }.show()
            }
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            coroutineScope.launch {
                delay(TopAdsDashboardConstant.TOASTER_DURATION)
                if (activity != null && isAdded) {
                    if (!deleteCancel)
                        presenter.setGroupAction(
                            ::onSuccessAction,
                            actionActivate,
                            getAdIds(),
                            resources
                        )
                    deleteCancel = false
                    startSelectMode(false)
                    singleDelGroupId = ""
                }
            }
        } else {
            presenter.setGroupAction(::onSuccessAction, actionActivate, getAdIds(), resources)
            singleDelGroupId = ""
        }
    }

    private fun getAdIds(): MutableList<String> {
        val ads: MutableList<String> = mutableListOf()
        return if (singleDelGroupId.isEmpty()) {
            adapter?.getSelectedItems()?.forEach {
                ads.add(it.data.groupId)
            }
            ads
        } else {
            mutableListOf(singleDelGroupId)
        }
    }

    private fun statusChange(pos: Int, status: Int) {
        val resources = context?.resources ?: return
        if (status != CUREENTY_ACTIVATED)
            presenter.setGroupAction(
                ::onSuccessAction, TopAdsDashboardConstant.ACTION_ACTIVATE,
                listOf((adapter?.items?.getOrNull(pos) as? HeadLineAdItemsItemModel)?.data?.groupId
                    ?: ""), resources
            )
        else
            presenter.setGroupAction(
                ::onSuccessAction, TopAdsDashboardConstant.ACTION_DEACTIVATE,
                listOf((adapter?.items?.getOrNull(pos) as? HeadLineAdItemsItemModel)?.data?.groupId
                    ?: ""), resources
            )
    }

    private fun onSuccessAction(action: String) {
        startSelectMode(false)
        fetchFirstPage()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchFirstPage()
        btnFilter?.setOnClickListener {
            groupFilterSheet.show(childFragmentManager, "")
            groupFilterSheet.onSubmitClick = { fetchFirstPage() }
        }
        closeButton?.setOnClickListener {
            startSelectMode(false)
        }
        activate?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(
                CLICK_AKTIFKAN_LONG_PRESS,
                "{${userSession.shopId}} - {${TextUtils.join(",", getAdIds())}}",
                userSession.userId
            )
            performAction(TopAdsDashboardConstant.ACTION_ACTIVATE)
        }
        deactivate?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(
                CLICK_NONAKTIFKAN_LONG_PRESS,
                "{${userSession.shopId}} - {${TextUtils.join(",", getAdIds())}}",
                userSession.userId
            )
            performAction(TopAdsDashboardConstant.ACTION_DEACTIVATE)
        }
        delete?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(
                CLICK_HAPUS_LONG_PRESS,
                "{${userSession.shopId}} - {${TextUtils.join(",", getAdIds())}}",
                userSession.userId
            )
            showConfirmationDialog()
        }
        btnAddItem?.setOnClickListener {
            RouteManager.route(context, ApplinkConstInternalTopAds.TOPADS_CREATE_ADS)
        }
        Utils.setSearchListener(context, view, ::fetchFirstPage)
    }

    private fun showConfirmationDialog() {
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(
                String.format(
                    getString(R.string.topads_dash_headline_bulk_delete_title),
                    adapter?.getSelectedItems()?.size
                )
            )
            dialog.setDescription(getString(R.string.topads_dasg_headline_bulk_delete_desc))
            dialog.setPrimaryCTAText(getString(com.tokopedia.topads.common.R.string.topads_common_cancel_btn))
            dialog.setSecondaryCTAText(getString(R.string.topads_dash_ya_hapus))
            dialog.setPrimaryCTAClickListener {
                dialog.dismiss()
            }
            dialog.setSecondaryCTAClickListener {
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(
                    CLICK_YA_HAPUS_LONG_PRESS,
                    "{${userSession.shopId}} - {${TextUtils.join(",", getAdIds())}}",
                    userSession.userId
                )
                dialog.dismiss()
                performAction(TopAdsDashboardConstant.ACTION_DELETE)
            }
            dialog.show()
        }
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.group_list)
        searchBar = view.findViewById(R.id.searchBar)
        btnFilter = view.findViewById(R.id.btnFilter)
        filterCount = view.findViewById(R.id.filterCount)
        btnAddItem = view.findViewById(R.id.btnAddItem)
        closeButton = view.findViewById(R.id.close_butt)
        activate = view.findViewById(R.id.activate)
        deactivate = view.findViewById(R.id.deactivate)
        movetogroup = view.findViewById(R.id.movetogroup)
        delete = view.findViewById(R.id.delete)
        actionbar = view.findViewById(R.id.actionbar)
        loader = view.findViewById(R.id.loader)
    }


    private val groupFilterSheet: TopadsGroupFilterSheet by lazy {
        TopadsGroupFilterSheet.newInstance(context)
    }

    fun fetchFirstPage() {
        groupIds.clear()
        currentPageNum = 1
        loader?.visibility = View.VISIBLE
        adapter?.items?.clear()
        adapter?.notifyDataSetChanged()
        val startDate = Utils.format.format(
            (parentFragment as? TopAdsHeadlineBaseFragment)?.startDate ?: Utils.getStartDate()
        )
        val endDate = Utils.format.format(
            (parentFragment as? TopAdsHeadlineBaseFragment)?.endDate ?: Utils.getEndDate()
        )
        if(this::presenter.isInitialized) {
            presenter.getGroupData(
                currentPageNum,
                searchBar?.searchBarTextField?.text.toString(),
                groupFilterSheet.getSelectedSortId(),
                groupFilterSheet.getSelectedStatusId(),
                startDate,
                endDate,
                TopAdsDashboardConstant.GROUP_TYPE_HEADLINE,
                this::onSuccessGroupResult
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TopAdsDashboardConstant.HEADLINE_UPADTED) {
            if (resultCode == Activity.RESULT_OK)
                fetchFirstPage()
        }
    }
}
