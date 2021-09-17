package com.tokopedia.play.helper

import android.text.SpannableString
import android.text.Spanned
import com.tokopedia.play_common.transformer.HtmlTextTransformer


/**
 * Created by mzennis on 10/02/21.
 */
class TestHtmlTextTransformer : HtmlTextTransformer {

    override fun transform(input: String): String {
        return input
    }

    override fun transformWithStyle(input: String): Spanned {
        return SpannableString(input)
    }
}