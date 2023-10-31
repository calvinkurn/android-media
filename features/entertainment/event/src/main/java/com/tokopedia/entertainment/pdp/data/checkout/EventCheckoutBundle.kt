package com.tokopedia.entertainment.pdp.data.checkout

import android.os.Parcelable
import com.tokopedia.entertainment.pdp.data.pdp.MetaDataResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventCheckoutBundle(
    val urlPDP: String = "",
    val metadataResponse: MetaDataResponse = MetaDataResponse(),
    val idPackageActive: String = "",
    val gatewayCode: String = "",
    val softbookExpireTime: String = ""
) : Parcelable
