package com.tokopedia.mvc.presentation.creation.step1.uimodel

import com.tokopedia.mvc.domain.entity.enums.PageMode

sealed class VoucherCreationEvent {
    data class ChooseVoucherType(
        val pageMode: PageMode, val isVoucherProduct: Boolean
    ) : VoucherCreationEvent()

    object NavigateToNextStep: VoucherCreationEvent()
}
