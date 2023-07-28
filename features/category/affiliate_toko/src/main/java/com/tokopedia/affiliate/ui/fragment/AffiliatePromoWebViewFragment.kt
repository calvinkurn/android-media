package com.tokopedia.affiliate.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.affiliate.ON_REGISTERED
import com.tokopedia.affiliate.ON_REVIEWED
import com.tokopedia.affiliate.PROMO_WEBVIEW_URL_PROD
import com.tokopedia.affiliate.PROMO_WEBVIEW_URL_STAGING
import com.tokopedia.affiliate.SYSTEM_DOWN
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.ui.activity.AffiliateRegistrationActivity
import com.tokopedia.affiliate.ui.custom.AffiliateBaseFragment
import com.tokopedia.affiliate.viewmodel.AffiliatePromoViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.affiliate_toko.databinding.AffiliatePromoWebviewLayoutBinding
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class AffiliatePromoWebViewFragment : AffiliateBaseFragment<AffiliatePromoViewModel>() {
    private var affiliatePromoViewModel: AffiliatePromoViewModel? = null
    val url = if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
        PROMO_WEBVIEW_URL_STAGING
    } else {
        PROMO_WEBVIEW_URL_PROD
    }

    @Inject
    @JvmField
    var viewModelProvider: ViewModelProvider.Factory? = null

    @Inject
    @JvmField
    var userSessionInterface: UserSessionInterface? = null

    @Inject
    @JvmField
    var remoteConfigInstance: RemoteConfigInstance? = null

    private var binding by autoClearedNullable<AffiliatePromoWebviewLayoutBinding>()

    private var webViewFragment: Fragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AffiliatePromoWebviewLayoutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        webViewFragment = AffiliateHelpFragment.getFragmentInstance(url, isPromo = true)
        webViewFragment?.let {
            childFragmentManager.beginTransaction()
                .add(
                    R.id.promo_web_view_container,
                    it
                )
                .commit()
        }
    }

    companion object {
        fun getFragmentInstance(): Fragment {
            return AffiliatePromoWebViewFragment()
        }
    }

    override fun getVMFactory(): ViewModelProvider.Factory? {
        return viewModelProvider
    }

    override fun initInject() {
        getComponent().injectPromoWebViewFragment(this)
    }

    private fun getComponent(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()

    override fun onSystemDown() {
        affiliatePromoViewModel?.setValidateUserType(SYSTEM_DOWN)
    }

    override fun onReviewed() {
        affiliatePromoViewModel?.setValidateUserType(ON_REVIEWED)
    }

    override fun onUserNotRegistered() {
        activity?.let {
            AffiliateRegistrationActivity.newInstance(it)
            it.finish()
        }
    }

    override fun onNotEligible() {
        activity?.let {
            AffiliateRegistrationActivity.newInstance(it)
            it.finish()
        }
    }

    override fun onUserValidated() {
        affiliatePromoViewModel?.setValidateUserType(ON_REGISTERED)
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliatePromoViewModel = viewModel as AffiliatePromoViewModel
    }

    override fun getViewModelType(): Class<AffiliatePromoViewModel> {
        return AffiliatePromoViewModel::class.java
    }

    fun handleBack() {
        (webViewFragment as? AffiliateHelpFragment)?.handleBack()
    }
}
