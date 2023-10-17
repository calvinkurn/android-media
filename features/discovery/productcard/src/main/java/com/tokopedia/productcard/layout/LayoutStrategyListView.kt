package com.tokopedia.productcard.layout

import com.tokopedia.productcard.layout.image.ImageLayoutStrategyListView
import com.tokopedia.productcard.layout.label.LabelLayoutStrategyListView
import com.tokopedia.productcard.layout.shadow.ShadowLayoutStrategyClear
import com.tokopedia.productcard.layout.spacemediainfo.SpaceMediaToInfoStrategyListView

internal class LayoutStrategyListView: BaseLayoutStrategy(
    imageLayoutStrategy = ImageLayoutStrategyListView(),
    labelLayoutStrategy = LabelLayoutStrategyListView(),
    shadowLayoutStrategy = ShadowLayoutStrategyClear(),
    spaceMediaToInfoStrategy = SpaceMediaToInfoStrategyListView(),
)
