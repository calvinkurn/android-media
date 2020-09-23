package com.tokopedia.purchase_platform.common.feature.button

import android.os.Parcelable
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ABTestButton(
        val enable: Boolean = false
) : Parcelable {

    fun getUnifyButtonType(): Int {
        if (enable) {
            return UnifyButton.Type.MAIN
        }
        return UnifyButton.Type.TRANSACTION
    }
}