package com.tokopedia.onboarding.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.DeeplinkDFMapper
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dynamicfeatures.DFInstaller
import com.tokopedia.onboarding.R
import com.tokopedia.onboarding.analytics.OnboardingAnalytics
import com.tokopedia.onboarding.common.ext.TabLayoutMediator
import com.tokopedia.onboarding.common.ext.TabLayoutMediator.TabConfigurationStrategy
import com.tokopedia.onboarding.di.OnboardingComponent
import com.tokopedia.onboarding.domain.model.DynamicOnboardingDataModel
import com.tokopedia.onboarding.view.adapter.PageAdapter
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.weaver.WeaveInterface
import com.tokopedia.weaver.Weaver
import kotlinx.android.synthetic.main.fragment_dynamic_onbaording.*
import org.jetbrains.annotations.NotNull
import javax.inject.Inject

class DynamicOnboardingFragment : BaseDaggerFragment(), TabConfigurationStrategy {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var onboardingAnalytics: OnboardingAnalytics

    @Inject
    lateinit var remoteConfig: RemoteConfig

    private var dynamicOnboardingDataModel = DynamicOnboardingDataModel()
    private var pagesAdapter = PageAdapter()

    override fun getScreenName(): String = OnboardingAnalytics.SCREEN_ONBOARDING

    override fun initInjector() {
        getComponent(OnboardingComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dynamic_onbaording, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            dynamicOnboardingDataModel = it.getParcelable(ARG_DYNAMIC_ONBAORDING_DATA) as DynamicOnboardingDataModel
        }

        val executeViewCreatedWeave = object : WeaveInterface {
            override fun execute(): Any {
                return executeViewCreateFlow()
            }
        }
        Weaver.executeWeaveCoRoutineWithFirebase(executeViewCreatedWeave, RemoteConfigKey.ENABLE_ASYNC_ONBOARDING_CREATE, context)
    }

    @NotNull
    private fun executeViewCreateFlow(): Boolean {
        trackPreInstall()
        initView()
        return true
    }

    private fun initView() {
        preparePages()

        pagesAdapter.clearAllItems()
        pagesAdapter.addPages(dynamicOnboardingDataModel.pageDataModels)
    }

    private fun preparePages() {
        viewPagerDynamicOnboarding?.apply {
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            adapter = pagesAdapter
            offscreenPageLimit = 2

            postDelayed({
                setCurrentItem(0, true)
                visibility = View.VISIBLE
            }, 10)

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    if (position >= pagesAdapter.itemCount - 1) {
                        nextDynamicOnbaording?.visibility = View.GONE
                    } else {
                        nextDynamicOnbaording?.visibility = View.VISIBLE
                    }

                    super.onPageSelected(position)
                }
            })
        }

        TabLayoutMediator(indicatorDynamicOnbaording, viewPagerDynamicOnboarding, this)
                .attach()

        navigationDynamicOnbaording?.apply {
            visibility = if (dynamicOnboardingDataModel.navigationDataModel.visibility) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        // TODO : If use global button
        buttonGlobalDynamicOnbaording?.apply {
            setOnClickListener(globalButtonClickListener())
        }

        skipDynamicOnbaording?.apply {
            text = dynamicOnboardingDataModel.navigationDataModel.skipButtonDataModel.name
            setOnClickListener(skipButtonClickListener())
        }

        nextDynamicOnbaording?.apply {
            text = dynamicOnboardingDataModel.navigationDataModel.nextDataModel.name
            setOnClickListener(nextButtonClickListener())
        }
    }

    override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
        viewPagerDynamicOnboarding?.setCurrentItem(position, true)
    }

    private fun globalButtonClickListener(): View.OnClickListener {
        return View.OnClickListener {
            // TODO : If global button available
        }
    }

    private fun skipButtonClickListener(): View.OnClickListener {
        return View.OnClickListener {
            activity?.let {
                onboardingAnalytics.eventOnboardingSkip(viewPagerDynamicOnboarding?.currentItem
                        ?: 0)
                RouteManager.route(it, dynamicOnboardingDataModel.navigationDataModel.skipButtonDataModel.appLink)
                finishOnBoarding()
            }
        }
    }

    private fun nextButtonClickListener(): View.OnClickListener {
        return View.OnClickListener {
            viewPagerDynamicOnboarding?.apply {
                onboardingAnalytics.eventOnboardingNext(currentItem)

                var currentPosition = currentItem
                if (currentPosition < pagesAdapter.itemCount) {
                    currentPosition++
                    setCurrentItem(currentPosition, true)
                }
            }
        }
    }

    private fun trackPreInstall() {
        if (GlobalConfig.IS_PREINSTALL) {
            onboardingAnalytics.trackMoengage()
        }
    }

    private fun finishOnBoarding() {
        activity?.let {
            userSession.setFirstTimeUserOnboarding(false)
            it.finish()
        }
    }

    companion object {
        const val ARG_DYNAMIC_ONBAORDING_DATA = "dynamicOnabordingData"

        fun createInstance(bundle: Bundle): DynamicOnboardingFragment {
            val fragment = DynamicOnboardingFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}