package com.tokopedia.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.TaskStackBuilder
import android.support.v4.content.ContextCompat
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
import com.tokopedia.onboarding.listener.CustomAnimationPageTransformer
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by stevenfredian on 14/05/19.
 * For navigate: use ApplinkConstInternalMarketplace.ONBOARDING
 */

class OnboardingActivity : BaseActivity() {

    lateinit var viewPager: ViewPager
    lateinit var indicator: ViewGroup
    private lateinit var pagerAdapter: OnboardingPagerAdapter

    @Inject
    lateinit var remoteConfig: RemoteConfig

    @Inject
    lateinit var analytics: OnboardingAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    val indicatorNormal: Int = R.drawable.indicator_onboarding_unfocused
    val indicatorFocused: Int = R.drawable.indicator_onboarding_focused

    lateinit var loginButton: ButtonCompat
    lateinit var registerButton: ButtonCompat
    lateinit var skipButton: TextView
    var currentPosition = 1


    protected var indicatorItems = java.util.ArrayList<ImageView>()

    companion object {
        fun createIntent(context: Context) = Intent(context, OnboardingActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_onboarding_activity)
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

        viewPager.setPageTransformer(false, CustomAnimationPageTransformer())
        viewPager.offscreenPageLimit = 1
        pagerAdapter = OnboardingPagerAdapter(supportFragmentManager, fragmentList)
        viewPager.adapter = pagerAdapter

        addIndicator(fragmentList)
        setListener()

    }

    private fun setListener() {
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                setIndicator(position)
                currentPosition = position
                analytics.sendScreen(position)

            }
        })

        loginButton.setOnClickListener {
            analytics.trackClickLogin(currentPosition)
            startActivityWithBackTask(ApplinkConst.LOGIN)
        }

        registerButton.setOnClickListener {
            analytics.trackClickRegister(currentPosition)
            startActivityWithBackTask(ApplinkConst.REGISTER)
        }

        skipButton.setOnClickListener {
            analytics.eventOnboardingSkip(applicationContext, viewPager.currentItem)
            finishOnboarding()
            RouteManager.route(this, ApplinkConst.HOME)
        }
    }

    private fun startActivityWithBackTask(applink: String) {
        finishOnboarding()
        val taskStackBuilder = TaskStackBuilder.create(this)
        val homeIntent = RouteManager.getIntent(this, ApplinkConst.HOME)
        taskStackBuilder.addNextIntent(homeIntent)
        val registerIntent = RouteManager.getIntent(this, applink)
        taskStackBuilder.addNextIntent(registerIntent)
        taskStackBuilder.startActivities()
    }

    private fun finishOnboarding() {
        userSession.setFirstTimeUserOnboarding(false)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishOnboarding()
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

    private fun addIndicator(fragmentList: ArrayList<Fragment>) {
        indicator = findViewById(R.id.indicator_container)

        for (count in fragmentList.indices) {
            val pointView = ImageView(this)
            pointView.setPadding(5, 0, 5, 0)
            if (count == 0) {
                pointView.setImageResource(indicatorFocused)
            } else {
                pointView.setImageResource(indicatorNormal)
            }
            indicatorItems.add(pointView)
            indicator.addView(pointView)
        }
    }

    private fun addFragments(): ArrayList<Fragment> {
        val fragmentList = ArrayList<Fragment>()
        //#1
        fragmentList.add(OnboardingFragment.createInstance(
                getMessageFromRemoteConfig(RemoteConfigKey.NONB1_TTL, R.string.nonb_1_title),
                getMessageFromRemoteConfig(RemoteConfigKey.NONB1_DESC, R.string.nonb_1_desc),
                "onboard1.json",
                ContextCompat.getColor(applicationContext, R.color.green_nob),
                0
        ))

        //#2
        fragmentList.add(OnboardingFragment.createInstance(
                getMessageFromRemoteConfig(RemoteConfigKey.NONB2_TTL, R.string.nonb_2_title),
                getMessageFromRemoteConfig(RemoteConfigKey.NONB2_DESC, R.string.nonb_2_desc),
                "onboard2.json",
                ContextCompat.getColor(applicationContext, R.color.blue_nob),
                1
        ))

        //#3
        fragmentList.add(OnboardingFragment.createInstance(
                getMessageFromRemoteConfig(RemoteConfigKey.NONB3_TTL, R.string.nonb_3_title),
                getMessageFromRemoteConfig(RemoteConfigKey.NONB3_DESC, R.string.nonb_3_desc),
                "onboard3.json",
                ContextCompat.getColor(applicationContext, R.color.orange_nob),
                2
        ))

        //#4
        fragmentList.add(OnboardingFragment.createInstance(
                getMessageFromRemoteConfig(RemoteConfigKey.NONB4_TTL, R.string.nonb_4_title),
                getMessageFromRemoteConfig(RemoteConfigKey.NONB4_DESC, R.string.nonb_4_desc),
                "onboard4.json",
                ContextCompat.getColor(applicationContext, R.color.green_nob),
                3
        ))

        //#5
        fragmentList.add(OnboardingFragment.createInstance(
                getMessageFromRemoteConfig(RemoteConfigKey.NONB5_TTL, R.string.nonb_5_title),
                getMessageFromRemoteConfig(RemoteConfigKey.NONB5_DESC, R.string.nonb_5_desc),
                "onboard5.json",
                ContextCompat.getColor(applicationContext, R.color.blue_nob),
                4
        ))
        return fragmentList
    }

    private fun getMessageFromRemoteConfig(firebaseKey: String, defaultMessageResId: Int): String {
        var msg = remoteConfig.getString(firebaseKey)
        if (TextUtils.isEmpty(msg)) {
            msg = getString(defaultMessageResId)
        }
        return msg
    }

}