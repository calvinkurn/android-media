package com.tokopedia.purchase_platform.common.feature.gifting.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnWordingModel(
        var packagingAndGreetingCard: String = "",
        var onlyGreetingCard: String = "",
        var invoiceNotSendToRecipient: String = ""
) : Parcelable
