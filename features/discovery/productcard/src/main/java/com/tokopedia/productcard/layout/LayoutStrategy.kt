package com.tokopedia.productcard.layout

import com.tokopedia.productcard.layout.eta.EtaLayoutStrategy
import com.tokopedia.productcard.layout.label.LabelLayoutStrategy
import com.tokopedia.productcard.layout.image.ImageLayoutStrategy
import com.tokopedia.productcard.layout.shadow.ShadowLayoutStrategy
import com.tokopedia.productcard.layout.threedots.ThreeDotsLayoutStrategy
import com.tokopedia.productcard.layout.variant.VariantLayoutStrategy

internal interface LayoutStrategy :
    ImageLayoutStrategy,
    LabelLayoutStrategy,
    VariantLayoutStrategy,
    EtaLayoutStrategy,
    ShadowLayoutStrategy,
    ThreeDotsLayoutStrategy
