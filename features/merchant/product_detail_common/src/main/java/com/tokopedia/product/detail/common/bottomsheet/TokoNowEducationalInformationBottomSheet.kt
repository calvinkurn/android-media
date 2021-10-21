package com.tokopedia.product.detail.common.bottomsheet

import android.content.Context
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.product.detail.common.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography

class TokoNowEducationalInformationBottomSheet : BottomSheetUnify() {

    companion object {
        const val BACKGROUND_URL =
            "https://images.tokopedia.net/img/android/others/bg_bottomsheet_tokomart_educational_information.png"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        clearContentPadding = true
        showCloseIcon = true
        isDragable = false
        isHideable = false
        setTitle(getString(R.string.pdp_usp_tokomart_static_title))
        setChild(generateView(inflater, container))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun generateView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(
            R.layout.bottomsheet_tokomart_educational_information, container
        ).apply {
            val text1 = findViewById<Typography>(R.id.pdp_tokomart_text_1)
            val text2 = findViewById<Typography>(R.id.pdp_tokomart_text_2)
            val text3 = findViewById<Typography>(R.id.pdp_tokomart_text_3)
            val text4 = findViewById<Typography>(R.id.pdp_tokomart_text_4)
            val text5 = findViewById<Typography>(R.id.pdp_tokomart_text_5)
            val background = findViewById<AppCompatImageView>(R.id.pdp_tokomart_background)

            val boldColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950).toString()
            text1.text = MethodChecker.fromHtml(
                getString(
                    R.string.pdp_usp_tokomart_static_text_1,
                    boldColor
                )
            )
            text2.text = MethodChecker.fromHtml(
                getString(
                    R.string.pdp_usp_tokomart_static_text_2,
                    boldColor
                )
            )
            text3.text = MethodChecker.fromHtml(
                getString(
                    R.string.pdp_usp_tokomart_static_text_3,
                    boldColor
                )
            )

            convertStringToLink(text4, context, R.string.pdp_usp_tokomart_static_text_4)
            convertStringToLink(text5, context, R.string.pdp_usp_tokomart_static_text_5)

            background.loadImageWithoutPlaceholder(BACKGROUND_URL)
        }
    }

    private fun convertStringToLink(typography: Typography, context: Context, stringRes: Int) {
        val linkColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500).toString()
        val linkHelper = HtmlLinkHelper(context, getString(stringRes, linkColor))
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