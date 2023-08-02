package com.tokopedia.wallet.ovoactivation.view.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by nabillasabbaha on 01/10/18.
 */
@Parcelize
class PhoneActionModel(
    var titlePhoneAction: String = "",
    var descPhoneAction: String = "",
    var labelBtnPhoneAction: String = "",
    var applinkPhoneAction: String = ""
) :
    Parcelable
