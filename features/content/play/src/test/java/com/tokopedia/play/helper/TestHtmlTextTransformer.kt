package com.tokopedia.play.helper

import com.tokopedia.play_common.transformer.HtmlTextTransformer


/**
 * Created by mzennis on 10/02/21.
 */
class TestHtmlTextTransformer : HtmlTextTransformer {

    override fun transform(input: String): String {
        return input
    }

}