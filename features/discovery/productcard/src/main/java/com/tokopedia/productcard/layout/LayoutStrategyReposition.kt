package com.tokopedia.productcard.layout

import com.tokopedia.productcard.layout.eta.EtaLayoutStrategy
import com.tokopedia.productcard.layout.eta.EtaLayoutStrategyControl
import com.tokopedia.productcard.layout.image.ImageLayoutStrategy
import com.tokopedia.productcard.layout.image.ImageLayoutStrategyControl
import com.tokopedia.productcard.layout.label.LabelLayoutStrategy
import com.tokopedia.productcard.layout.label.LabelLayoutStrategyReposition
import com.tokopedia.productcard.layout.name.NameLayoutStrategy
import com.tokopedia.productcard.layout.name.NameLayoutStrategyControl
import com.tokopedia.productcard.layout.shadow.ShadowLayoutStrategy
import com.tokopedia.productcard.layout.shadow.ShadowLayoutStrategyBorder
import com.tokopedia.productcard.layout.stockbar.StockBarLayoutStrategy
import com.tokopedia.productcard.layout.stockbar.StockBarLayoutStrategyControl
import com.tokopedia.productcard.layout.threedots.ThreeDotsLayoutStrategy
import com.tokopedia.productcard.layout.threedots.ThreeDotsLayoutStrategyNone
import com.tokopedia.productcard.layout.variant.VariantLayoutStrategy
import com.tokopedia.productcard.layout.variant.VariantLayoutStrategyControl

internal class LayoutStrategyReposition: LayoutStrategy,
    ImageLayoutStrategy by ImageLayoutStrategyControl(),
    NameLayoutStrategy by NameLayoutStrategyControl(),
    LabelLayoutStrategy by LabelLayoutStrategyReposition(),
    VariantLayoutStrategy by VariantLayoutStrategyControl(),
    EtaLayoutStrategy by EtaLayoutStrategyControl(),
    ShadowLayoutStrategy by ShadowLayoutStrategyBorder(),
    ThreeDotsLayoutStrategy by ThreeDotsLayoutStrategyNone(),
    StockBarLayoutStrategy by StockBarLayoutStrategyControl()
