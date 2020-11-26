package com.tokopedia.ovop2p.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class OvoP2pTransferSource(@SerializedName("name")
                           var name: String = "",
                           @SerializedName("phone")
                           var phone: String = "") : Parcelable
