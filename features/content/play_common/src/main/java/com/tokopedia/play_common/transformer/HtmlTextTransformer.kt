package com.tokopedia.play_common.transformer


/**
 * Created by mzennis on 10/02/21.
 */
interface HtmlTextTransformer {

    fun transform(input: String): String
}