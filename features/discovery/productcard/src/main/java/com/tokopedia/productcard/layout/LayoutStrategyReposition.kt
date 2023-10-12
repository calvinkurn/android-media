package com.tokopedia.productcard.layout

import com.tokopedia.productcard.layout.label.LabelLayoutStrategyGridReposition
import com.tokopedia.productcard.layout.shadow.ShadowLayoutStrategyBorder

internal class LayoutStrategyReposition: BaseLayoutStrategy(
    labelLayoutStrategy = LabelLayoutStrategyGridReposition(),
    shadowLayoutStrategy = ShadowLayoutStrategyBorder(),
)
