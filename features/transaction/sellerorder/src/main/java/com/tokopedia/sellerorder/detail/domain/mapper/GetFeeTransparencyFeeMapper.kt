package com.tokopedia.sellerorder.detail.domain.mapper

import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeUiModelWrapper
import javax.inject.Inject

class GetFeeTransparencyFeeMapper @Inject constructor() {

    fun mapToTransparencyFeeWrapperUiModel(): TransparencyFeeUiModelWrapper {
        return TransparencyFeeUiModelWrapper("", emptyList())
    }
}
