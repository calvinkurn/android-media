package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.media.loader.clearImage
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmProgramDetailCallback
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.tracker.TmTracker
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_ACTION
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_ID
import com.tokopedia.tokomember_seller_dashboard.util.PATH_TOKOMEMBER_COUPON_LIST
import com.tokopedia.tokomember_seller_dashboard.util.PATH_TOKOMEMBER_PROGRAM_LIST
import com.tokopedia.tokomember_seller_dashboard.util.TM_FEEDBACK_URL
import com.tokopedia.tokomember_seller_dashboard.util.TOKOMEMBER_SCREEN
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TokomemberDashHomeViewpagerAdapter
import com.tokopedia.unifycomponents.TabsUnify
import java.util.*
import javax.inject.Inject

class TokomemberDashHomeMainFragment : BaseDaggerFragment() {

    private lateinit var homeHeader: HeaderUnify
    private lateinit var homeTabs: TabsUnify
    private lateinit var homeViewPager: ViewPager
    private lateinit var homeFragmentCallback: TmProgramDetailCallback
    private var programActionType = -1
    private var tmTracker : TmTracker? = null

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TmProgramDetailCallback) {
            homeFragmentCallback =  context as TmProgramDetailCallback
        } else {
            throw RuntimeException(requireContext().toString() )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tmTracker = TmTracker()
        return inflater.inflate(R.layout.tm_dash_home_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeHeader = view.findViewById(R.id.home_header)
        homeTabs = view.findViewById(R.id.home_tabs)
        homeViewPager = view.findViewById(R.id.home_viewpager)
        programActionType = arguments?.getInt(BUNDLE_PROGRAM_ACTION)?:0

        homeHeader.apply {
            title = "TokoMember"
            isShowBackButton = true
            isShowShadow = false
            setNavigationOnClickListener {
                activity?.finish()
            }

            val feedbackIcon = addRightIcon(0)
            feedbackIcon.clearImage()
            feedbackIcon.setImageDrawable(getIconUnifyDrawable(context, IconUnify.CHAT_REPORT))
            feedbackIcon.setColorFilter(
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_Static_Black
                ),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
            feedbackIcon.setOnClickListener {
                tmTracker?.clickHomeFeedback(shopId = arguments?.getInt(BUNDLE_SHOP_ID).toString())
                RouteManager.route(context,String.format(Locale.getDefault(), "%s?url=%s", ApplinkConst.WEBVIEW, Uri.encode(TM_FEEDBACK_URL)))
            }

        }

        homeTabs.setupWithViewPager(homeViewPager)
        homeTabs.getUnifyTabLayout().setupWithViewPager(homeViewPager)

        val adapter = TokomemberDashHomeViewpagerAdapter(childFragmentManager)
        adapter.addFragment(TokomemberDashHomeFragment.newInstance(arguments), "Home")
        adapter.addFragment(TokomemberDashProgramListFragment.newInstance(arguments), "Program")
        adapter.addFragment(TokomemberDashCouponFragment.newInstance(arguments), "Kupon Tokomember")

        homeViewPager.adapter = adapter
        homeViewPager.offscreenPageLimit = 3

        homeTabs.getUnifyTabLayout().removeAllTabs()
        homeTabs.customTabMode = TabLayout.MODE_SCROLLABLE
        homeTabs.addNewTab("Home")
        homeTabs.addNewTab("Program")
        homeTabs.addNewTab("Kupon Tokomember")

        if (programActionType!=-1) {
            setTabsProgramList()
        }
        routeToScreen()

        homeViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                when(position){
                    0 ->{
                        tmTracker?.viewHomeTabsSection(arguments?.getInt(BUNDLE_SHOP_ID).toString())
                    }
                    1 ->{
                        tmTracker?.viewProgramListTabSection(arguments?.getInt(BUNDLE_SHOP_ID).toString())
                    }
                    2 ->{
                        tmTracker?.viewCouponListTabSection(arguments?.getInt(BUNDLE_SHOP_ID).toString())
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })
    }

    private fun setTabsProgramList(){
        homeTabs.getUnifyTabLayout().getTabAt(1)?.select()
        homeViewPager.currentItem = 1
    }

    private fun setTabsCouponList(){
        homeTabs.getUnifyTabLayout().getTabAt(2)?.select()
        homeViewPager.currentItem = 2
    }

    private fun routeToScreen(){
        val screen = arguments?.get(TOKOMEMBER_SCREEN) as? Uri
        screen?.let{
            when(it.lastPathSegment){
                PATH_TOKOMEMBER_PROGRAM_LIST -> setTabsProgramList()
                PATH_TOKOMEMBER_COUPON_LIST -> setTabsCouponList()
            }
        }
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder().baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent).build().inject(this)
    }

    companion object {
        const val TAG_HOME = "tag_home"
        fun newInstance(extras: Bundle?) = TokomemberDashHomeMainFragment().apply {
            arguments = extras
        }
    }
}
