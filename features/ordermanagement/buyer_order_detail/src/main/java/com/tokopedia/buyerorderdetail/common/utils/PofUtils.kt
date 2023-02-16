package com.tokopedia.buyerorderdetail.common.utils

import android.content.Context
import android.graphics.Color
import android.text.method.LinkMovementMethod
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography

object PofUtils {

    const val STRING_COLOR_INDEX = 2

    fun Typography.setupHyperlinkText(
        message: String,
        onHyperLinkClicked: (String) -> Unit = { }
    ) {
        val getHtmlLinkHelper = HtmlLinkHelper(context, message)
        text = getHtmlLinkHelper.spannedString
        this.movementMethod = LinkMovementMethod.getInstance()
        this.highlightColor = Color.TRANSPARENT
        val htmlLink = getHtmlLinkHelper.urlList.getOrNull(Int.ZERO)
        htmlLink?.setOnClickListener {
            onHyperLinkClicked(htmlLink.linkUrl)
        }
    }

    fun getColorHexString(context: Context, idColor: Int): String {
        return try {
            val colorHexInt = ContextCompat.getColor(context, idColor)
            val colorToHexString = Integer.toHexString(colorHexInt).uppercase().substring(
                STRING_COLOR_INDEX
            )
            "#$colorToHexString"
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}
