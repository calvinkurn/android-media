package com.tokopedia.play_common.transformer

import android.text.Spanned


/**
 * Created by mzennis on 10/02/21.
 */
interface HtmlTextTransformer {

    fun transform(input: String): String

    fun transformWithStyle(input: String): Spanned
}