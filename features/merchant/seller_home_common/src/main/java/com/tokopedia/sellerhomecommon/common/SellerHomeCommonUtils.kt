package com.tokopedia.sellerhomecommon.common

import android.widget.TextView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.sellerhomecommon.presentation.model.BaseWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TooltipUiModel
import com.tokopedia.sellerhomecommon.utils.clearUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created By @ilhamsuaib on 02/09/20
 */

object SellerHomeCommonUtils {

    /**
     * Returns list of links contained in given text
     */
    fun extractUrls(text: String): List<String> {
        val urlRegex =
            "((https?|ftp|gopher|telnet|file|tokopedia):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)"
        val pattern: Pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE)
        val urlMatcher: Matcher = pattern.matcher(text)
        val containedUrls = mutableListOf<String>()
        while (urlMatcher.find()) {
            containedUrls.add(text.substring(urlMatcher.start(0), urlMatcher.end(0)))
        }
        return containedUrls
    }

    fun setupWidgetTooltip(
        textView: TextView,
        element: BaseWidgetUiModel<*>,
        onClick: (TooltipUiModel) -> Unit
    ) {
        val tooltip = element.tooltip
        val shouldShowTooltip = (tooltip?.shouldShow == true)
                && (tooltip.content.isNotBlank() || tooltip.list.isNotEmpty())
        if (shouldShowTooltip) {
            textView.setUnifyDrawableEnd(IconUnify.INFORMATION)
            textView.setOnClickListener {
                onClick(tooltip ?: return@setOnClickListener)
            }
        } else {
            textView.clearUnifyDrawableEnd()
        }
    }
}