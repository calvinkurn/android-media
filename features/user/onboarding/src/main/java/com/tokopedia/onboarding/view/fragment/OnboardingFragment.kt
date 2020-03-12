package com.tokopedia.onboarding.view.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.TaskStackBuilder
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.DeeplinkDFMapper
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dynamicfeatures.DFInstaller
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.onboarding.R
import com.tokopedia.onboarding.analytics.OnboardingAnalytics
import com.tokopedia.onboarding.common.IOnBackPressed
import com.tokopedia.onboarding.data.OnboardingScreenItem
import com.tokopedia.onboarding.di.OnboardingComponent
import com.tokopedia.onboarding.view.adapter.OnboardingViewPagerAdapter
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.track.TrackApp
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.weaver.WeaveInterface
import com.tokopedia.weaver.Weaver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.annotations.NotNull
import javax.inject.Inject


/**
 * Created by Ade Fulki on 2020-02-08.
 * ade.hadian@tokopedia.com
 */

class OnboardingFragment : BaseDaggerFragment(), IOnBackPressed {

    private lateinit var screenViewpager: ViewPager
    private lateinit var skipAction: Typography
    private lateinit var nextAction: Typography
    private lateinit var joinButton: UnifyButton
    private lateinit var tabIndicator: TabLayout

    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var onboardingAnalytics: OnboardingAnalytics
    @Inject
    lateinit var remoteConfig: RemoteConfig

    private lateinit var onboardingViewPagerAdapter: OnboardingViewPagerAdapter

    override fun getScreenName(): String = OnboardingAnalytics.SCREEN_ONBOARDING

