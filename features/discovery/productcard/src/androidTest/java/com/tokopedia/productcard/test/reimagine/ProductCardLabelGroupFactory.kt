package com.tokopedia.productcard.test.reimagine

import com.tokopedia.productcard.reimagine.LABEL_REIMAGINE_ASSIGNED_VALUE
import com.tokopedia.productcard.reimagine.LABEL_REIMAGINE_BENEFIT
import com.tokopedia.productcard.reimagine.LABEL_REIMAGINE_PREVENTIVE_THEMATIC
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
    type = "lightGreen",
)

internal fun labelGroupPreventiveThematic() = LabelGroup(
    position = LABEL_REIMAGINE_PREVENTIVE_THEMATIC,
    title = "Plus Value Day",
    type = listOf(
        "${LabelGroupStyle.TEXT_COLOR}=#FFFFFF",
        "${LabelGroupStyle.BACKGROUND_COLOR}=#000000",
        "${LabelGroupStyle.BACKGROUND_OPACITY}=0.7",
    ).joinToString("&"),
)

internal fun labelGroupProductOffers() = LabelGroup(
    position = LABEL_REIMAGINE_PRODUCT_OFFER,
    title = "Beli 3 Diskon 10%",
)

internal fun labelGroupAssignedValueGradient() = LabelGroup(
    position = LABEL_REIMAGINE_ASSIGNED_VALUE,
    title = "Tokopedia Choice",
    imageUrl = "icon.net",
    type = listOf(
        "${LabelGroupStyle.TEXT_COLOR}=#FFFFFF",
        "${LabelGroupStyle.BACKGROUND_COLOR}=#098A4E,#7ADBA5",
    ).joinToString("&"),
)

internal fun labelGroupAssignedValueSolid() = LabelGroup(
    position = LABEL_REIMAGINE_ASSIGNED_VALUE,
    title = "Mumpung Murah",
    type = listOf(
        "${LabelGroupStyle.TEXT_COLOR}=#FFFFFF",
        "${LabelGroupStyle.BACKGROUND_COLOR}=#E02954",
    ).joinToString("&"),
)
