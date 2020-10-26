package com.tokopedia.salam.umrah.travel.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.analytics.*
import com.tokopedia.salam.umrah.common.data.TravelAgent
import com.tokopedia.salam.umrah.common.data.UmrahItemWidgetModel
import com.tokopedia.salam.umrah.common.util.UmrahQuery
import com.tokopedia.salam.umrah.common.util.UmrahShare
import com.tokopedia.salam.umrah.travel.data.UmrahTravelAgentBySlugNameEntity
import com.tokopedia.salam.umrah.travel.di.UmrahTravelComponent
import com.tokopedia.salam.umrah.travel.presentation.activity.UmrahTravelActivity
import com.tokopedia.salam.umrah.travel.presentation.activity.UmrahTravelActivity.Companion.EXTRA_SLUG_NAME
import com.tokopedia.salam.umrah.travel.presentation.adapter.UmrahTravelAgentViewPagerAdapter
import com.tokopedia.salam.umrah.travel.presentation.viewmodel.UmrahTravelViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.bottom_sheet_umrah_travel_agent_permission.view.*
import kotlinx.android.synthetic.main.fragment_umrah_travel_agent.*
import kotlinx.android.synthetic.main.fragment_umrah_travel_agent.view.*
import kotlinx.android.synthetic.main.widget_umrah_item.*
import javax.inject.Inject

/**
 * @author by Firman on 22/1/20
 */

class UmrahTravelFragment : BaseDaggerFragment(), UmrahTravelActivity.TravelListener {

    @Inject
    lateinit var umrahTravelViewModel: UmrahTravelViewModel

    @Inject
    lateinit var umrahTrackingUtil: UmrahTrackingAnalytics

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    var travelAgent: TravelAgent = TravelAgent()
    var positionBefore: Int = 0

    private lateinit var umrahTravelAgentViewPagerAdapter: UmrahTravelAgentViewPagerAdapter

    lateinit var swipeToRefresh : SwipeRefreshLayout
    lateinit var performanceMonitoring: PerformanceMonitoring

    private var slugName: String? = ""
    private val OFF_SCREEN_LIMIT = 3

    override fun getScreenName(): String = getString(R.string.umrah_travel_agent_title)

