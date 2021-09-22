package com.tokopedia.tokopedianow.home.presentation.bottomsheet

import android.content.Context
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokopedianow.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography

class TokoNowHomeEducationalInformationBottomSheet : BottomSheetUnify() {

    companion object {
        private const val BACKGROUND_BOTTOMSHEET = "https://images.tokopedia.net/img/android/tokonow/tokopedianow_ic_bottom_background_bottomsheet.png"
        private val TAG = TokoNowHomeEducationalInformationBottomSheet::class.simpleName

        fun newInstance(): TokoNowHomeEducationalInformationBottomSheet {
            return TokoNowHomeEducationalInformationBottomSheet()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun initView(inflater: LayoutInflater, container: ViewGroup?) {
        clearContentPadding = true
        showCloseIcon = true
        isDragable = false
        isHideable = false
        setupItemView(inflater, container)
        setTitle(getString(R.string.tokopedianow_home_educational_information_title))
    }

    private fun setupItemView(inflater: LayoutInflater, container: ViewGroup?) {
        val itemView = inflater.inflate(R.layout.bottomsheet_tokopedianow_educational_information, container)
        setUi(itemView)
        setChild(itemView)
    }

    private fun setUi(itemView: View) {
        itemView.apply {
            val ivBackgroundImage = findViewById<AppCompatImageView>(R.id.iv_background_image)
            val tpTwoHours = findViewById<Typography>(R.id.tp_two_hours)
            val tpStockAvailable = findViewById<Typography>(R.id.tp_stock_available)
            val tpGuaranteedQuality = findViewById<Typography>(R.id.tp_guaranteed_quality)
            val tpTwentyFourHours = findViewById<Typography>(R.id.tp_twenty_four_hours)
            val tpTermsAndConditions = findViewById<Typography>(R.id.tp_terms_and_conditions)

            tpTwoHours.text = MethodChecker.fromHtml(getString(R.string.tokopedianow_home_educational_information_two_hours_bottomsheet))
            tpStockAvailable.text = MethodChecker.fromHtml(getString(R.string.tokopedianow_home_educational_information_stock_available_bottomsheet))
            tpGuaranteedQuality.text = MethodChecker.fromHtml(getString(R.string.tokopedianow_home_educational_information_guaranteed_quality_bottomsheet))

            convertStringToLink(tpTwentyFourHours, context, R.string.tokopedianow_home_educational_information_twenty_four_hours_bottomsheet)
            convertStringToLink(tpTermsAndConditions, context, R.string.tokopedianow_home_educational_information_terms_and_conditions_bottomsheet)

            Glide.with(this)
                .load(BACKGROUND_BOTTOMSHEET)
                .into(ivBackgroundImage)
        }
    }

    private fun convertStringToLink(typography: Typography, context: Context, stringRes: Int) {
        val linkHelper = HtmlLinkHelper(context, getString(stringRes))
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