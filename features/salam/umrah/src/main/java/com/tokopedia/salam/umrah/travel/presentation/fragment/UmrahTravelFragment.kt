package com.tokopedia.salam.umrah.travel.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingAnalytics
import com.tokopedia.salam.umrah.common.data.TravelAgent
import com.tokopedia.salam.umrah.common.data.UmrahItemWidgetModel
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
import kotlinx.android.synthetic.main.widget_umrah_item.*
import javax.inject.Inject

/**
 * @author by Firman on 22/1/20
 */

class UmrahTravelFragment : BaseDaggerFragment(), UmrahTravelActivity.OnBackListener {

    @Inject
    lateinit var umrahTravelViewModel: UmrahTravelViewModel

    @Inject
    lateinit var umrahTrackingUtil: UmrahTrackingAnalytics

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    var travelAgent: TravelAgent = TravelAgent()

    private lateinit var umrahTravelAgentViewPagerAdapter: UmrahTravelAgentViewPagerAdapter


    private var slugName: String? = ""
    private val OFF_SCREEN_LIMIT = 3

    override fun getScreenName(): String = getString(R.string.umrah_travel_agent_title)

    override fun initInjector() = getComponent(UmrahTravelComponent::class.java).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        slugName = savedInstanceState?.getString(EXTRA_SLUG_NAME)
                ?: arguments?.getString(EXTRA_SLUG_NAME) ?: ""
    }

    private fun requestData() {
        slugName?.let {
            umrahTravelViewModel.requestTravelData(
                    GraphqlHelper.loadRawString(resources, R.raw.gql_query_umrah_travel_by_slugname), it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_umrah_travel_agent, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestData()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        umrahTravelViewModel.travelAgentData.observe(this, Observer {
            when (it) {
                is Success -> {
                    travelAgent = it.data.umrahTravelAgentBySlug
                    setupAll(it.data)
                }
                is Fail -> {
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
    }

    private fun setupChat() {
        btn_umrah_travel_contact.setOnClickListener {
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
                override fun onPageScrollStateChanged(state: Int) {}
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

                override fun onPageSelected(position: Int) {
                    when (position) {
                        0 -> umrahTrackingUtil.umrahTravelAgentClickPacketUmroh()
                        1 -> umrahTrackingUtil.umrahTravelAgentClickGaleri()
                        2 -> umrahTrackingUtil.umrahTravelAgentClickInfo()
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
            setPermissionPdp()
            setVerifiedTravel()
        }

        tg_widget_umrah_pdp_item_desc.setOnClickListener {
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
            umrahTrackingUtil.umrahTravelAgentClickBack()
        }
    }

    override fun shareTravelLink() {
        activity?.run {
            UmrahShare(this).share(travelAgent, { showLoading() }, { hideLoading() })
        }
    }

    fun hideLoading() {
        umrah_pb_travel_share.hide()
    }

    fun showLoading() {
        umrah_pb_travel_share.show()
    }
}