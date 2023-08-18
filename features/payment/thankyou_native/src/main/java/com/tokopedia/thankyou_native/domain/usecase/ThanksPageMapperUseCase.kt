package com.tokopedia.thankyou_native.domain.usecase

import com.google.gson.Gson
import com.tokopedia.thankyou_native.data.mapper.PaymentDeductionKey
import com.tokopedia.thankyou_native.domain.model.ConfigFlag
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ThanksPageMapperUseCase @Inject constructor() : UseCase<ThanksPageData>() {
    private lateinit var thanksPageData: ThanksPageData

    fun populateThanksPageDataFields(
        thanksPageData: ThanksPageData,
        onSuccess: (ThanksPageData) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        this.thanksPageData = thanksPageData
        execute({ onSuccess(it) }, { onError(it) })
    }

    override suspend fun executeOnBackground(): ThanksPageData {
        thanksPageData.paymentDeductions?.forEach {
            if (it.itemName == PaymentDeductionKey.REWARDS_POINT) {
                thanksPageData.paymentMethodCount++
            }
        }
        thanksPageData.paymentDetails?.apply {
            thanksPageData.paymentMethodCount += (size - 1)
        }

        val configFlagData: ConfigFlag? = thanksPageData.configFlag?.let {
            Gson().fromJson(it, ConfigFlag::class.java)
        }
//        thanksPageData.configFlagData = configFlagData
        thanksPageData.configFlagData = ConfigFlag(
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true
        )

        val gyroData = thanksPageData.customDataOther?.gyroData?.let {
            val map: MutableMap<String, Any?> = HashMap()
            Gson().fromJson(it, map.javaClass)
        }
        thanksPageData.gyroData = gyroData

        return thanksPageData
    }
}
