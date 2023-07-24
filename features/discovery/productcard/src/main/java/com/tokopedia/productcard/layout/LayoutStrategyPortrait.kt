package com.tokopedia.productcard.layout

import com.tokopedia.productcard.layout.eta.EtaLayoutStrategy
import com.tokopedia.productcard.layout.eta.EtaLayoutStrategyControl
import com.tokopedia.productcard.layout.image.ImageLayoutStrategy
import com.tokopedia.productcard.layout.image.ImageLayoutStrategyLong
import com.tokopedia.productcard.layout.label.LabelLayoutStrategy
import com.tokopedia.productcard.layout.label.LabelLayoutStrategyControl
import com.tokopedia.productcard.layout.name.NameLayoutStrategy
import com.tokopedia.productcard.layout.name.NameLayoutStrategyControl
import com.tokopedia.productcard.layout.shadow.ShadowLayoutStrategy
import com.tokopedia.productcard.layout.shadow.ShadowLayoutStrategyControl
import com.tokopedia.productcard.layout.stockbar.StockBarLayoutStrategy
import com.tokopedia.productcard.layout.stockbar.StockBarLayoutStrategyControl
import com.tokopedia.productcard.layout.threedots.ThreeDotsLayoutStrategy
import com.tokopedia.productcard.layout.threedots.ThreeDotsLayoutStrategyNone
import com.tokopedia.productcard.layout.variant.VariantLayoutStrategy
import com.tokopedia.productcard.layout.variant.VariantLayoutStrategyControl

internal class LayoutStrategyPortrait: LayoutStrategy,
    ImageLayoutStrategy by ImageLayoutStrategyLong(),
    NameLayoutStrategy by NameLayoutStrategyControl(),
    LabelLayoutStrategy by LabelLayoutStrategyControl(),
    VariantLayoutStrategy by VariantLayoutStrategyControl(),
    EtaLayoutStrategy by EtaLayoutStrategyControl(),
    ShadowLayoutStrategy by ShadowLayoutStrategyControl(),
    ThreeDotsLayoutStrategy by ThreeDotsLayoutStrategyNone(),
    StockBarLayoutStrategy by StockBarLayoutStrategyControl()
