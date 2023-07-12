package com.tokopedia.promousage.view.model

import com.tokopedia.promousage.constant.PromoConstant

data class PromoTncUiModel(
    val promoCodes: List<PromoItemUiModel> = emptyList()
) : PromoSection {

    override val id: String
        get() = PromoConstant.PROMO_SECTION_TNC // TODO: Temporary

    override val type: String
        get() = PromoConstant.PROMO_SECTION_TNC
}
