package com.tokopedia.productcard.layout

import com.tokopedia.productcard.layout.height.HeightLayoutStrategyFullHeight
import com.tokopedia.productcard.layout.label.LabelLayoutStrategyGridReposition
import com.tokopedia.productcard.layout.shadow.ShadowLayoutStrategyBorder

internal class LayoutStrategyFixedGrid: BaseLayoutStrategy(
    labelLayoutStrategy = LabelLayoutStrategyGridReposition(),
    shadowLayoutStrategy = ShadowLayoutStrategyBorder(),
    heightLayoutStrategy = HeightLayoutStrategyFullHeight(),
)
