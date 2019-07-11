package com.tokopedia.affiliate.feature.createpost.view.type

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import com.tokopedia.affiliate.R

sealed class ShareType(
        @StringRes val titleRes: Int,
        @DrawableRes val imageRes: Int,
        val isActivated: Boolean,
        val isMandatory: Boolean,
        val showTitleFormula: (Context, Int) -> String
) {

    data class Tokopedia(val totalPerson: Int) : ShareType(R.string.af_share_tokopedia, R.drawable.ic_tokopedia, true, true, { ctx, titleRes -> ctx.getString(titleRes, totalPerson) })
    data class Facebook(val isActive: Boolean) : ShareType(R.string.af_share_facebook, R.drawable.ic_facebook, isActive, false, { ctx, titleRes -> ctx.getString(titleRes) })
    data class Twitter(val isActive: Boolean) : ShareType(R.string.af_share_twitter, R.drawable.ic_twitter, isActive, false, { ctx, titleRes -> ctx.getString(titleRes) })
}