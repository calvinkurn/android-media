package com.tokopedia.productcard.layout

import com.tokopedia.productcard.layout.image.ImageLayoutStrategyLong
import com.tokopedia.productcard.layout.label.LabelLayoutStrategyFashionReposition
import com.tokopedia.productcard.layout.name.NameLayoutStrategyReposition
import com.tokopedia.productcard.layout.variant.VariantLayoutStrategyReposition

internal class LayoutStrategyFashionLongImage: BaseLayoutStrategy(
    imageLayoutStrategy = ImageLayoutStrategyLong(),
    nameLayoutStrategy = NameLayoutStrategyReposition(),
    labelLayoutStrategy = LabelLayoutStrategyFashionReposition(),
    variantLayoutStrategy = VariantLayoutStrategyReposition(),
)
