package com.tokopedia.productcard.test.reimagine

import com.tokopedia.productcard.reimagine.LABEL_REIMAGINE_ASSIGNED_VALUE
import com.tokopedia.productcard.reimagine.LABEL_REIMAGINE_BENEFIT
import com.tokopedia.productcard.reimagine.LABEL_REIMAGINE_PRODUCT_OFFER
import com.tokopedia.productcard.reimagine.LABEL_REIMAGINE_RIBBON
import com.tokopedia.productcard.reimagine.LabelGroupStyle
import com.tokopedia.productcard.reimagine.ProductCardModel.LabelGroup
import com.tokopedia.productcard.utils.RED

internal fun labelGroupRibbon(type: String = RED) = LabelGroup(
    position = LABEL_REIMAGINE_RIBBON,
    title = "20%",
    type = type,
)

internal fun labelGroupBenefit() = LabelGroup(
    position = LABEL_REIMAGINE_BENEFIT,
    title = "Cashback Rp10 rb",
    styles = listOf(
        LabelGroup.Style(LabelGroupStyle.BACKGROUND_COLOR, "#FFF5F6"),
        LabelGroup.Style(LabelGroupStyle.TEXT_COLOR, "#F94D63"),
        LabelGroup.Style(LabelGroupStyle.OUTLINE_COLOR, "#FFB2C2"),
        LabelGroup.Style(LabelGroupStyle.BACKGROUND_OPACITY, "1"),
    ),
)

internal fun labelGroupProductOffers() = LabelGroup(
    position = LABEL_REIMAGINE_PRODUCT_OFFER,
    title = "Beli 3 Diskon 10%",
    styles = listOf(
        LabelGroup.Style(LabelGroupStyle.TEXT_COLOR, "#FF7F17"),
    )
)

internal fun labelGroupAssignedValue() = LabelGroup(
    position = LABEL_REIMAGINE_ASSIGNED_VALUE,
    imageUrl = "https://images.tokopedia.net/img/restriction-engine/bebas-ongkir/bo_reg_20k.png",
    styles = listOf(
        LabelGroup.Style(LabelGroupStyle.WIDTH, "30"),
    )
)
