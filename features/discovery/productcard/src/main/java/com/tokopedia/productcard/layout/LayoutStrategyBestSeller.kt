package com.tokopedia.productcard.layout

import com.tokopedia.productcard.layout.eta.EtaLayoutStrategy
import com.tokopedia.productcard.layout.eta.EtaLayoutStrategyControl
import com.tokopedia.productcard.layout.image.ImageLayoutStrategy
import com.tokopedia.productcard.layout.image.ImageLayoutStrategyBestSeller
import com.tokopedia.productcard.layout.label.LabelLayoutStrategy
import com.tokopedia.productcard.layout.label.LabelLayoutStrategyControl
import com.tokopedia.productcard.layout.name.NameLayoutStrategy
import com.tokopedia.productcard.layout.name.NameLayoutStrategyBestSeller
import com.tokopedia.productcard.layout.shadow.ShadowLayoutStrategy
import com.tokopedia.productcard.layout.shadow.ShadowLayoutStrategyClear
import com.tokopedia.productcard.layout.stockbar.StockBarLayoutStrategy
import com.tokopedia.productcard.layout.stockbar.StockBarLayoutStrategyControl
import com.tokopedia.productcard.layout.threedots.ThreeDotsLayoutStrategy
import com.tokopedia.productcard.layout.threedots.ThreeDotsLayoutStrategyControl
import com.tokopedia.productcard.layout.variant.VariantLayoutStrategy
import com.tokopedia.productcard.layout.variant.VariantLayoutStrategyControl

internal class LayoutStrategyBestSeller: LayoutStrategy,
    ImageLayoutStrategy by ImageLayoutStrategyBestSeller(),
    NameLayoutStrategy by NameLayoutStrategyBestSeller(),
    LabelLayoutStrategy by LabelLayoutStrategyControl(),
    VariantLayoutStrategy by VariantLayoutStrategyControl(),
    EtaLayoutStrategy by EtaLayoutStrategyControl(),
    ShadowLayoutStrategy by ShadowLayoutStrategyClear(),
    ThreeDotsLayoutStrategy by ThreeDotsLayoutStrategyControl(),
    StockBarLayoutStrategy by StockBarLayoutStrategyControl()
