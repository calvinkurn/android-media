package com.tokopedia.digital_product_detail.domain.repository

import com.tokopedia.common.topupbills.favoritepdp.domain.repository.RechargeFavoriteNumberRepository

interface DigitalPDPTokenListrikRepository : RechargeFavoriteNumberRepository,
    RechargeCatalogMenuDetailRepository,
    RechargeAddToCartRepository,
    RechargeCatalogProductInputMultiTabRepository,
    RechargeCatalogOperatorSelectGroupRepository,
    RechargeRecommendationRepository