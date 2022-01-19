package com.tokopedia.digital_product_detail.di

import com.tokopedia.digital_product_detail.data.repository.*
import com.tokopedia.digital_product_detail.domain.repository.*
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
    abstract fun bindDigitalPDPRepository(repo: DigitalPDPRepositoryImpl): DigitalPDPRepository
}