package com.tokopedia.power_merchant.subscribe.view_old.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class PMCancellationQuestionnaireData(
        val expiredDate: String = "",
        val listQuestion: MutableList<PMCancellationQuestionnaireQuestionModel> = mutableListOf()
) : Parcelable