package com.tokopedia.topads.view.fragment

import android.graphics.Typeface
import android.os.Bundle
import com.tokopedia.topads.create.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.topads.constants.SpanConstant
import com.tokopedia.topads.create.databinding.MpAdCreationOnboardingFragmentBinding
import com.tokopedia.topads.utils.Span
import com.tokopedia.topads.utils.SpannableUtils
import com.tokopedia.topads.utils.SpannedString
import com.tokopedia.utils.image.ImageUtils

class MpAdCreationOnboardingFragment : TkpdBaseV4Fragment() {

    companion object{
        private const val SCREEN_NAME = "MpAdCreationOnboarding"
        private const val FOOTER_LINK_TEXT = "Yuk, Kenalan Dulu"
        private const val ONBOARDING_IMG_URL = "https://images.tokopedia.net/img/topads/topads_onboarding.png"

        fun newInstance() : MpAdCreationOnboardingFragment{
            return MpAdCreationOnboardingFragment()
        }
    }

    private var binding:MpAdCreationOnboardingFragmentBinding?=null

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
              SpannableUtils.applySpannable(
                  text,
                  SpannedString(FOOTER_LINK_TEXT, listOf(
                      Span(SpanConstant.COLOR_SPAN,linkColor),
                      Span(SpanConstant.TYPEFACE_SPAN,Typeface.BOLD)
                  ))
              )
          } ?: text
      }
    }

    override fun getScreenName() = SCREEN_NAME
}
