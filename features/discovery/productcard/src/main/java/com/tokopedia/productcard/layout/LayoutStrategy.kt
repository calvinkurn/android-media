package com.tokopedia.productcard.layout

import com.tokopedia.productcard.layout.eta.EtaLayoutStrategy
import com.tokopedia.productcard.layout.height.HeightLayoutStrategy
import com.tokopedia.productcard.layout.image.ImageLayoutStrategy
import com.tokopedia.productcard.layout.label.LabelLayoutStrategy
import com.tokopedia.productcard.layout.name.NameLayoutStrategy
import com.tokopedia.productcard.layout.shadow.ShadowLayoutStrategy
import com.tokopedia.productcard.layout.spacemediainfo.SpaceMediaToInfoStrategy
import com.tokopedia.productcard.layout.stockbar.StockBarLayoutStrategy
import com.tokopedia.productcard.layout.variant.VariantLayoutStrategy

internal interface LayoutStrategy :
    ImageLayoutStrategy,
    NameLayoutStrategy,
    LabelLayoutStrategy,
    VariantLayoutStrategy,
    EtaLayoutStrategy,
    ShadowLayoutStrategy,
    StockBarLayoutStrategy,
    HeightLayoutStrategy,
    SpaceMediaToInfoStrategy