    override fun initInjector() = getComponent(UmrahTravelComponent::class.java).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializePerformance()
        slugName = savedInstanceState?.getString(EXTRA_SLUG_NAME)
                ?: arguments?.getString(EXTRA_SLUG_NAME) ?: ""
    }

    private fun requestData() {
        slugName?.let {
            umrahTravelViewModel.requestTravelData(
                    UmrahQuery.UMRAH_TRAVEL_BY_SLUGNAME, it)
        }
    }

    private fun initializePerformance(){
        performanceMonitoring = PerformanceMonitoring.start(UMRAH_TRAVEL_MAIN_PAGE_PERFORMANCE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_umrah_travel_agent, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSwipeToRefresh(view)
        requestData()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        umrahTravelViewModel.travelAgentData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    travelAgent = it.data.umrahTravelAgentBySlug
                    setupAll(it.data)
                    performanceMonitoring.stopTrace()
                }
                is Fail -> {
                    performanceMonitoring.stopTrace()
                    NetworkErrorHelper.showEmptyState(context, view?.rootView, it.throwable.message, null, null, R.drawable.umrah_img_empty_search_png) {
                        requestData()
                    }
                }

            }

        })
    }

    companion object {
        fun getInstance(slugName: String) =
                UmrahTravelFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_SLUG_NAME, slugName)
                    }
                }

        const val REQUEST_CODE_LOGIN = 400
        const val EXTRA_SLUGNAME = "EXTRA_SLUGNAME"

        const val POSITION_PRODUCT = 0
        const val POSITION_GALLERY = 1
        const val POSITION_INFO = 2

        const val UMRAH_TRAVEL_MAIN_PAGE_PERFORMANCE = "sl_umrah_travel_agent_info"
    }

    private fun setupSwipeToRefresh(view: View) {
        swipeToRefresh = view.umrah_travel_swipe_to_refresh
        swipeToRefresh.setColorSchemeColors(resources.getColor(com.tokopedia.unifyprinciples.R.color.Green_G600))
        swipeToRefresh.setOnRefreshListener {
            initializePerformance()
            hideLayout()
            swipeToRefresh.isRefreshing = true
            requestData()
        }
    }

    private fun enableSwipeToRefresh() {
        swipeToRefresh.isRefreshing = false
        swipeToRefresh.isEnabled = true
    }

    private fun showLayout() {
        container_umrah_travel_shimmering.gone()
        container_umrah_travel.show()
    }

    private fun hideLayout() {
        container_umrah_travel_shimmering.show()
        container_umrah_travel.gone()
    }

    private fun setupAll(travelAgentBySlugName: UmrahTravelAgentBySlugNameEntity) {
        showLayout()
        setupTravelAgent(travelAgentBySlugName.umrahTravelAgentBySlug)
        setupViewPager(travelAgentBySlugName)
        setupChat()
        enableSwipeToRefresh()
    }

    private fun setupChat() {
        btn_umrah_travel_contact.setOnClickListener {
            umrahTrackingUtil.umrahTravelAgentClickHubungiTravel(getEventCategoryTracking(getCurrentPositionViewPager()))
            checkChatSession()
        }
    }

    private fun setupViewPager(travelAgentBySlugName: UmrahTravelAgentBySlugNameEntity) {
        slugName?.let {
            umrahTravelAgentViewPagerAdapter = UmrahTravelAgentViewPagerAdapter(childFragmentManager, it, travelAgentBySlugName)
            vp_umrah_travel_agent.adapter = umrahTravelAgentViewPagerAdapter
            vp_umrah_travel_agent.offscreenPageLimit = OFF_SCREEN_LIMIT
            tl_umrah_travel_agent.setupWithViewPager(vp_umrah_travel_agent)
            vp_umrah_travel_agent.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    when (position) {
                        POSITION_PRODUCT -> {
                            if (getPositionBeforeChange() == POSITION_GALLERY)
                                umrahTrackingUtil.umrahTravelAgentClickPacketUmroh(getEventCategoryTracking(POSITION_GALLERY))
                            else umrahTrackingUtil.umrahTravelAgentClickPacketUmroh(getEventCategoryTracking(POSITION_PRODUCT))

                            setPositionBeforeChange(position)
                        }
                        POSITION_GALLERY -> {
                            if (getPositionBeforeChange() == POSITION_PRODUCT)
                                umrahTrackingUtil.umrahTravelAgentClickGaleri(getEventCategoryTracking(POSITION_PRODUCT))
                            else if (positionBefore == POSITION_INFO)
                                umrahTrackingUtil.umrahTravelAgentClickGaleri(getEventCategoryTracking(POSITION_GALLERY))
                            vp_umrah_travel_agent.adapter?.let{
                                val fragmentGallery = it.instantiateItem(vp_umrah_travel_agent,POSITION_GALLERY) as UmrahTravelAgentGalleryFragment
                                fragmentGallery.firstTracking()
                            }
                            setPositionBeforeChange(position)
                        }
                        POSITION_INFO -> {
                            if (getPositionBeforeChange() == POSITION_GALLERY)
                                umrahTrackingUtil.umrahTravelAgentClickInfo(getEventCategoryTracking(POSITION_GALLERY))
                            else umrahTrackingUtil.umrahTravelAgentClickInfo(getEventCategoryTracking(POSITION_PRODUCT))

                            setPositionBeforeChange(position)
                        }
                    }
                }
            })
        }
    }

    private fun setupTravelAgent(travelAgent: TravelAgent) {
        val umrahItemWidgetModelData: UmrahItemWidgetModel = UmrahItemWidgetModel().apply {
            title = travelAgent.name
            imageUri = travelAgent.imageUrl
            desc = travelAgent.permissionOfUmrah
        }

        iw_umrah_travel_agent.apply {
            umrahItemWidgetModel = umrahItemWidgetModelData
            buildView()
            setPermissionTravel()
            setVerifiedTravel()
        }

        tg_widget_umrah_pdp_item_desc.setOnClickListener {
            umrahTrackingUtil.umrahTravelAgentClickNumberRegistration(getEventCategoryTracking(getCurrentPositionViewPager()))
            showPermissionUmrah(travelAgent.permissionOfUmrah)
        }
    }

    private fun checkChatSession() {
        if (userSessionInterface.isLoggedIn) {
            context?.let {
                startChatUmrah(it)
            }
        } else {
            goToLoginPage()
        }
    }

    private fun startChatUmrah(context: Context) {
        val intent = RouteManager.getIntent(context,
                ApplinkConst.TOPCHAT_ASKSELLER,
                resources.getString(R.string.umrah_shop_id), "",
                resources.getString(R.string.umrah_shop_source), resources.getString(R.string.umrah_shop_name), "")
        startActivity(intent)
    }

    private fun showPermissionUmrah(permissionUmrah: String) {
        val permissionBottomSheet = BottomSheetUnify()
        permissionBottomSheet.clearClose(true)
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_umrah_travel_agent_permission, null)
        view.apply {
            tg_umrah_permission_desc.text = getString(R.string.umrah_travel_permission_desc, permissionUmrah)
            btn_travel_permission_close.setOnClickListener {
                umrahTrackingUtil.umrahTravelAgentCloseVerification()
                permissionBottomSheet.dismiss()
            }
        }
        permissionBottomSheet.setChild(view)
        permissionBottomSheet.show(fragmentManager!!, "")

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_LOGIN -> context?.let { checkChatSession() }
            }
        }
    }

    private fun goToLoginPage() {
        if (activity != null) {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                    REQUEST_CODE_LOGIN)
        }
    }

    override fun onBackPressed() {
        if (!isDetached) {
            if (getCurrentPositionViewPager() == POSITION_GALLERY)
                umrahTrackingUtil.umrahTravelAgentClickBack(UMRAH_TRAVEL_PAGE_GALERY_CATEGORY)
            else umrahTrackingUtil.umrahTravelAgentClickBack(UMRAH_TRAVEL_PAGE_CATEGORY)
        }
    }

    override fun shareTravelLink() {
        umrahTrackingUtil.umrahTravelAgentThreeDots(getEventCategoryTracking(getCurrentPositionViewPager()),UMRAH_TRAVEL_THREE_DOT_SHARE)
        activity?.run {
            UmrahShare(this).shareTravelAgent(travelAgent, { showLoading() }, { hideLoading() },this.applicationContext)
        }
    }

    override fun clickHelp() {
        umrahTrackingUtil.umrahTravelAgentThreeDots(getEventCategoryTracking(getCurrentPositionViewPager()),UMRAH_TRAVEL_THREE_DOT_HELP)
    }

    override fun clickSalam() {
        umrahTrackingUtil.umrahTravelAgentThreeDots(getEventCategoryTracking(getCurrentPositionViewPager()), UMRAH_TRAVEL_THREE_DOT_SALAM)
    }

    fun getCurrentPositionViewPager(): Int {
        return vp_umrah_travel_agent.currentItem
    }

    fun setPositionBeforeChange(positionBefore: Int) {
        this.positionBefore = positionBefore
    }

    fun getPositionBeforeChange(): Int {
        return positionBefore
    }

    fun getEventCategoryTracking(position: Int): String {
       return when(position){
           POSITION_PRODUCT -> UMRAH_TRAVEL_PAGE_CATEGORY
           POSITION_GALLERY -> UMRAH_TRAVEL_PAGE_GALERY_CATEGORY
           POSITION_INFO -> UMRAH_TRAVEL_PAGE_INFO_CATEGORY
           else -> ""
        }
    }

    fun hideLoading() {
        umrah_pb_travel_share.hide()
    }

    fun showLoading() {
        umrah_pb_travel_share.show()
    }

    override fun onDestroyView() {
        performanceMonitoring.stopTrace()
        super.onDestroyView()
    }
}