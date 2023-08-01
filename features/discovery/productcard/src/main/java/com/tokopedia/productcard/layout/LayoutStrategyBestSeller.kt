package com.tokopedia.productcard.layout

import com.tokopedia.productcard.layout.image.ImageLayoutStrategyBestSeller
import com.tokopedia.productcard.layout.label.LabelLayoutStrategyListView
import com.tokopedia.productcard.layout.name.NameLayoutStrategyBestSeller
import com.tokopedia.productcard.layout.shadow.ShadowLayoutStrategyClear
import com.tokopedia.productcard.layout.spacemediainfo.SpaceMediaToInfoStrategyListView

internal class LayoutStrategyBestSeller: BaseLayoutStrategy(
    imageLayoutStrategy = ImageLayoutStrategyBestSeller(),
    nameLayoutStrategy = NameLayoutStrategyBestSeller(),
    shadowLayoutStrategy = ShadowLayoutStrategyClear(),
    labelLayoutStrategy = LabelLayoutStrategyListView(),
    spaceMediaToInfoStrategy = SpaceMediaToInfoStrategyListView(),
)
