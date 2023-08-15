package com.tokopedia.affiliate.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.affiliate.AFFILIATE_APP_LINK
import com.tokopedia.affiliate.AFFILIATE_SPLASH_TIME
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.ui.fragment.registration.AffiliateLoginFragment
import com.tokopedia.affiliate.ui.fragment.registration.AffiliatePortfolioFragment
import com.tokopedia.affiliate.ui.fragment.registration.AffiliateTermsAndConditionFragment
import com.tokopedia.affiliate.viewmodel.AffiliateRegistrationSharedViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.affiliate_toko.databinding.AffiliateRegistrationLayoutBinding
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliateRegistrationActivity :
    BaseSimpleActivity(),
    HasComponent<AffiliateComponent> {
    private val affiliateComponent: AffiliateComponent by lazy(LazyThreadSafetyMode.NONE) { initInject() }

    @Inject
    @JvmField
    var userSessionInterface: UserSessionInterface? = null

    @Inject
    @JvmField
    var viewModelProvider: ViewModelProvider.Factory? = null

    private var affiliateRegistrationSharedViewModel: AffiliateRegistrationSharedViewModel? = null

    private var binding: AffiliateRegistrationLayoutBinding? = null

    private var productId: String? = null
    private var source: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.injectRegistrationActivity(this)
        binding = AffiliateRegistrationLayoutBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        affiliateRegistrationSharedViewModel =
            viewModelProvider?.let { ViewModelProvider(this, it) }
                ?.get(AffiliateRegistrationSharedViewModel::class.java)

        val queryData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(QUERY_PARAM, Pair::class.java)
        } else {
            intent.getSerializableExtra(QUERY_PARAM) as? Pair<*, *>
        }
        when (queryData?.first) {
            PRODUCT_ID_KEY -> productId = queryData.second.toString()
            SOURCE_KEY -> source = queryData.second.toString()
        }
        setupView(savedInstanceState == null)
    }

    private fun setupView(isSavedInstanceStateNull: Boolean) {
        initObserver()
        if (isSavedInstanceStateNull) initFragment()
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction().add(
            R.id.parent_view,
            AffiliateLoginFragment.getFragmentInstance(),
            AffiliateLoginFragment.TAG
        ).commit()
    }

    private fun initObserver() {
        affiliateRegistrationSharedViewModel?.getUserAction()?.observe(this) {
            when (it) {
                AffiliateRegistrationSharedViewModel.UserAction.NaigateToPortFolio -> {
                    openFragment(
                        AffiliatePortfolioFragment.getFragmentInstance(),
                        AffiliatePortfolioFragment.TAG
                    )
                }

                AffiliateRegistrationSharedViewModel.UserAction.NaigateToTermsAndFragment -> {
                    openFragment(
                        AffiliateTermsAndConditionFragment.getFragmentInstance(),
                        AffiliateTermsAndConditionFragment.TAG
                    )
                }

                AffiliateRegistrationSharedViewModel.UserAction.RegistrationSucces -> {
                    showSplashScreen()
                }

                else -> {}
            }
        }
    }

    private var showingSplashScreen = false
    private fun showSplashScreen() {
        showingSplashScreen = true
        binding?.parentView?.hide()
        binding?.splashTitle?.text =
            getString(R.string.affiliate_hai_ana_selamat_bergabung_di_tokopedia_affiliate).replace(
                "{name}",
                userSessionInterface?.name.orEmpty()
            )
        binding?.splashGroup?.show()
        if (source == SOURCE_WISHLIST) {
            binding?.actionContainer?.show()
            binding?.backToWishlist?.apply {
                setOnClickListener {
                    finish()
                }
            }
            binding?.goToPromote?.apply {
                setOnClickListener {
                    openAffiliate()
                }
            }
        } else {
            splashHandler = Handler(Looper.getMainLooper())
            splashHandler?.postDelayed(splashRunnable, AFFILIATE_SPLASH_TIME)
        }
    }

    private var splashHandler: Handler? = null

    private val splashRunnable = Runnable {
        binding?.splashGroup?.hide()
        if (productId == null) {
            openAffiliate()
        } else if (productId.isNullOrBlank()) {
            finish()
        } else {
            openPdp()
        }
    }

    override fun onDestroy() {
        splashHandler?.removeCallbacks(splashRunnable)
        super.onDestroy()
    }

    private fun openAffiliate() {
        RouteManager.route(this, AFFILIATE_APP_LINK)
        finish()
    }

    private fun openPdp() {
        RouteManager.route(this, "tokopedia://product/$productId")
        finish()
    }

    private fun openFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction().add(R.id.parent_view, fragment).addToBackStack(
            tag
        ).commit()
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun getComponent(): AffiliateComponent = affiliateComponent

    private fun initInject() =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()

    companion object {
        private const val QUERY_PARAM = "query_param"
        private const val PRODUCT_ID_KEY = "productId"
        private const val SOURCE_KEY = "source"
        private const val SOURCE_WISHLIST = "wishlist"
        fun newInstance(context: Context, queryData: Pair<String, String?>? = null) {
            context.startActivity(
                Intent(context, AffiliateRegistrationActivity::class.java).apply {
                    putExtra(QUERY_PARAM, queryData)
                }
            )
        }
    }

    override fun getLayoutRes(): Int = R.layout.affiliate_registration_layout
}