    override fun initInjector() = getComponent(OnboardingComponent::class.java).inject(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_onboarding, container, false)
        screenViewpager = view.findViewById(R.id.screen_viewpager)
        skipAction = view.findViewById(R.id.skip_action)
        nextAction = view.findViewById(R.id.next_action)
        joinButton = view.findViewById(R.id.join_button)
        tabIndicator = view.findViewById(R.id.tab_indicator)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val executeViewCreatedWeave = object : WeaveInterface {
            override fun execute(): Any {
                return executeViewCreateFlow()
            }
        }
        Weaver.executeWeaveCoRoutineWithFirebase(executeViewCreatedWeave, RemoteConfigKey.ENABLE_ASYNC_ONBOARDING_CREATE, context)
    }

    @NotNull
    private fun executeViewCreateFlow() : Boolean{
        GlobalScope.launch(Dispatchers.Main) {
            trackPreinstall()
            initView()
        }
        return true
    }

    private fun initView() {
        context?.let {
            val listItem = generateListAllButton()

            onboardingViewPagerAdapter = OnboardingViewPagerAdapter(it, listItem)
            screenViewpager.apply {
                adapter = onboardingViewPagerAdapter
                offscreenPageLimit = 2
                addOnPageChangeListener(OnPageChangeListener())
            }

            tabIndicator.setupWithViewPager(screenViewpager)

            skipAction.setOnClickListener(skipActionClickListener())
            nextAction.setOnClickListener(nextActionClickListener())
            joinButton.setOnClickListener(joinActionClickListener())
        }
    }

    private fun generateListAllButton(): List<OnboardingScreenItem> {
        val listItem = arrayListOf<OnboardingScreenItem>()
        context?.let {
            listItem.add(OnboardingScreenItem(
                    title = getString(R.string.title_screen_item_page_1),
                    imageUrl = ONBOARD_IMAGE_PAGE_1_URL,
                    placeholder = R.drawable.onboarding_image_page_1_placeholder
            ))
            listItem.add(OnboardingScreenItem(
                    title = getString(R.string.title_screen_item_page_2),
                    imageUrl = ONBOARD_IMAGE_PAGE_2_URL,
                    placeholder = R.drawable.onboarding_image_page_2_placeholder
            ))
            listItem.add(OnboardingScreenItem(
                    title = getString(R.string.title_screen_item_page_3),
                    imageUrl = ONBOARD_IMAGE_PAGE_3_URL,
                    placeholder = R.drawable.onboarding_image_page_3_placeholder
            ))
        }
        return listItem
    }

    private fun trackPreinstall() {
        if (GlobalConfig.IS_PREINSTALL) {
            onboardingAnalytics.trackMoengage()
        }
        onboardingAnalytics.trackScreen(0)
    }

    private fun joinActionClickListener(): View.OnClickListener {
        return View.OnClickListener {
            context?.let {
                onboardingAnalytics.eventOnboardingJoin(screenViewpager.currentItem)
                if (TextUtils.isEmpty(TrackApp.getInstance().appsFlyer.defferedDeeplinkPathIfExists)) {
                    startActivityWithBackTask()
                } else {
                    RouteManager.route(it, TrackApp.getInstance().appsFlyer.defferedDeeplinkPathIfExists)
                }
            }
        }
    }

    private fun skipActionClickListener(): View.OnClickListener {
        return View.OnClickListener {
            context?.let {
                onboardingAnalytics.eventOnboardingSkip(screenViewpager.currentItem)
                val intent = if (TextUtils.isEmpty(TrackApp.getInstance().appsFlyer.defferedDeeplinkPathIfExists)) {
                    RouteManager.getIntent(it, ApplinkConst.HOME)
                } else {
                    RouteManager.getIntent(it, TrackApp.getInstance().appsFlyer.defferedDeeplinkPathIfExists)
                }
                startActivity(intent)
                finishOnBoarding()
            }
        }
    }

    private fun nextActionClickListener(): View.OnClickListener {
        return View.OnClickListener {
            var position = screenViewpager.currentItem
            onboardingAnalytics.eventOnboardingNext(position)
            val size = onboardingViewPagerAdapter.listScreen.size
            if (position < size) {
                position++
                screenViewpager.currentItem = position
            }
        }
    }

    private fun startActivityWithBackTask() {
        context?.let {
            val taskStackBuilder = TaskStackBuilder.create(it)
            val homeIntent = RouteManager.getIntent(it, ApplinkConst.HOME)
            val loginIntent = RouteManager.getIntent(it, ApplinkConst.REGISTER)

            finishOnBoarding()

            taskStackBuilder.addNextIntent(homeIntent)
            taskStackBuilder.addNextIntent(loginIntent)
            taskStackBuilder.startActivities()
        }
    }

    private fun hideNextAction() {
        nextAction.invisible()
    }

    private fun showNextAction() {
        nextAction.show()
    }

    private fun loadLastScreen() {
        hideNextAction()
    }

    private fun finishOnBoarding() {
        activity?.let {
            userSession.setFirstTimeUserOnboarding(false)
            DFInstaller().uninstallOnBackground(it.application, listOf(DeeplinkDFMapper.DFM_ONBOARDING))
            it.finish()
        }
    }

    override fun onBackPressed(): Boolean {
        finishOnBoarding()
        return true
    }

    inner class OnPageChangeListener: ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) { }
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) { }

        override fun onPageSelected(position: Int) {
            onboardingAnalytics.trackScreen(position)
            val size = onboardingViewPagerAdapter.listScreen.size
            if (position < size) {
                showNextAction()
            }

            if (position == size - 1) {
                loadLastScreen()
            }
        }
    }

    companion object {
        const val ONBOARD_IMAGE_PAGE_1_URL = "https://ecs7.tokopedia.net/android/others/onboarding_image_page_1.png"
        const val ONBOARD_IMAGE_PAGE_2_URL = "https://ecs7.tokopedia.net/android/others/onboarding_image_page_2.png"
        const val ONBOARD_IMAGE_PAGE_3_URL = "https://ecs7.tokopedia.net/android/others/onboarding_image_page_3.png"

        fun createInstance(bundle: Bundle): OnboardingFragment {
            val fragment = OnboardingFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}