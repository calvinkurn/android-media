package com.tokopedia.top_ads_on_boarding.view.activity

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.top_ads_on_boarding.R
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant.EMPTY_TEXT
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant.STEPPER_ONE
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant.TAG_STEPPER_FRAGMENT
import com.tokopedia.top_ads_on_boarding.di.DaggerTopAdsOnBoardingComponent
import com.tokopedia.top_ads_on_boarding.di.TopAdsOnBoardingComponent
import com.tokopedia.top_ads_on_boarding.view.fragment.AdsObjectiveFragment
import com.tokopedia.top_ads_on_boarding.view.fragment.AdsTypeFragment
import com.tokopedia.top_ads_on_boarding.view.fragment.AutoPsOnboardingFragment
import com.tokopedia.top_ads_on_boarding.view.fragment.StartPageFragment
import com.tokopedia.top_ads_on_boarding.view.viewmodel.TopAdsOnBoardingViewModel
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.AUTOPS_EXPERIMENT
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.AUTOPS_VARIANT
import java.util.Locale
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class TopAdsOnBoardingActivity : BaseSimpleActivity(),
    HasComponent<TopAdsOnBoardingComponent>, FragmentManager.OnBackStackChangedListener {

    private var loader: View? = null
    private var stepperOne: View? = null
    private var stepperTwo: View? = null
    private var stepperThree: View? = null

    @JvmField
    @Inject
    var factory: ViewModelProvider.Factory? = null

    private val onBoardingViewModel by lazy {
        if (factory == null) {
            null
        } else {
            ViewModelProvider(this, factory!!).get(TopAdsOnBoardingViewModel::class.java)
        }
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        initViews()
        onBoardingViewModel?.getVariantById()
        setupObservers()
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun setupObservers(){
        onBoardingViewModel?.shopVariant?.observe(this){ shopVariants ->
            if(shopVariants.isNotEmpty() && shopVariants.filter { it.experiment == AUTOPS_EXPERIMENT && it.variant == AUTOPS_VARIANT}.isNotEmpty()){
                showAutoPsOnboarding()
            } else {
                showTopAdsOnBoarding()
            }
            loader?.gone()
        }
    }

    private fun showAutoPsOnboarding(){
        updateTitle(getString(R.string.topads_onboarding_title))
        stepperOne?.gone()
        stepperTwo?.gone()
        stepperThree?.gone()
        supportFragmentManager.beginTransaction()
            .add(R.id.onBoardingFragmentContainer, AutoPsOnboardingFragment.newInstance(), TAG_STEPPER_FRAGMENT)
            .commit()
        supportFragmentManager.addOnBackStackChangedListener(this)
    }

    private fun showTopAdsOnBoarding(){
        updateTitle(EMPTY_TEXT)
        stepperOne?.show()
        stepperTwo?.show()
        stepperThree?.show()
        supportFragmentManager.beginTransaction()
            .add(R.id.onBoardingFragmentContainer, StartPageFragment.newInstance(), TAG_STEPPER_FRAGMENT)
            .addToBackStack(STEPPER_ONE)
            .commit()
        supportFragmentManager.addOnBackStackChangedListener(this)
    }

    private fun initViews() {
        loader = findViewById(R.id.loader)
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
                        unifyprinciplesR.color.Unify_GN500
                    )
                )
                stepperTwo?.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        unifyprinciplesR.color.Unify_NN50
                    )
                )
                stepperThree?.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        unifyprinciplesR.color.Unify_NN50
                    )
                )
            }
            is AdsObjectiveFragment -> {
                stepperOne?.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        unifyprinciplesR.color.Unify_GN500
                    )
                )
                stepperTwo?.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        unifyprinciplesR.color.Unify_GN500
                    )
                )
                stepperThree?.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        unifyprinciplesR.color.Unify_NN50
                    )
                )
            }

            is AdsTypeFragment -> {
                stepperOne?.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        unifyprinciplesR.color.Unify_GN500
                    )
                )
                stepperTwo?.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        unifyprinciplesR.color.Unify_GN500
                    )
                )
                stepperThree?.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        unifyprinciplesR.color.Unify_GN500
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


