package com.tokopedia.affiliate.feature.createpost.view.type

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import com.tokopedia.affiliate.R

sealed class ShareType(
        @StringRes val titleRes: Int,
        @DrawableRes val imageRes: Int,
        val isActivated: Boolean,
        val isMandatory: Boolean
) {

    object Default : ShareType(R.string.af_share_tokopedia, R.drawable.ic_btn_other, true, true)
    data class Facebook(val isActive: Boolean) : ShareType(R.string.af_share_facebook, R.drawable.ic_btn_fb, isActive, false)
    data class Twitter(val isActive: Boolean) : ShareType(R.string.af_share_twitter, R.drawable.ic_btn_twitter, isActive, false)
}