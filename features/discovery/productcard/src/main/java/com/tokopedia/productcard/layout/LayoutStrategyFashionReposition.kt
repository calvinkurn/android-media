package com.tokopedia.productcard.layout

import com.tokopedia.productcard.layout.label.LabelLayoutStrategyFashionReposition
import com.tokopedia.productcard.layout.name.NameLayoutStrategyReposition
import com.tokopedia.productcard.layout.variant.VariantLayoutStrategyReposition

internal class LayoutStrategyFashionReposition: BaseLayoutStrategy(
    nameLayoutStrategy = NameLayoutStrategyReposition(),
    labelLayoutStrategy = LabelLayoutStrategyFashionReposition(),
    variantLayoutStrategy = VariantLayoutStrategyReposition(),
)
