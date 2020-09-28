package com.tokopedia.ovop2p.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class OvoP2pTransferThankyouBase(@SerializedName("goalP2PThanks")
                                 var ovoP2pTransferThankyou: OvoP2pTransferThankyou = OvoP2pTransferThankyou()) : Parcelable