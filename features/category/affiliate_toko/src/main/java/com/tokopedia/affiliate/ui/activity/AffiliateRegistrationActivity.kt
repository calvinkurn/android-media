package com.tokopedia.affiliate.ui.activity


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.FrameLayout
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.affiliate.AFFILIATE_SPLASH_TIME
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.ui.fragment.registration.AffiliateLoginFragment
import com.tokopedia.affiliate.ui.fragment.registration.AffiliatePortfolioFragment
import com.tokopedia.affiliate.ui.fragment.registration.AffiliateTermsAndConditionFragment
import com.tokopedia.affiliate.viewmodel.AffiliateRegistrationSharedViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.applink.RouteManager
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliateRegistrationActivity: BaseViewModelActivity<AffiliateRegistrationSharedViewModel>() {

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    lateinit var affiliateRegistrationSharedViewModel: AffiliateRegistrationSharedViewModel

    override fun getViewModelType(): Class<AffiliateRegistrationSharedViewModel> {
        return AffiliateRegistrationSharedViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateRegistrationSharedViewModel = viewModel as AffiliateRegistrationSharedViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView(savedInstanceState == null)
    }

    private fun setupView(isSavedInstanceStateNull: Boolean) {
        initObserver()
        if(isSavedInstanceStateNull) initFragment()
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction().add(R.id.parent_view,AffiliateLoginFragment.getFragmentInstance(),AffiliateLoginFragment.TAG).commit()
    }


    private fun initObserver() {
        affiliateRegistrationSharedViewModel.getUserAction().observe(this,
             {
            when(it){
                AffiliateRegistrationSharedViewModel.UserAction.NaigateToPortFolio -> {
                    openFragment(AffiliatePortfolioFragment.getFragmentInstance(),AffiliatePortfolioFragment.TAG)
                }
                AffiliateRegistrationSharedViewModel.UserAction.NaigateToTermsAndFragment -> {
                    openFragment(AffiliateTermsAndConditionFragment.getFragmentInstance(),AffiliateTermsAndConditionFragment.TAG)
                }
                AffiliateRegistrationSharedViewModel.UserAction.RegistrationSucces -> {
                    showSplashScreen()
                }
                else -> {}
            }
        })
    }

    var showingSplashScreen = false
    private fun showSplashScreen() {
        showingSplashScreen = true
        findViewById<FrameLayout>(R.id.parent_view)?.hide()
        findViewById<Typography>(R.id.splash_title).text =
            getString(R.string.affiliate_hai_ana_selamat_bergabung_di_tokopedia_affiliate).replace(
                "{name}",
                userSessionInterface.name
            )
        findViewById<Group>(R.id.splash_group)?.show()
        splashHandler = Handler(Looper.getMainLooper())
        splashHandler?.postDelayed(splashRunnable, AFFILIATE_SPLASH_TIME)
    }

    var splashHandler: Handler? = null

    private val splashRunnable = Runnable {
        findViewById<Group>(R.id.splash_group)?.hide()
        openAffiliate()
    }

    override fun onDestroy() {
        splashHandler?.removeCallbacks(splashRunnable)
        super.onDestroy()
    }

    private fun openAffiliate() {
        RouteManager.route(this,"tokopedia://affiliate")
        finish()
    }

    private fun openFragment(fragment: Fragment,tag: String) {
        supportFragmentManager.beginTransaction().replace(R.id.parent_view,fragment).addToBackStack(
            tag
        ).commit()
    }


    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun initInject() {
        getComponent().injectRegistrationActivity(this)
    }

    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    private fun getComponent(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()

    companion object{
        fun newInstance(context: Context){
            context.startActivity(Intent(context,AffiliateRegistrationActivity::class.java))
        }
    }

    override fun onBackPressed() {
        if(!showingSplashScreen) super.onBackPressed()
    }

    override fun getLayoutRes(): Int = R.layout.affiliate_registration_layout
}