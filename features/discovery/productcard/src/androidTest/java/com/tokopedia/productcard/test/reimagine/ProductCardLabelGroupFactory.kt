package com.tokopedia.productcard.test.reimagine

import com.tokopedia.productcard.reimagine.LABEL_OVERLAY_
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

internal fun labelGroupOverlay1(width: Int) = LabelGroup(
    position = LABEL_OVERLAY_ + "1",
    imageUrl = "https://images.tokopedia.net/img/jbZAUJ/2023/12/19/d95215c1-0213-4a49-9241-6eb668e38eb5.png",
    styles = listOf(
        LabelGroup.Style(LabelGroupStyle.WIDTH, width.toString()),
    ),
)

internal fun labelGroupOverlay2(width: Int) = LabelGroup(
    position = LABEL_OVERLAY_ + "2",
    imageUrl = "https://images.tokopedia.net/img/jbZAUJ/2023/12/19/116496e7-df32-4c13-a485-9b9607cfdd02.png",
    styles = listOf(
        LabelGroup.Style(LabelGroupStyle.WIDTH, width.toString()),
    ),
)

internal fun labelGroupOverlay3(width: Int) = LabelGroup(
    position = LABEL_OVERLAY_ + "3",
    imageUrl = "https://images.tokopedia.net/img/jbZAUJ/2023/12/19/0e95ea9c-358c-4739-8a03-6fd188b83cd8.png",
    styles = listOf(
        LabelGroup.Style(LabelGroupStyle.WIDTH, width.toString()),
    ),
)
