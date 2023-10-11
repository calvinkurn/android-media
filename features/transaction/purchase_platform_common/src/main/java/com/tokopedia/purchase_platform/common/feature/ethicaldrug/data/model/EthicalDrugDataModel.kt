package com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EthicalDrugDataModel(
    val needPrescription: Boolean = false,
    val iconUrl: String = "",
    val text: String = ""
) : Parcelable
