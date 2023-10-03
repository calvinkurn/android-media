package com.tokopedia.productcard.layout

import com.tokopedia.productcard.layout.eta.EtaLayoutStrategy
import com.tokopedia.productcard.layout.eta.EtaLayoutStrategyControl
import com.tokopedia.productcard.layout.height.HeightLayoutStrategy
import com.tokopedia.productcard.layout.height.HeightLayoutStrategyControl
import com.tokopedia.productcard.layout.image.ImageLayoutStrategy
import com.tokopedia.productcard.layout.image.ImageLayoutStrategyControl
import com.tokopedia.productcard.layout.label.LabelLayoutStrategy
import com.tokopedia.productcard.layout.label.LabelLayoutStrategyControl
import com.tokopedia.productcard.layout.name.NameLayoutStrategy
import com.tokopedia.productcard.layout.name.NameLayoutStrategyControl
import com.tokopedia.productcard.layout.shadow.ShadowLayoutStrategy
import com.tokopedia.productcard.layout.shadow.ShadowLayoutStrategyControl
import com.tokopedia.productcard.layout.spacemediainfo.SpaceMediaToInfoStrategy
import com.tokopedia.productcard.layout.spacemediainfo.SpaceMediaToInfoStrategyControl
import com.tokopedia.productcard.layout.stockbar.StockBarLayoutStrategy
import com.tokopedia.productcard.layout.stockbar.StockBarLayoutStrategyControl
import com.tokopedia.productcard.layout.variant.VariantLayoutStrategy
import com.tokopedia.productcard.layout.variant.VariantLayoutStrategyControl

internal abstract class BaseLayoutStrategy(
    imageLayoutStrategy: ImageLayoutStrategy = ImageLayoutStrategyControl(),
    nameLayoutStrategy: NameLayoutStrategy = NameLayoutStrategyControl(),
    labelLayoutStrategy : LabelLayoutStrategy = LabelLayoutStrategyControl(),
    variantLayoutStrategy : VariantLayoutStrategy = VariantLayoutStrategyControl(),
    etaLayoutStrategy : EtaLayoutStrategy = EtaLayoutStrategyControl(),
    shadowLayoutStrategy : ShadowLayoutStrategy = ShadowLayoutStrategyControl(),
    stockBarLayoutStrategy: StockBarLayoutStrategy = StockBarLayoutStrategyControl(),
    heightLayoutStrategy: HeightLayoutStrategy = HeightLayoutStrategyControl(),
    spaceMediaToInfoStrategy: SpaceMediaToInfoStrategy = SpaceMediaToInfoStrategyControl(),
) : LayoutStrategy,
    ImageLayoutStrategy by imageLayoutStrategy,
    NameLayoutStrategy by nameLayoutStrategy,
    LabelLayoutStrategy by labelLayoutStrategy,
    VariantLayoutStrategy by variantLayoutStrategy,
    EtaLayoutStrategy by  etaLayoutStrategy,
    ShadowLayoutStrategy by shadowLayoutStrategy,
    StockBarLayoutStrategy by stockBarLayoutStrategy,
    HeightLayoutStrategy by heightLayoutStrategy,
    SpaceMediaToInfoStrategy by spaceMediaToInfoStrategy
