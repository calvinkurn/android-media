package com.tokopedia.buyerorderdetail.common.utils

import android.graphics.Color
import android.text.method.LinkMovementMethod
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography

object PofUtils {

    fun Typography.setupHyperlinkText(
        message: String,
        onHyperLinkClicked: (String) -> Unit
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
}
