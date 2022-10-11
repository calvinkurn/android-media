package com.tokopedia.createpost.common.view.type

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tokopedia.createpost.common.R
import com.tokopedia.kotlin.extensions.view.toAmountString

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
                    R.string.cp_common_share_tokopedia,
                    R.string.cp_common_share_tokopedia_option,
                    R.string.cp_common_share_tokopedia_hint,
                    R.drawable.cp_common_ic_tokopedia,
                    true,
                    true,
                    { ctx, subtitleRes ->
                        if (totalPerson != null) ctx.getString(subtitleRes, totalPerson.toAmountString())
                        else null
                    }
            )

        data class Twitter(val isActive: Boolean) :
            ShareType(
                    R.string.cp_common_share_twitter,
                    R.string.cp_common_share_twitter_option,
                    null,
                    R.drawable.cp_common_ic_twitter,
                    isActive,
                    false
            )
}