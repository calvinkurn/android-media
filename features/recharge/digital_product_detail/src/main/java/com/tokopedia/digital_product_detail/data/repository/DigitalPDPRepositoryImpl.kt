package com.tokopedia.digital_product_detail.data.repository

import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogPrefixSelectRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeFavoriteNumberRepository
import javax.inject.Inject

class DigitalPDPRepositoryImpl @Inject constructor(
    private val rechargeCatalogRepo: RechargeCatalogRepository,
    private val rechargeFavoriteNumberRepo: RechargeFavoriteNumberRepository,
    private val rechargeCatalogPrefixSelectRepo: RechargeCatalogPrefixSelectRepository
): DigitalPDPRepository,
        RechargeCatalogRepository by rechargeCatalogRepo,
        RechargeFavoriteNumberRepository by rechargeFavoriteNumberRepo,
        RechargeCatalogPrefixSelectRepository by rechargeCatalogPrefixSelectRepo