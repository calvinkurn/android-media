package com.tokopedia.affiliate.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.affiliate.PROMO_WEBVIEW_URL_PROD
import com.tokopedia.affiliate.PROMO_WEBVIEW_URL_STAGING
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate_toko.R
import com.tokopedia.affiliate_toko.databinding.AffiliatePromoWebviewLayoutBinding
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class AffiliatePromoWebViewFragment : BaseDaggerFragment() {
    val url = if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
        PROMO_WEBVIEW_URL_STAGING
    } else {
        PROMO_WEBVIEW_URL_PROD
    }

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

    override fun getScreenName(): String = this::class.java.name

    override fun initInjector() {
        getComponent().injectPromoWebViewFragment(this)
    }

    companion object {
        fun getFragmentInstance(): Fragment {
            return AffiliatePromoWebViewFragment()
        }
    }

    private fun getComponent(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()

    fun handleBack() {
        (webViewFragment as? AffiliateHelpFragment)?.handleBack()
    }
}
