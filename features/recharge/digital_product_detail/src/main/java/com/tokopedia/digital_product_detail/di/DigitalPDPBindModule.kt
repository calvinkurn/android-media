package com.tokopedia.digital_product_detail.di

import com.tokopedia.digital_product_detail.data.repository.DigitalPDPRepositoryImpl
import com.tokopedia.digital_product_detail.data.repository.RechargeAddToCartRepositoryImpl
import com.tokopedia.digital_product_detail.data.repository.RechargeCatalogMenuDetailRepositoryImpl
import com.tokopedia.digital_product_detail.data.repository.RechargeCatalogPrefixSelectRepositoryImpl
import com.tokopedia.digital_product_detail.data.repository.RechargeCatalogRepositoryImpl
import com.tokopedia.digital_product_detail.data.repository.RechargeFavoriteNumberRepositoryImpl
import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeAddToCartRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogMenuDetailRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogPrefixSelectRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeFavoriteNumberRepository
import dagger.Binds
import dagger.Module

@Module
abstract class DigitalPDPBindModule {

    /**
     * Repository
     */

    @Binds
    @DigitalPDPScope
    abstract fun bindRechargeCatalogRepository(repo: RechargeCatalogRepositoryImpl): RechargeCatalogRepository

    @Binds
    @DigitalPDPScope
    abstract fun bindRechargeFavoriteNumberRepository(repo: RechargeFavoriteNumberRepositoryImpl): RechargeFavoriteNumberRepository

    @Binds
    @DigitalPDPScope
    abstract fun bindRechargeCatalogPrefixSelectRepository(repo: RechargeCatalogPrefixSelectRepositoryImpl): RechargeCatalogPrefixSelectRepository

    @Binds
    @DigitalPDPScope
    abstract fun bindRechargeCatalogMenuDetailRepository(repo: RechargeCatalogMenuDetailRepositoryImpl): RechargeCatalogMenuDetailRepository

    @Binds
    @DigitalPDPScope
    abstract fun bindRechargeAddToCartRepository(repo: RechargeAddToCartRepositoryImpl): RechargeAddToCartRepository

    @Binds
    @DigitalPDPScope
    abstract fun bindDigitalPDPRepository(repo: DigitalPDPRepositoryImpl): DigitalPDPRepository
}