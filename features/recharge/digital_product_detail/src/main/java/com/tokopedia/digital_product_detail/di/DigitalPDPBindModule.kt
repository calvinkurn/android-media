package com.tokopedia.digital_product_detail.di

import com.tokopedia.digital_product_detail.data.repository.DigitalPDPRepositoryImpl
import com.tokopedia.digital_product_detail.data.repository.RechargeCatalogRepositoryImpl
import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogRepository
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
    abstract fun bindDigitalPDPRepository(repo: DigitalPDPRepositoryImpl): DigitalPDPRepository
}