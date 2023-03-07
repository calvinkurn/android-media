package com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EthicalDrugDataModel(
    var needPrescription: Boolean = false,
    var iconUrl: String = "",
    var text: String = ""
) : Parcelable
