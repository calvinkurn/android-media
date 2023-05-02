package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.coachmark.BuyerOrderDetailCoachMarkItemManager
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.orZero

data class EpharmacyInfoUiModel(
    val consultationDate: String = String.EMPTY,
    val consultationDoctorName: String = String.EMPTY,
    val consultationExpiryDate: String = String.EMPTY,
    val consultationName: String = String.EMPTY,
    val consultationPatientName: String = String.EMPTY,
    val consultationPrescriptionNumber: String = String.EMPTY,
) : BaseVisitableUiModel {

    override fun shouldShow(context: Context?): Boolean {
        return consultationDate.isNotBlank()
                || consultationDoctorName.isNotBlank()
                || consultationExpiryDate.isNotBlank()
                || consultationName.isNotBlank()
                || consultationPatientName.isNotBlank()
                || consultationPrescriptionNumber.isNotBlank()
    }

    override fun getCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager? {
        return null
    }

    override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }
}
