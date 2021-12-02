package com.tokopedia.tokopedianow.educationalinfo.presentation.bottomsheet

import android.content.Context
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.BottomsheetTokopedianowEducationalInformationBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.lifecycle.autoClearedNullable

class TokoNowEducationalInfoBottomSheet :
    BottomSheetUnify(){

    companion object {
        private const val BACKGROUND_BOTTOMSHEET = "https://images.tokopedia.net/img/android/others/bg_bottomsheet_tokomart_educational_information.png"
        private val TAG = TokoNowEducationalInfoBottomSheet::class.simpleName

        fun newInstance(): TokoNowEducationalInfoBottomSheet {
            return TokoNowEducationalInfoBottomSheet()
        }
    }

    private var binding by autoClearedNullable<BottomsheetTokopedianowEducationalInformationBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun initView() {
        binding = BottomsheetTokopedianowEducationalInformationBinding.inflate(LayoutInflater.from(context))
        clearContentPadding = true
        showCloseIcon = true
        isDragable = false
        isHideable = false
        setChild(binding?.root)
        setUi(binding)
        setTitle(getString(R.string.tokopedianow_home_educational_information_title))
    }

    private fun setUi(binding: BottomsheetTokopedianowEducationalInformationBinding?) {
        context?.let { context ->
            binding?.apply {
                val boldColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950).toString()
                tpTwoHours.text = MethodChecker.fromHtml(getString(R.string.tokopedianow_home_educational_information_two_hours_bottomsheet, boldColor, boldColor))
                tpStockAvailable.text = MethodChecker.fromHtml(getString(R.string.tokopedianow_home_educational_information_stock_available_bottomsheet, boldColor))
                tpGuaranteedQuality.text = MethodChecker.fromHtml(getString(R.string.tokopedianow_home_educational_information_guaranteed_quality_bottomsheet, boldColor))

                convertStringToLink(tpTwentyFourHours, context, R.string.tokopedianow_home_educational_information_twenty_four_hours_bottomsheet)
                convertStringToLink(tpTermsAndConditions, context, R.string.tokopedianow_home_educational_information_terms_and_conditions_bottomsheet)

                Glide.with(context)
                    .load(BACKGROUND_BOTTOMSHEET)
                    .into(ivBackgroundImage)
            }
        }
    }

    private fun convertStringToLink(typography: Typography, context: Context, stringRes: Int) {
        val greenColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500).toString()
        val linkHelper = HtmlLinkHelper(context, getString(stringRes, greenColor))
        typography.text = linkHelper.spannedString
        typography.movementMethod = LinkMovementMethod.getInstance()
        linkHelper.urlList[0].let { link ->
            link.onClick = {
                goToInformationPage(link.linkUrl)
            }
        }
    }

    private fun goToInformationPage(linkUrl: String) {
        RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=${linkUrl}")
    }
}