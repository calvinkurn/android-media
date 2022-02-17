package com.tokopedia.digital_product_detail.data.repository

import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPTokenListrikRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeAddToCartRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogMenuDetailRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogOperatorSelectGroupRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogPrefixSelectRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeFavoriteNumberRepository
import javax.inject.Inject

class DigitalPDPTokenListrikRepositoryImpl @Inject constructor(
    private val rechargeFavoriteNumberRepo: RechargeFavoriteNumberRepository,
    private val rechargeCatalogPrefixSelectRepo: RechargeCatalogPrefixSelectRepository,
    private val rechargeCatalogMenuDetailRepo: RechargeCatalogMenuDetailRepository,
    private val rechargeAddToCardRepo: RechargeAddToCartRepository,
    private val rechargeCatalogProduct: RechargeCatalogRepository,
    private val rechargeSelectGroup: RechargeCatalogOperatorSelectGroupRepository
): DigitalPDPTokenListrikRepository,
    RechargeFavoriteNumberRepository by rechargeFavoriteNumberRepo,
    RechargeCatalogPrefixSelectRepository by rechargeCatalogPrefixSelectRepo,
    RechargeCatalogMenuDetailRepository by rechargeCatalogMenuDetailRepo,
    RechargeAddToCartRepository by rechargeAddToCardRepo,
    RechargeCatalogRepository by rechargeCatalogProduct,
    RechargeCatalogOperatorSelectGroupRepository by rechargeSelectGroup