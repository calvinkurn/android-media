package com.tokopedia.kyc_centralized.ui.gotoKyc.main

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.databinding.FragmentGotoKycBridgingAccountLinkingBinding
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.utils.lifecycle.autoClearedNullable

class BridgingAccountLinkingFragment : TkpdBaseV4Fragment() {

    private var binding by autoClearedNullable<FragmentGotoKycBridgingAccountLinkingBinding>()

    private val args: BridgingAccountLinkingFragmentArgs by navArgs()
    private var isDoneGopay: Boolean = false
    private var hintNameKtp: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGotoKycBridgingAccountLinkingBinding.inflate(inflater, container, false)

        isDoneGopay = args.isDoneGopay
        hintNameKtp = args.hintNameKtp

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.unifyToolbar?.setNavigationOnClickListener { activity?.finish() }
        initView()
        initSpannable()
        initListener()
    }

    private fun initView() {
        binding?.apply {
            ivBridgingAccountLinking.loadImageWithoutPlaceholder(
                getString(R.string.img_url_goto_kyc_bridging_account_linking)
            )

            tvSubtitle.text = if (isDoneGopay) {
                getString(R.string.goto_kyc_bridging_done_gopay_subtitle)
            } else {
                getString(R.string.goto_kyc_bridging_not_done_gopay_subtitle)
            }

            layoutDoneGopay.root.showWithCondition(isDoneGopay)
            layoutNotDoneGopay.root.showWithCondition(!isDoneGopay)

            btnConfirm.text = if (isDoneGopay) {
                getString(R.string.goto_kyc_bridging_done_gopay_button)
            } else {
                getString(R.string.goto_kyc_bridging_not_done_gopay_button)
            }

            tvTokopediaCare.showWithCondition(isDoneGopay)

            if (isDoneGopay) {
                layoutDoneGopay.imgItem.loadImageWithoutPlaceholder(
                    getString(R.string.img_url_goto_kyc_onboard_gopay)
                )
                layoutDoneGopay.tvNameKtp.text = hintNameKtp
            } else {
                layoutNotDoneGopay.ivKtp.loadImageWithoutPlaceholder(
                    getString(R.string.img_url_goto_kyc_onboard_ktp)
                )
                layoutNotDoneGopay.ivSelfie.loadImageWithoutPlaceholder(
                    getString(R.string.img_url_goto_kyc_onboard_selfie)
                )
            }
        }
    }

    private fun initListener() {
        binding?.btnConfirm?.setOnClickListener {
            if (isDoneGopay) {
                goToDobChallenge()
            } else {
                goToKycProcess()
            }
        }
    }

    private fun initSpannable() {
        val message = getString(R.string.goto_kyc_bridging_done_gopay_tokopedia_care)
        val indexStar = message.indexOf(getString(R.string.goto_kyc_bridging_done_gopay_tokopedia_care_text_link))
        val indexEnd = message.length

        val spannable = SpannableString(message)
        spannable.setSpan(
            object : ClickableSpan() {
                override fun onClick(view: View) {
                    goToTokopediaCare()
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_G500
                    )
                    ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
            },
            indexStar,
            indexEnd,
            0
        )
        binding?.tvTokopediaCare?.apply {
            movementMethod = LinkMovementMethod.getInstance()
            setText(spannable, TextView.BufferType.SPANNABLE)
        }
    }

    private fun goToTokopediaCare() {
        RouteManager.route(
            context,
            String.format(
                TOKOPEDIA_CARE_STRING_FORMAT,
                ApplinkConst.WEBVIEW,
                TokopediaUrl.getInstance().MOBILEWEB.plus(TOKOPEDIA_CARE_PATH)
            )
        )
    }

    private fun goToDobChallenge() {

    }

    private fun goToKycProcess() {

    }

    override fun getScreenName(): String = SCREEN_NAME

    companion object {
        private const val TOKOPEDIA_CARE_PATH = "help"
        private const val TOKOPEDIA_CARE_STRING_FORMAT = "%s?url=%s"
        private val SCREEN_NAME = BridgingAccountLinkingFragment::class.java.simpleName
    }
}
