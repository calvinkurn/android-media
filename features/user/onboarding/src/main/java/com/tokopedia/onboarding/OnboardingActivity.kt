package com.tokopedia.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.TaskStackBuilder
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.onboarding.adapter.OnboardingPagerAdapter
import com.tokopedia.onboarding.analytics.OnboardingAnalytics
import com.tokopedia.onboarding.di.DaggerOnboardingComponent
import com.tokopedia.onboarding.fragment.OnboardingFragment
import com.tokopedia.onboarding.listener.OnboardingVideoListener
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by stevenfredian on 14/05/19.
 * For navigate: use ApplinkConstInternalMarketplace.ONBOARDING
 */

class OnboardingActivity : BaseActivity() {

    var viewPager: ViewPager? = null
    var indicator: ViewGroup? = null
    var pagerAdapter: OnboardingPagerAdapter? = null

    @Inject
    lateinit var remoteConfig: RemoteConfig

    @Inject
    lateinit var analytics: OnboardingAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    private lateinit var slideCallBackList: MutableList<onBoardingFirsbaseCallBack>

    interface onBoardingFirsbaseCallBack {
        fun onResponse(remoteConfig: RemoteConfig)
    }

    private val indicatorNormal: Int = R.drawable.indicator_onboarding_unfocused
    private val indicatorFocused: Int = R.drawable.indicator_onboarding_focused

    lateinit var loginButton: ButtonCompat
    lateinit var registerButton: ButtonCompat
    lateinit var skipButton: TextView
    var currentPosition = 0

    private var indicatorItems = java.util.ArrayList<ImageView>()

    private var lastFragment: OnboardingVideoListener? = null

    companion object {
        fun createIntent(context: Context) = Intent(context, OnboardingActivity::class.java)
    }

