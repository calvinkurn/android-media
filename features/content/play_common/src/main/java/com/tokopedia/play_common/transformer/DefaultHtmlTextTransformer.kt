package com.tokopedia.play_common.transformer

import android.text.Spanned
import com.tokopedia.abstraction.common.utils.view.MethodChecker


/**
 * Created by mzennis on 10/02/21.
 */
class DefaultHtmlTextTransformer : HtmlTextTransformer {

    override fun transform(input: String): String = transformWithStyle(input).toString()

    override fun transformWithStyle(input: String): Spanned = MethodChecker.fromHtml(input)
}