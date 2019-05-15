package com.tokopedia.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.text.TextUtils
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.config.GlobalConfig
import com.tokopedia.onboarding.adapter.OnboardingPagerAdapter
import com.tokopedia.onboarding.analytics.OnboardingAnalytics
import com.tokopedia.onboarding.fragment.OnboardingFragment
import com.tokopedia.onboarding.listener.CustomAnimationPageTransformer
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.track.TrackApp
import javax.inject.Inject

/**
 * @author by stevenfredian on 14/05/19.
 */

class OnboardingActivity : BaseSimpleActivity() {

    lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: OnboardingPagerAdapter

    @Inject
    private lateinit var remoteConfig: RemoteConfig

    @Inject
    private lateinit var analytics: OnboardingAnalytics

    companion object {
        fun createIntent(context: Context) = Intent(context, OnboardingActivity::class.java)
    }

    override fun onStart() {
        super.onStart()
        //TODO
//        analytics.sendScreen(this, screenName)
    }

    override fun getLayoutRes(): Int {
        return R.layout.base_onboarding_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initInjector()
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
        val fragmentList = addFragments()

        viewPager.setPageTransformer(false, CustomAnimationPageTransformer())
        viewPager.offscreenPageLimit = 1
        pagerAdapter = OnboardingPagerAdapter(supportFragmentManager, fragmentList)
        viewPager.adapter = pagerAdapter
        viewPager.currentItem = 1
    }

    private fun addFragments(): ArrayList<Fragment> {
        val fragmentList = ArrayList<Fragment>()
        //#1
        fragmentList.add(OnboardingFragment.createInstance(
                getMessageFromRemoteConfig(RemoteConfigKey.NONB1_TTL, R.string.nonb_1_title),
                getMessageFromRemoteConfig(RemoteConfigKey.NONB1_DESC, R.string.nonb_1_desc),
                "onboarding1.json",
                ContextCompat.getColor(applicationContext, R.color.green_nob),
                0
        ))

        //#2
        fragmentList.add(OnboardingFragment.createInstance(
                getMessageFromRemoteConfig(RemoteConfigKey.NONB2_TTL, R.string.nonb_2_title),
                getMessageFromRemoteConfig(RemoteConfigKey.NONB2_DESC, R.string.nonb_2_desc),
                "onboarding2.json",
                ContextCompat.getColor(applicationContext, R.color.blue_nob),
                1
        ))

        //#3
        fragmentList.add(OnboardingFragment.createInstance(
                getMessageFromRemoteConfig(RemoteConfigKey.NONB3_TTL, R.string.nonb_3_title),
                getMessageFromRemoteConfig(RemoteConfigKey.NONB3_DESC, R.string.nonb_3_desc),
                "onboarding3.json",
                ContextCompat.getColor(applicationContext, R.color.orange_nob),
                2
        ))

        //#4
        fragmentList.add(OnboardingFragment.createInstance(
                getMessageFromRemoteConfig(RemoteConfigKey.NONB4_TTL, R.string.nonb_4_title),
                getMessageFromRemoteConfig(RemoteConfigKey.NONB4_DESC, R.string.nonb_4_desc),
                "onboarding4.json",
                ContextCompat.getColor(applicationContext, R.color.green_nob),
                3
        ))

        //#5
        fragmentList.add(OnboardingFragment.createInstance(
                getMessageFromRemoteConfig(RemoteConfigKey.NONB5_TTL, R.string.nonb_5_title),
                getMessageFromRemoteConfig(RemoteConfigKey.NONB5_DESC, R.string.nonb_5_desc),
                "onboarding5.json",
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

    //Not used
    override fun getNewFragment(): Fragment {
        return Fragment()
    }
}