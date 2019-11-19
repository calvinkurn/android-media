package com.tokopedia.createpost.view.type

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tokopedia.createpost.createpost.R
import com.tokopedia.kotlin.extensions.view.toCompactAmountString

private typealias ShareSubtitleGenerator = (Context, Int) -> String?

sealed class ShareType(
        @StringRes val keyRes: Int,
        @StringRes val titleRes: Int,
        @StringRes val subtitleRes: Int?,
        @DrawableRes val imageRes: Int,
        val isActivated: Boolean,
        val isMandatory: Boolean,
        val subtitleFormula: ShareSubtitleGenerator? = null
) {

    data class Tokopedia(val totalPerson: Int?) :
            ShareType(
                    R.string.af_share_tokopedia,
                    R.string.af_share_tokopedia_option,
                    R.string.af_share_tokopedia_hint,
                    R.drawable.ic_tokopedia,
                    true,
                    true,
                    { ctx, subtitleRes ->
                        if (totalPerson != null) ctx.getString(subtitleRes, totalPerson.toCompactAmountString())
                        else null
                    }
            )

    data class Facebook(val isActive: Boolean) :
            ShareType(
                    R.string.af_share_facebook,
                    R.string.af_share_facebook_option,
                    null,
                    R.drawable.ic_facebook,
                    isActive,
                    false
            )

    data class Twitter(val isActive: Boolean) :
            ShareType(
                    R.string.af_share_twitter,
                    R.string.af_share_twitter_option,
                    null,
                    R.drawable.ic_twitter,
                    isActive,
                    false
            )
}