package com.tokopedia.pms.bankaccount.data.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by zulfikarrahman on 7/5/18.
 */
@Parcelize
data class BankListModel(
    val id: String?,
    val bankName: String?
) : Parcelable