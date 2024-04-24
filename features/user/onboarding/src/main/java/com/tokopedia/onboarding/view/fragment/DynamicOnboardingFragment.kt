package com.tokopedia.onboarding.view.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.logger.utils.globalScopeLaunch
import com.tokopedia.onboarding.R
import com.tokopedia.onboarding.analytics.OnboardingAnalytics
import com.tokopedia.onboarding.common.IOnBackPressed
import com.tokopedia.onboarding.databinding.FragmentDynamicOnboardingBinding
import com.tokopedia.onboarding.di.OnboardingComponent
import com.tokopedia.onboarding.domain.model.ConfigDataModel
import com.tokopedia.onboarding.view.adapter.PageAdapter
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.weaver.WeaveInterface
import com.tokopedia.weaver.Weaver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    private val binding: FragmentDynamicOnboardingBinding? by viewBinding()

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
        Weaver.executeWeaveCoRoutineWithFirebase(executeViewCreatedWeave, RemoteConfigKey.ENABLE_ASYNC_ONBOARDING_CREATE, context, true)
    }

    override fun onBackPressed(): Boolean {
        finishOnBoarding()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_NEXT_PAGE -> {
                activity?.let {
                    val intentHome = RouteManager.getIntent(activity, ApplinkConst.HOME)
                    intentHome.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    it.startActivity(intentHome)
                    it.finish()
                }
            }

            else -> {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    @NotNull
    private fun executeViewCreateFlow(): Boolean {
        globalScopeLaunch({
            withContext(Dispatchers.Main) {
                initView()
            }
        }, onFinish = {
                trackPreInstall()
            })
        return true
    }

    private fun initView() {
        preparePages()

        pagesAdapter.clearAllItems()
        pagesAdapter.addPages(dynamicOnboardingDataModel.pageDataModels)

        binding?.pageDots?.addDots(pagesAdapter.itemCount)
    }

    private fun preparePages() {
        binding?.viewPagerDynamicOnboarding?.apply {
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            adapter = pagesAdapter
            offscreenPageLimit = 2

            postDelayed({
                setCurrentItem(0, true)
                visibility = View.VISIBLE
            }, DELAY)

            binding?.pageDots?.setViewpager(this)
            registerOnPageChangeCallback(OnPageChangeListener())
        }

        binding?.navigationDynamicOnbaording?.apply {
            visibility = if (dynamicOnboardingDataModel.navigationDataModel.visibility) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        binding?.skipDynamicOnbaording?.apply {
            visibility = if (dynamicOnboardingDataModel.navigationDataModel.skipButtonDataModel.visibility) {
                View.VISIBLE
            } else {
                View.GONE
            }

            setOnClickListener(skipButtonClickListener(dynamicOnboardingDataModel.navigationDataModel.skipButtonDataModel.appLink))
        }

        binding?.nextDynamicOnbaording?.apply {
            visibility = if (dynamicOnboardingDataModel.navigationDataModel.nextDataModel.visibility) {
                View.VISIBLE
            } else {
                View.GONE
            }

            setOnClickListener(nextButtonClickListener())
        }

        binding?.pageDots?.apply {
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
            onboardingAnalytics.eventOnboardingJoin(binding?.viewPagerDynamicOnboarding?.currentItem ?: 0)
            goToNextPage(appLink)
        }
    }

    private fun skipButtonClickListener(appLink: String): View.OnClickListener {
        return View.OnClickListener {
            onboardingAnalytics.eventOnboardingSkip(binding?.viewPagerDynamicOnboarding?.currentItem ?: 0)
            goToNextPage(appLink)
        }
    }

    private fun nextButtonClickListener(): View.OnClickListener {
        return View.OnClickListener {
            binding?.viewPagerDynamicOnboarding?.apply {
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
        binding?.buttonGlobalDynamicOnbaording?.apply {
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

    private fun goToNextPage(appLink: String) {
        context?.let {
            finishOnBoarding()
            val defferedDeeplinkPath = TrackApp.getInstance().appsFlyer.defferedDeeplinkPathIfExists
            val page = RouteManager.getIntent(it, appLink)
            if (defferedDeeplinkPath.isEmpty()) {
                if (appLink == ApplinkConst.REGISTER || appLink == ApplinkConst.LOGIN) {
                    startActivityForResult(page, REQUEST_NEXT_PAGE)
                } else if (appLink != ApplinkConst.HOME) {
                    val intentHome = RouteManager.getIntent(activity, ApplinkConst.HOME)
                    intentHome.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    activity?.startActivities(arrayOf(intentHome, page))
                    activity?.finish()
                } else {
                    activity?.startActivity(page)
                    activity?.finish()
                }
            } else {
                RouteManager.route(it, TrackApp.getInstance().appsFlyer.defferedDeeplinkPathIfExists)
                activity?.finish()
            }
        }
    }

    private fun finishOnBoarding() {
        activity?.let {
            saveFirstInstallTime()
            userSession.setFirstTimeUserOnboarding(false)
        }
    }

    private fun saveFirstInstallTime() {
        context?.let {
            val date = Date()
            sharedPrefs = it.getSharedPreferences(
                OnboardingFragment.KEY_FIRST_INSTALL_SEARCH,
                Context.MODE_PRIVATE
            )
            sharedPrefs.edit().putLong(
                OnboardingFragment.KEY_FIRST_INSTALL_TIME_SEARCH,
                date.time
            ).apply()
        }
    }

    inner class OnPageChangeListener : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            if (position >= pagesAdapter.itemCount - 1) {
                binding?.nextDynamicOnbaording?.visibility = View.GONE
            } else {
                binding?.nextDynamicOnbaording?.visibility = View.VISIBLE
            }

            onboardingAnalytics.trackScreen(position)
            checkGlobalButtonState(position)
            binding?.pageDots?.setCurrent(position)
            super.onPageSelected(position)
        }
    }

    companion object {
        const val ARG_DYNAMIC_ONBAORDING_DATA = "dynamicOnabordingData"

        private const val REQUEST_NEXT_PAGE = 679
        private const val DELAY = 10L

        fun createInstance(bundle: Bundle): DynamicOnboardingFragment {
            val fragment = DynamicOnboardingFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
