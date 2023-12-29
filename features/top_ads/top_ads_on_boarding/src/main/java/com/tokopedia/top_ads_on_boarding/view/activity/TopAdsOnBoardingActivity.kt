package com.tokopedia.top_ads_on_boarding.view.activity

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.top_ads_on_boarding.R
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant.EMPTY_TEXT
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant.STEPPER_ONE
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant.TAG_STEPPER_FRAGMENT
import com.tokopedia.top_ads_on_boarding.di.DaggerTopAdsOnBoardingComponent
import com.tokopedia.top_ads_on_boarding.di.TopAdsOnBoardingComponent
import com.tokopedia.top_ads_on_boarding.view.fragment.AdsObjectiveFragment
import com.tokopedia.top_ads_on_boarding.view.fragment.AdsTypeFragment
import com.tokopedia.top_ads_on_boarding.view.fragment.StartPageFragment
import java.util.*

class TopAdsOnBoardingActivity : BaseSimpleActivity(),
    HasComponent<TopAdsOnBoardingComponent>, FragmentManager.OnBackStackChangedListener {

    private var stepperOne: View? = null
    private var stepperTwo: View? = null
    private var stepperThree: View? = null

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle(EMPTY_TEXT)
        initViews()

        supportFragmentManager.beginTransaction()
            .add(R.id.onBoardingFragmentContainer, StartPageFragment.newInstance(), TAG_STEPPER_FRAGMENT)
            .addToBackStack(STEPPER_ONE)
            .commit()
        supportFragmentManager.addOnBackStackChangedListener(this)
    }

    private fun initViews() {
        stepperOne = findViewById(R.id.stepperOne)
        stepperTwo = findViewById(R.id.stepperTwo)
        stepperThree = findViewById(R.id.stepperThree)
    }

    override fun getLayoutRes(): Int {
        return R.layout.top_ads_onboarding_layout
    }

    override fun getToolbarResourceID(): Int {
        return R.id.onBoardingToolabar;

    }

    fun goToWebView(url: String) {
        RouteManager.route(
            this,
            String.format(Locale.getDefault(), getString(R.string.topads_onbaording_url_format), ApplinkConst.WEBVIEW, url)
        )
    }

    override fun onBackStackChanged() {
        val fragments = supportFragmentManager.fragments
        if (fragments.isEmpty()) {
            finish()
            return
        }
        when (fragments[0]) {
            is StartPageFragment -> {
                stepperOne?.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                )
                stepperTwo?.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN50
                    )
                )
                stepperThree?.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN50
                    )
                )
            }
            is AdsObjectiveFragment -> {
                stepperOne?.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                )
                stepperTwo?.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                )
                stepperThree?.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN50
                    )
                )
            }

            is AdsTypeFragment -> {
                stepperOne?.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                )
                stepperTwo?.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                )
                stepperThree?.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                )

            }
        }
    }

    override fun getComponent(): TopAdsOnBoardingComponent =
        DaggerTopAdsOnBoardingComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent
        ).build()

}


