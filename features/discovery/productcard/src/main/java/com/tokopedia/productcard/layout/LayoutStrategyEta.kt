package com.tokopedia.productcard.layout

import com.tokopedia.productcard.layout.eta.EtaLayoutStrategy
import com.tokopedia.productcard.layout.eta.EtaLayoutStrategyReposition
import com.tokopedia.productcard.layout.label.LabelLayoutStrategy
import com.tokopedia.productcard.layout.label.LabelLayoutStrategyControl
import com.tokopedia.productcard.layout.image.ImageLayoutStrategy
import com.tokopedia.productcard.layout.image.ImageLayoutStrategyControl
import com.tokopedia.productcard.layout.shadow.ShadowLayoutStrategy
import com.tokopedia.productcard.layout.shadow.ShadowLayoutStrategyControl
import com.tokopedia.productcard.layout.shadow.ShadowLayoutStrategyNone
import com.tokopedia.productcard.layout.threedots.ThreeDotsLayoutStrategy
import com.tokopedia.productcard.layout.threedots.ThreeDotsLayoutStrategyControl
import com.tokopedia.productcard.layout.threedots.ThreeDotsLayoutStrategyNone
import com.tokopedia.productcard.layout.variant.VariantLayoutStrategy
import com.tokopedia.productcard.layout.variant.VariantLayoutStrategyControl

internal class LayoutStrategyEta: LayoutStrategy,
    ImageLayoutStrategy by ImageLayoutStrategyControl(),
    LabelLayoutStrategy by LabelLayoutStrategyControl(),
    VariantLayoutStrategy by VariantLayoutStrategyControl(),
    EtaLayoutStrategy by EtaLayoutStrategyReposition(),
    ShadowLayoutStrategy by ShadowLayoutStrategyNone(),
    ThreeDotsLayoutStrategy by ThreeDotsLayoutStrategyNone()
