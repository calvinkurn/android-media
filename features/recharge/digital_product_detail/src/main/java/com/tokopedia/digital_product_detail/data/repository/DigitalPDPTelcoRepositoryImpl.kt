package com.tokopedia.digital_product_detail.data.repository

import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPTelcoRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeAddToCartRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogMenuDetailRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogPrefixSelectRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogProductInputMultiTabRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeFavoriteNumberRepository
import javax.inject.Inject

class DigitalPDPTelcoRepositoryImpl @Inject constructor(
    private val rechargeFavoriteNumberRepo: RechargeFavoriteNumberRepository,
    private val rechargeCatalogPrefixSelectRepo: RechargeCatalogPrefixSelectRepository,
    private val rechargeCatalogMenuDetailRepo: RechargeCatalogMenuDetailRepository,
    private val rechargeAddToCardRepo: RechargeAddToCartRepository,
    private val rechargeCatalogProductInputMultiTabRepository: RechargeCatalogProductInputMultiTabRepository
): DigitalPDPTelcoRepository,
        RechargeFavoriteNumberRepository by rechargeFavoriteNumberRepo,
        RechargeCatalogPrefixSelectRepository by rechargeCatalogPrefixSelectRepo,
        RechargeCatalogMenuDetailRepository by rechargeCatalogMenuDetailRepo,
        RechargeAddToCartRepository by rechargeAddToCardRepo,
        RechargeCatalogProductInputMultiTabRepository by rechargeCatalogProductInputMultiTabRepository