package com.tokopedia.digital_product_detail.domain.repository

import com.tokopedia.common.topupbills.favoritepdp.domain.repository.RechargeFavoriteNumberRepository

interface DigitalPDPTagihanListrikRepository : RechargeFavoriteNumberRepository,
    RechargeCatalogMenuDetailRepository,
    RechargeAddToCartRepository,
    RechargeCatalogOperatorSelectGroupRepository,
    RechargeCatalogProductInputMultiTabRepository,
    RechargeInquiryRepository