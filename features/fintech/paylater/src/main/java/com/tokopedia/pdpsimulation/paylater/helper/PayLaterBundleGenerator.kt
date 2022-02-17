package com.tokopedia.pdpsimulation.paylater.helper

import android.os.Bundle
import com.tokopedia.pdpsimulation.paylater.domain.model.Cta
import com.tokopedia.pdpsimulation.paylater.domain.model.Detail
import com.tokopedia.pdpsimulation.paylater.domain.model.InstallmentDetails
import com.tokopedia.pdpsimulation.paylater.presentation.bottomsheet.PayLaterActionStepsBottomSheet
import com.tokopedia.pdpsimulation.paylater.presentation.bottomsheet.PayLaterInstallmentFeeInfo
import com.tokopedia.pdpsimulation.paylater.presentation.bottomsheet.PayLaterTokopediaGopayBottomsheet

object PayLaterBundleGenerator {

    fun getHowToUseBundle(detail: Detail) = Bundle().apply {
        putParcelable(PayLaterActionStepsBottomSheet.STEPS_DATA, detail)
    }

    fun getGoPayBundle(cta: Cta) = Bundle().apply {
        putParcelable(PayLaterTokopediaGopayBottomsheet.GOPAY_BOTTOMSHEET_DETAIL, cta)
    }

    fun getInstallmentBundle(installmentDetails: InstallmentDetails?) = Bundle().apply {
        putParcelable(PayLaterInstallmentFeeInfo.INSTALLMENT_DETAIL, installmentDetails)
    }

}