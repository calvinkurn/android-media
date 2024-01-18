package com.tokopedia.productcard.test.reimagine

import com.tokopedia.productcard.reimagine.LABEL_NETT_PRICE
import com.tokopedia.productcard.reimagine.LABEL_OVERLAY_1
import com.tokopedia.productcard.reimagine.LABEL_OVERLAY_2
import com.tokopedia.productcard.reimagine.LABEL_OVERLAY_3
import com.tokopedia.productcard.reimagine.LABEL_PREVENTIVE_BLOCK
import com.tokopedia.productcard.reimagine.LABEL_PREVENTIVE_OVERLAY
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
    imageUrl = "https://images.tokopedia.net/img/jbZAUJ/2023/12/19/ee4f4255-f463-4961-99ce-94e7b8697a68.png",
    styles = listOf(
        LabelGroup.Style(LabelGroupStyle.WIDTH, "48"),
    )
)

internal fun labelGroupOverlay1() = LabelGroup(
    position = LABEL_OVERLAY_1,
    imageUrl = "https://images.tokopedia.net/img/jbZAUJ/2023/12/19/d95215c1-0213-4a49-9241-6eb668e38eb5.png",
)

internal fun labelGroupOverlay2() = LabelGroup(
    position = LABEL_OVERLAY_2,
    imageUrl = "https://images.tokopedia.net/img/restriction-engine/bebas-ongkir/overlay-bo20k.png",
)

internal fun labelGroupOverlay3() = LabelGroup(
    position = LABEL_OVERLAY_3,
    imageUrl = "https://images.tokopedia.net/img/jbZAUJ/2023/12/19/0e95ea9c-358c-4739-8a03-6fd188b83cd8.png",
)

internal fun labelGroupNettPrice() = LabelGroup(
    position = LABEL_NETT_PRICE,
    title = "Rp9.000.000",
    imageUrl = "https://images.tokopedia.net/img/jbZAUJ/2023/12/19/36c06351-769f-4cae-941f-9b9586a43acf.png",
    styles = listOf(
        LabelGroup.Style(key = LabelGroupStyle.BACKGROUND_COLOR, value = "#FFF5F6"),
        LabelGroup.Style(key = LabelGroupStyle.BACKGROUND_OPACITY, value = "1"),
        LabelGroup.Style(key = LabelGroupStyle.OUTLINE_COLOR, value = "#FFB2C2"),
        LabelGroup.Style(key = LabelGroupStyle.TEXT_COLOR, value = "#F94D63")
    )
)

internal fun labelGroupPreventiveOverlay() = LabelGroup(
    position = LABEL_PREVENTIVE_OVERLAY,
    title = "PreOrder",
    styles = listOf(
        LabelGroup.Style(key = LabelGroupStyle.BACKGROUND_COLOR, value = "#000000"),
        LabelGroup.Style(key = LabelGroupStyle.BACKGROUND_OPACITY, value = "0.7"),
        LabelGroup.Style(key = LabelGroupStyle.TEXT_COLOR, value = "#FFFFFF")
    )
)

internal fun labelGroupPreventiveBlock() = LabelGroup(
    position = LABEL_PREVENTIVE_BLOCK,
    title = "Diluar Jangkauan",
    styles = listOf(
        LabelGroup.Style(key = LabelGroupStyle.BACKGROUND_COLOR, value = "#000000"),
        LabelGroup.Style(key = LabelGroupStyle.BACKGROUND_OPACITY, value = "0.5"),
        LabelGroup.Style(key = LabelGroupStyle.TEXT_COLOR, value = "#FFFFFF")
    )
)
