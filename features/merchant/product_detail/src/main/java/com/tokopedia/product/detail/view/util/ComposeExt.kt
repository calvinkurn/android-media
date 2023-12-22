package com.tokopedia.product.detail.view.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import com.tokopedia.nest.principles.utils.toAnnotatedString
import com.tokopedia.unifycomponents.HtmlLinkHelper

/**
 * Created by yovi.putra on 21/12/23"
 * Project name: android-tokopedia-core
 **/

val String.asHtmlLink
    @Composable
    get() = HtmlLinkHelper(
        context = LocalContext.current,
        htmlString = this
    ).spannedString?.toAnnotatedString() ?: ""

fun ComposeView.setContentSafety(content: @Composable () -> Unit) = runCatching {
    setContent(content)
}.onFailure {
    it.printStackTrace()
}
