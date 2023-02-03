package com.tokopedia.onboarding.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.onboarding.R
import com.tokopedia.onboarding.analytics.OnboardingAnalytics
import com.tokopedia.onboarding.common.IOnBackPressed
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_IS_SMART_REGISTER
import com.tokopedia.onboarding.data.OnboardingConstant.PARAM_SOURCE_ONBOARDING
import com.tokopedia.onboarding.data.OnboardingScreenItem
import com.tokopedia.onboarding.databinding.FragmentOnboardingBinding
import com.tokopedia.onboarding.di.OnboardingComponent
import com.tokopedia.onboarding.view.adapter.OnboardingViewPagerAdapter
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.weaver.WeaveInterface
import com.tokopedia.weaver.Weaver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.*
import org.jetbrains.annotations.NotNull
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


/**
 * Created by Ade Fulki on 2020-02-08.
 * ade.hadian@tokopedia.com
 */

class OnboardingFragment : BaseDaggerFragment(), CoroutineScope, IOnBackPressed {

    private val job = SupervisorJob()

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var onboardingAnalytics: OnboardingAnalytics

    @Inject
    lateinit var remoteConfig: RemoteConfig

    @Inject
    lateinit var dispatcher: CoroutineDispatchers


    override val coroutineContext: CoroutineContext
        get() = job + dispatcher.main

    private val binding: FragmentOnboardingBinding? by viewBinding()

    private lateinit var onboardingViewPagerAdapter: OnboardingViewPagerAdapter
    private lateinit var sharedPrefs: SharedPreferences

    override fun getScreenName(): String = OnboardingAnalytics.SCREEN_ONBOARDING

    override fun initInjector() = getComponent(OnboardingComponent::class.java).inject(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_onboarding, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val executeViewCreatedWeave = object : WeaveInterface {
            override fun execute(): Any {
                return executeViewCreateFlow()
            }
        }
        Weaver.executeWeaveCoRoutineWithFirebase(executeViewCreatedWeave, RemoteConfigKey.ENABLE_ASYNC_ONBOARDING_CREATE, context, true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_REGISTER -> {
                activity?.let {
                    val intentNewUser = RouteManager.getIntent(context, ApplinkConst.DISCOVERY_NEW_USER)
                    val intentHome = RouteManager.getIntent(activity, ApplinkConst.HOME)
                    intentHome.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                    if (resultCode == Activity.RESULT_OK && userSession.isLoggedIn && data != null) {
                        val isSuccessRegister = data.getBooleanExtra(ApplinkConstInternalGlobal.PARAM_IS_SUCCESS_REGISTER, false)
                        if (isSuccessRegister) {
                            it.startActivities(arrayOf(intentHome, intentNewUser))
                        } else {
                            it.startActivity(intentHome)
                        }
                    } else {
                        it.startActivity(intentHome)
                    }
                    it.finish()
                }
            }

            else -> {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    @NotNull
    private fun executeViewCreateFlow(): Boolean {
        GlobalScope.launch(coroutineContext) {
            trackPreinstall()
            initView()
        }
        return true
    }

    private fun initView() {
        context?.let {
            val listItem = generateListAllButton()

            binding?.let { binding ->
                onboardingViewPagerAdapter = OnboardingViewPagerAdapter(it, listItem)
                binding.screenViewpager.apply {
                    adapter = onboardingViewPagerAdapter
                    if (onboardingViewPagerAdapter.count > 1) {
                        binding.screenViewpager.offscreenPageLimit =
                            onboardingViewPagerAdapter.count - 1
                    }
                    addOnPageChangeListener(OnPageChangeListener())
                }

                binding.tabIndicator.setupWithViewPager(binding.screenViewpager)

                binding.skipAction.setOnClickListener(skipActionClickListener())
                binding.nextAction.setOnClickListener(nextActionClickListener())
                binding.joinButton.setOnClickListener(joinActionClickListener())
            }

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
                binding?.let {
                    onboardingAnalytics.eventOnboardingJoin(it.screenViewpager.currentItem)
                }
                if (TextUtils.isEmpty(TrackApp.getInstance().appsFlyer.defferedDeeplinkPathIfExists)) {
                    goToRegisterPage()
                } else {
                    RouteManager.route(it, TrackApp.getInstance().appsFlyer.defferedDeeplinkPathIfExists)
                }
            }
        }
    }

    private fun skipActionClickListener(): View.OnClickListener {
        return View.OnClickListener {
            context?.let {
                binding?.let {
                    onboardingAnalytics.eventOnboardingSkip(it.screenViewpager.currentItem)
                }
                val applink = if (TextUtils.isEmpty(TrackApp.getInstance().appsFlyer.defferedDeeplinkPathIfExists)) {
                    ApplinkConst.HOME
                } else {
                    TrackApp.getInstance().appsFlyer.defferedDeeplinkPathIfExists
                }

                launchCatchError(
                        block = {
                            finishOnBoarding()
                            val intent = getIntentforApplink(it, applink)
                            startActivity(intent)
                            activity?.finish()
                        },
                        onError = {
                        }
                )
            }
        }
    }

    private fun nextActionClickListener(): View.OnClickListener {
        return View.OnClickListener {
            binding?.let { binding ->
                var position = binding.screenViewpager.currentItem
                onboardingAnalytics.eventOnboardingNext(position)
                val size = onboardingViewPagerAdapter.listScreen.size
                if (position < size) {
                    position++
                    binding.screenViewpager.currentItem = position
                }
            }
        }
    }

    private fun goToRegisterPage() {
        context?.let {
            launchCatchError(
                    block = {
                        finishOnBoarding()
                        val intent = getIntentforApplink(it, ApplinkConst.REGISTER)
                        startActivityForResult(intent, REQUEST_REGISTER)
                    },
                    onError = {
                    }
            )
        }
    }

    private fun hideNextAction() {
        binding?.nextAction?.invisible()
    }

    private fun showNextAction() {
        binding?.nextAction?.show()
    }

    private fun loadLastScreen() {
        hideNextAction()
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
                    KEY_FIRST_INSTALL_SEARCH, Context.MODE_PRIVATE)
            sharedPrefs.edit().putLong(
                    KEY_FIRST_INSTALL_TIME_SEARCH, date.time).apply()
        }
    }

    /**
     * Get Intent for Applink using suspend function
     */
    private suspend fun getIntentforApplink(context: Context, applink: String): Intent = withContext(Dispatchers.IO){
        return@withContext RouteManager.getIntent(context, applink)
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
        const val ONBOARD_IMAGE_PAGE_1_URL = "https://images.tokopedia.net/android/others/onboarding_image_page_1.png"
        const val ONBOARD_IMAGE_PAGE_2_URL = "https://images.tokopedia.net/android/others/onboarding_image_page_2.png"
        const val ONBOARD_IMAGE_PAGE_3_URL = "https://images.tokopedia.net/android/others/onboarding_image_page_3.png"

        const val KEY_FIRST_INSTALL_SEARCH = "KEY_FIRST_INSTALL_SEARCH"
        const val KEY_FIRST_INSTALL_TIME_SEARCH = "KEY_IS_FIRST_INSTALL_TIME_SEARCH"

        private const val REQUEST_REGISTER = 779

        fun createInstance(bundle: Bundle): OnboardingFragment {
            val fragment = OnboardingFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
