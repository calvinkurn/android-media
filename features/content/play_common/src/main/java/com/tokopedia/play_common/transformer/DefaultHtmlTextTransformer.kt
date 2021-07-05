package com.tokopedia.play_common.transformer

import com.tokopedia.abstraction.common.utils.view.MethodChecker


/**
 * Created by mzennis on 10/02/21.
 */
class DefaultHtmlTextTransformer : HtmlTextTransformer {

    override fun transform(input: String): String = MethodChecker.fromHtml(input).toString()

}