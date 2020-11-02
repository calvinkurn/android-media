package com.tokopedia.onboarding.view.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.TaskStackBuilder
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.DeeplinkDFMapper
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dynamicfeatures.DFInstaller
import com.tokopedia.onboarding.R
import com.tokopedia.onboarding.analytics.OnboardingAnalytics
import com.tokopedia.onboarding.common.IOnBackPressed
import com.tokopedia.onboarding.data.OnboardingConstant
import com.tokopedia.onboarding.di.OnboardingComponent
import com.tokopedia.onboarding.domain.model.ConfigDataModel
import com.tokopedia.onboarding.view.adapter.PageAdapter
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.weaver.WeaveInterface
import com.tokopedia.weaver.Weaver
import kotlinx.android.synthetic.main.fragment_dynamic_onboarding.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.annotations.NotNull
import java.util.*
import javax.inject.Inject


class DynamicOnboardingFragment : BaseDaggerFragment(), IOnBackPressed {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var onboardingAnalytics: OnboardingAnalytics

    @Inject
    lateinit var remoteConfig: RemoteConfig

    private var dynamicOnboardingDataModel = ConfigDataModel()
    private var pagesAdapter = PageAdapter()
    private lateinit var sharedPrefs: SharedPreferences

    override fun getScreenName(): String = OnboardingAnalytics.SCREEN_ONBOARDING

    override fun initInjector() {
        getComponent(OnboardingComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dynamic_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            dynamicOnboardingDataModel = it.getParcelable(ARG_DYNAMIC_ONBAORDING_DATA) ?: ConfigDataModel()
        }

        val executeViewCreatedWeave = object : WeaveInterface {
            override fun execute(): Any {
                return executeViewCreateFlow()
            }
        }
        Weaver.executeWeaveCoRoutineWithFirebase(executeViewCreatedWeave, RemoteConfigKey.ENABLE_ASYNC_ONBOARDING_CREATE, context)
    }

    override fun onBackPressed(): Boolean {
        finishOnBoarding()
        return true
    }

    @NotNull
    private fun executeViewCreateFlow(): Boolean {
        GlobalScope.launch(Dispatchers.Main) {
            trackPreInstall()
            initView()
        }
        return true
    }

    private fun initView() {
        preparePages()

        pagesAdapter.clearAllItems()
        pagesAdapter.addPages(dynamicOnboardingDataModel.pageDataModels)

        pageDots?.addDots(pagesAdapter.itemCount)
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

            pageDots?.setViewpager(this)
            registerOnPageChangeCallback(OnPageChangeListener())
        }

        navigationDynamicOnbaording?.apply {
            visibility = if (dynamicOnboardingDataModel.navigationDataModel.visibility) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        skipDynamicOnbaording?.apply {
            visibility = if (dynamicOnboardingDataModel.navigationDataModel.skipButtonDataModel.visibility) {
                View.VISIBLE
            } else {
                View.GONE
            }

            setOnClickListener(skipButtonClickListener(dynamicOnboardingDataModel.navigationDataModel.skipButtonDataModel.appLink))
        }

        nextDynamicOnbaording?.apply {
            visibility = if (dynamicOnboardingDataModel.navigationDataModel.nextDataModel.visibility) {
                View.VISIBLE
            } else {
                View.GONE
            }

            setOnClickListener(nextButtonClickListener())
        }

        pageDots?.apply {
            visibility = if (dynamicOnboardingDataModel.navigationDataModel.indicatorsDataModel.visibility) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        checkGlobalButtonState(0)
    }

    private fun globalButtonClickListener(appLink: String): View.OnClickListener {
        return View.OnClickListener {
            onboardingAnalytics.eventOnboardingJoin(viewPagerDynamicOnboarding?.currentItem ?: 0)
            startActivityWithBackTask(appLink)
        }
    }

    private fun skipButtonClickListener(appLink: String): View.OnClickListener {
        return View.OnClickListener {
            onboardingAnalytics.eventOnboardingSkip(viewPagerDynamicOnboarding?.currentItem ?: 0)
            startActivityWithBackTask(appLink)
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

    private fun checkGlobalButtonState(position: Int) {
        buttonGlobalDynamicOnbaording?.apply {
            val buttonDataModel = dynamicOnboardingDataModel.pageDataModels[position].componentsDataModel.buttonDataModel
            val appLink = buttonDataModel.appLink

            text = buttonDataModel.text
            visibility = if (buttonDataModel.visibility) {
                View.VISIBLE
            } else {
                View.GONE
            }

            setOnClickListener(globalButtonClickListener(appLink))
        }
    }

    private fun trackPreInstall() {
        if (GlobalConfig.IS_PREINSTALL) {
            onboardingAnalytics.trackMoengage()
        }
        onboardingAnalytics.trackScreen(0)
    }

    private fun startActivityWithBackTask(appLink: String) {
        context?.let {
            val taskStackBuilder = TaskStackBuilder.create(it)
            val defferedDeeplinkPath = TrackApp.getInstance().appsFlyer.defferedDeeplinkPathIfExists
            val homeIntent = RouteManager.getIntent(it, ApplinkConst.HOME)
            val page = RouteManager.getIntent(it, appLink)

            if (defferedDeeplinkPath.isEmpty()) {

                if (appLink.contains(ApplinkConst.REGISTER)) {
                    page.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, OnboardingConstant.PARAM_SOURCE_ONBOARDING)
                }

                if (appLink != ApplinkConst.HOME) {
                    taskStackBuilder.addNextIntent(homeIntent)
                    taskStackBuilder.addNextIntent(page)
                    taskStackBuilder.startActivities()
                } else {
                    it.startActivity(page)
                }
            } else {
                RouteManager.route(it, TrackApp.getInstance().appsFlyer.defferedDeeplinkPathIfExists)
            }

            finishOnBoarding()
        }
    }

    private fun finishOnBoarding() {
        activity?.let {
            saveFirstInstallTime()
            userSession.setFirstTimeUserOnboarding(false)
            it.finish()
        }
    }

    private fun saveFirstInstallTime() {
        context?.let {
            val date = Date()
            sharedPrefs = it.getSharedPreferences(
                    OnboardingFragment.KEY_FIRST_INSTALL_SEARCH, Context.MODE_PRIVATE)
            sharedPrefs.edit().putLong(
                    OnboardingFragment.KEY_FIRST_INSTALL_TIME_SEARCH, date.time).apply()
        }
    }

    inner class OnPageChangeListener : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            if (position >= pagesAdapter.itemCount - 1) {
                nextDynamicOnbaording?.visibility = View.GONE
            } else {
                nextDynamicOnbaording?.visibility = View.VISIBLE
            }

            onboardingAnalytics.trackScreen(position)
            checkGlobalButtonState(position)
            pageDots?.setCurrent(position)
            super.onPageSelected(position)
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