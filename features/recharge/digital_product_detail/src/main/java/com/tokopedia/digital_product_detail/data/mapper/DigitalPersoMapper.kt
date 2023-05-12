package com.tokopedia.digital_product_detail.data.mapper

import com.tokopedia.digital_product_detail.data.model.data.DigitalPersoData
import com.tokopedia.digital_product_detail.domain.model.DigitalCheckBalanceOTPModel
import com.tokopedia.digital_product_detail.domain.model.DigitalCheckBalanceOTPBottomSheetModel
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import javax.inject.Inject

class DigitalPersoMapper @Inject constructor() {

    fun mapDigiPersoToCheckBalanceOTPModel(data: DigitalPersoData): DigitalCheckBalanceOTPModel {
        val persoItem = data.items.getOrNull(0)
        return DigitalCheckBalanceOTPModel(
            subtitle = persoItem?.subtitle.toEmptyStringIfNull(),
            label = "testing",
            bottomSheetModel = DigitalCheckBalanceOTPBottomSheetModel(
                title = persoItem?.title.toEmptyStringIfNull(),
                mediaUrl = persoItem?.mediaURL.toEmptyStringIfNull(),
                description = persoItem?.label1.toEmptyStringIfNull(),
                buttonText = persoItem?.textLink.toEmptyStringIfNull(),
                buttonAppLink = persoItem?.appLink.toEmptyStringIfNull()
            )
        )
    }
}
