package com.tokopedia.kyc_centralized.ui.gotoKyc.transparent

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.databinding.LayoutGotoKycOnboardProgressiveBinding
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.utils.lifecycle.autoClearedNullable

class OnboardProgressiveBottomSheet(private val source: String): BottomSheetUnify() {

    private var binding by autoClearedNullable<LayoutGotoKycOnboardProgressiveBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutGotoKycOnboardProgressiveBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        setTokopediaCareView()
        binding?.apply {
            layoutDataKtp.imgItem.loadImageWithoutPlaceholder(
                getString(R.string.img_url_goto_kyc_onboard_gopay)
            )

            tvTitle.text = if (source.isEmpty()) {
                getString(R.string.goto_kyc_onboard_progressive_title_account_page)
            } else {
                getString(R.string.goto_kyc_onboard_progressive_title_not_account_page, source)
            }
        }
    }

    private fun setTokopediaCareView() {
        val message = getString(R.string.goto_kyc_onboard_progressive_tokopedia_care)
        val spannable = SpannableString(message)
        spannable.setSpan(
            object : ClickableSpan() {
                override fun onClick(view: View) {
                    goToTokopediaCare()
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.isFakeBoldText = true
                    ds.color = MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_G500
                    )
                }
            },
            message.indexOf(getString(R.string.goto_kyc_onboard_progressive_tokopedia_care_spannable)),
            message.length,
            0
        )
        binding?.tvTokopediaCare?.apply {
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
            setText(spannable, TextView.BufferType.SPANNABLE)
        }
    }

    private fun goToTokopediaCare() {
        RouteManager.route(
            context,
            ApplinkConstInternalGlobal.WEBVIEW,
            TokopediaUrl.getInstance().MOBILEWEB.plus(PATH_TOKOPEDIA_CARE)
        )
    }

    companion object {
        private const val PATH_TOKOPEDIA_CARE = "help?lang=id?isBack=true"
    }

}
