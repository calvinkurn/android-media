package com.tokopedia.productcard.layout

import com.tokopedia.productcard.layout.eta.EtaLayoutStrategyReposition

internal class LayoutStrategyEta: BaseLayoutStrategy(
    etaLayoutStrategy = EtaLayoutStrategyReposition(),
)
