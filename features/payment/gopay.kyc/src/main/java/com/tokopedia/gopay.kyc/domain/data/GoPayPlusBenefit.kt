package com.tokopedia.gopay.kyc.domain.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.gopay.kyc.utils.ViewType

data class GoPayPlusBenefit(
    val benefitTitle: String,
    val benefitGopay: String,
    val benefitGopayPlus: String,
    val isLastItem: Boolean = false,
): Visitable<ViewType> {
    override fun type(typeFactory: ViewType): Int {
        return typeFactory.type(this)
    }
}