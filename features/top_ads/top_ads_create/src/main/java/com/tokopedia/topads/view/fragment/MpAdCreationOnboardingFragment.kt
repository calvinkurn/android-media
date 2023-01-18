package com.tokopedia.topads.view.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.text.method.LinkMovementMethod
import com.tokopedia.topads.create.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.topads.constants.SpanConstant
import com.tokopedia.topads.create.databinding.MpAdCreationOnboardingFragmentBinding
import com.tokopedia.topads.utils.Span
import com.tokopedia.topads.utils.SpannableUtils
import com.tokopedia.topads.utils.SpannedString
import com.tokopedia.topads.view.activity.RoutingCallback
import com.tokopedia.utils.image.ImageUtils

class MpAdCreationOnboardingFragment : TkpdBaseV4Fragment() {

    companion object{
        private const val SCREEN_NAME = "MpAdCreationOnboarding"
        private const val FOOTER_LINK_TEXT = "Yuk, Kenalan Dulu"
        private const val ONBOARDING_IMG_URL = "https://images.tokopedia.net/img/topads/topads_onboarding.png"
        private const val PRODUCT_ID_KEY = "product_id"
        private const val TAG = "MP_TOPADS_CREATE_ONBOARDING"

        fun newInstance(productId:String = "") : MpAdCreationOnboardingFragment{
            return MpAdCreationOnboardingFragment().apply {
                arguments = Bundle().apply {
                    putString(PRODUCT_ID_KEY,productId)
                }
            }
        }
    }

    private var binding:MpAdCreationOnboardingFragmentBinding?=null
    private var productId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productId = arguments?.getString(PRODUCT_ID_KEY) ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MpAdCreationOnboardingFragmentBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        loadOnboardingImage()
        setupFooterText()
        binding?.onboardingCta?.setOnClickListener {
            activity?.let{
                if(it is RoutingCallback){
                    it.addCreateAds()
                }
            }
        }
    }

    private fun setupToolbar(){
        binding?.onboardingHeader?.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun loadOnboardingImage(){
        binding?.onboardingImage?.let {
            ImageUtils.loadImage(
                it,
                ONBOARDING_IMG_URL,
                com.tokopedia.unifycomponents.R.drawable.unify_loader_shimmer
            )
        }
    }

    private fun setupFooterText(){
      binding?.footerText?.let {
          val text = context?.resources?.getString(R.string.mp_ad_creation_onboarding_footer_text).orEmpty()
          it.text = context?.resources?.let { it1 ->
              val linkColor = ResourcesCompat.getColor(it1,com.tokopedia.unifyprinciples.R.color.Unify_GN500,null)
              it.movementMethod = LinkMovementMethod.getInstance()
              SpannableUtils.applySpannable(
                  text,
                  SpannedString(FOOTER_LINK_TEXT, listOf(
                      Span(SpanConstant.COLOR_SPAN,linkColor),
                      Span(SpanConstant.TYPEFACE_SPAN,Typeface.BOLD),
                      Span(SpanConstant.CLICK_SPAN,::goToTopAdsOnboarding)
                  ))
              )
          } ?: text
      }
    }

    private fun goToTopAdsOnboarding(){
        RouteManager.route(context, ApplinkConstInternalTopAds.TOPADS_CREATION_ONBOARD)
    }

    override fun getScreenName() = SCREEN_NAME
}
