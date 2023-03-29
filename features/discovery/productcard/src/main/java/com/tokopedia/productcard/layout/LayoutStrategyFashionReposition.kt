package com.tokopedia.productcard.layout

import com.tokopedia.productcard.layout.eta.EtaLayoutStrategy
import com.tokopedia.productcard.layout.eta.EtaLayoutStrategyControl
import com.tokopedia.productcard.layout.label.LabelLayoutStrategy
import com.tokopedia.productcard.layout.label.LabelLayoutStrategyFashionReposition
import com.tokopedia.productcard.layout.image.ImageLayoutStrategy
import com.tokopedia.productcard.layout.image.ImageLayoutStrategyControl
import com.tokopedia.productcard.layout.variant.VariantLayoutStrategy
import com.tokopedia.productcard.layout.variant.VariantLayoutStrategyControl
import com.tokopedia.productcard.layout.variant.VariantLayoutStrategyReposition

class LayoutStrategyFashionReposition: LayoutStrategy,
    ImageLayoutStrategy by ImageLayoutStrategyControl(),
    LabelLayoutStrategy by LabelLayoutStrategyFashionReposition(),
    VariantLayoutStrategy by VariantLayoutStrategyReposition(),
    EtaLayoutStrategy by EtaLayoutStrategyControl()
