package com.tokopedia.purchase_platform.common.feature.gifting.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnWordingData(
        var packagingAndGreetingCard: String = "",
        var onlyGreetingCard: String = "",
        var invoiceNotSendToRecipient: String = ""
) : Parcelable