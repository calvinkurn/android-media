package com.tokopedia.digital_product_detail.data.repository

import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPPulsaRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeAddToCartRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogMenuDetailRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogPrefixSelectRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeFavoriteNumberRepository
import javax.inject.Inject

class DigitalPDPRepositoryImpl @Inject constructor(
    private val rechargeCatalogRepo: RechargeCatalogRepository,
    private val rechargeFavoriteNumberRepo: RechargeFavoriteNumberRepository,
    private val rechargeCatalogPrefixSelectRepo: RechargeCatalogPrefixSelectRepository,
    private val rechargeCatalogMenuDetailRepo: RechargeCatalogMenuDetailRepository,
    private val rechargeAddToCardRepo: RechargeAddToCartRepository
): DigitalPDPPulsaRepository,
        RechargeCatalogRepository by rechargeCatalogRepo,
        RechargeFavoriteNumberRepository by rechargeFavoriteNumberRepo,
        RechargeCatalogPrefixSelectRepository by rechargeCatalogPrefixSelectRepo,
        RechargeCatalogMenuDetailRepository by rechargeCatalogMenuDetailRepo,
        RechargeAddToCartRepository by rechargeAddToCardRepo