    override fun onStart() {
        super.onStart()
        analytics.sendScreen(currentPosition)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_onboarding_activity)
        slideCallBackList = ArrayList()
        initInjector()
        initView()
        trackPreinstall()
    }

    private fun trackPreinstall() {
        if (GlobalConfig.IS_PREINSTALL) {
            analytics.trackMoengage()
        }
    }

    private fun initInjector() {
        val onboardingComponent = DaggerOnboardingComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()

        onboardingComponent.inject(this)
    }

    private fun initView() {
        viewPager = findViewById(R.id.pager_onboarding)
        loginButton = findViewById(R.id.btnLogin)
        registerButton = findViewById(R.id.btnRegister)
        skipButton = findViewById(R.id.skip)

        val fragmentList = addFragments()

        viewPager?.offscreenPageLimit = 2
        pagerAdapter = OnboardingPagerAdapter(supportFragmentManager, fragmentList)
        viewPager?.adapter = pagerAdapter

        addIndicator(fragmentList.size)
        setListener()
    }

    private fun onFragmentSelected(position: Int) {
        pagerAdapter?.let {
            val fragment = it.getItem(position)
            if (fragment is OnboardingVideoListener) {
                fragment.onPageSelected(position)
                lastFragment = fragment
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lastFragment?.onPageSelected(currentPosition)
    }

    private val pageChangeListener: ViewPager.OnPageChangeListener = (object: ViewPager.OnPageChangeListener {

        var first: Boolean = true
        override fun onPageScrollStateChanged(state: Int) {}

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            if (first && positionOffset == 0f && positionOffsetPixels == 0){
                onPageSelected(0)
                first = false
            }
        }

        override fun onPageSelected(position: Int) {
            lastFragment?.onPageUnSelected()

            onFragmentSelected(position)
            setIndicator(position)
            currentPosition = position
            analytics.sendScreen(position)
        }
    })

    private fun setListener() {
        viewPager?.addOnPageChangeListener(pageChangeListener)

        loginButton.setOnClickListener {
            analytics.trackClickLogin(currentPosition)
            startActivityWithBackTask(ApplinkConst.LOGIN)
        }

        registerButton.setOnClickListener {
            analytics.trackClickRegister(currentPosition)
            startActivityWithBackTask(ApplinkConst.REGISTER)
        }

        skipButton.setOnClickListener {
            analytics.eventOnboardingSkip(applicationContext, currentPosition)
            finishOnBoarding()
            RouteManager.route(this, ApplinkConst.HOME)
        }
    }

    private fun startActivityWithBackTask(applink: String) {
        finishOnBoarding()
        val taskStackBuilder = TaskStackBuilder.create(this)
        val homeIntent = RouteManager.getIntent(this, ApplinkConst.HOME)
        taskStackBuilder.addNextIntent(homeIntent)
        val registerIntent = RouteManager.getIntent(this, applink)
        taskStackBuilder.addNextIntent(registerIntent)
        taskStackBuilder.startActivities()
    }

    private fun finishOnBoarding() {
        userSession.setFirstTimeUserOnboarding(false)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishOnBoarding()
    }

    private fun setIndicator(position: Int) {
        for (i in indicatorItems.indices) {
            if (position != i) {
                indicatorItems[i].setImageResource(indicatorNormal)
            } else {
                indicatorItems[i].setImageResource(indicatorFocused)
            }
        }
    }

    private fun addIndicator(size: Int) {
        indicator = findViewById(R.id.indicator_container)

        for (count in 0..size) {
            val pointView = ImageView(this)
            pointView.setPadding(5, 0, 5, 0)
            if (count == 0) {
                pointView.setImageResource(indicatorFocused)
            } else {
                pointView.setImageResource(indicatorNormal)
            }
            indicatorItems.add(pointView)
            indicator?.addView(pointView)
        }
    }

    private fun addFragments(): ArrayList<Fragment> {
        fetchFromRemoteConfig()
        val fragmentList = ArrayList<Fragment>()
        //#1
        fragmentList.add(createAndAddSlide(
                getMessageFromRemoteConfig(RemoteConfigKey.NONB1_TTL, R.string.nonb_1_title),
                getMessageFromRemoteConfig(RemoteConfigKey.NONB1_DESC, R.string.nonb_1_desc),
                0,
                RemoteConfigKey.NONB1_TTL,
                RemoteConfigKey.NONB1_DESC,
                getStringVideoPath(R.raw.onboard_1)
        ))

        //#2
        fragmentList.add(createAndAddSlide(
                getMessageFromRemoteConfig(RemoteConfigKey.NONB2_TTL, R.string.nonb_2_title),
                getMessageFromRemoteConfig(RemoteConfigKey.NONB2_DESC, R.string.nonb_2_desc),
                1,
                RemoteConfigKey.NONB2_TTL,
                RemoteConfigKey.NONB2_DESC,
                getStringVideoPath(R.raw.onboard_2)
        ))

        //#3
        fragmentList.add(createAndAddSlide(
                getMessageFromRemoteConfig(RemoteConfigKey.NONB3_TTL, R.string.nonb_3_title),
                getMessageFromRemoteConfig(RemoteConfigKey.NONB3_DESC, R.string.nonb_3_desc),
                2,
                RemoteConfigKey.NONB3_TTL,
                RemoteConfigKey.NONB3_DESC,
                getStringVideoPath(R.raw.onboard_3)
        ))

        //#4
        fragmentList.add(createAndAddSlide(
                getMessageFromRemoteConfig(RemoteConfigKey.NONB4_TTL, R.string.nonb_4_title),
                getMessageFromRemoteConfig(RemoteConfigKey.NONB4_DESC, R.string.nonb_4_desc),
                3,
                RemoteConfigKey.NONB4_TTL,
                RemoteConfigKey.NONB4_DESC,
                getStringVideoPath(R.raw.onboard_4)
        ))

        //#5
        fragmentList.add(createAndAddSlide(
                getMessageFromRemoteConfig(RemoteConfigKey.NONB5_TTL, R.string.nonb_5_title),
                getMessageFromRemoteConfig(RemoteConfigKey.NONB5_DESC, R.string.nonb_5_desc),
                4,
                RemoteConfigKey.NONB5_TTL,
                RemoteConfigKey.NONB5_DESC,
                getStringVideoPath(R.raw.onboard_5)
        ))
        return fragmentList
    }

    private fun getStringVideoPath(rawResource: Int): String {
        return "android.resource://$packageName/$rawResource"
    }

    private fun getMessageFromRemoteConfig(firebaseKey: String, defaultMessageResId: Int): String {
        var msg = remoteConfig.getString(firebaseKey)
        if (TextUtils.isEmpty(msg)) {
            msg = getString(defaultMessageResId)
        }
        return msg
    }

    private fun fetchFromRemoteConfig() {
        remoteConfig = FirebaseRemoteConfigImpl(this)
        remoteConfig.fetch(object : RemoteConfig.Listener {
            override fun onComplete(rc: RemoteConfig?) {
                if (slideCallBackList.isNotEmpty()) {
                    for (slideCallback in slideCallBackList) {
                        slideCallback.onResponse(remoteConfig)
                    }
                }
            }

            override fun onError(e: Exception) {

            }
        })
    }

    private fun createAndAddSlide(title: String, description: String,
                                  position: Int,
                                  ttlKey: String,
                                  descKey: String,
                                  videoPath: String):OnboardingFragment {
        val slide = OnboardingFragment.createInstance(title,
                description,
                videoPath,
                position,
                ttlKey,
                descKey)
        slideCallBackList.add(slide)
        return slide
    }

}