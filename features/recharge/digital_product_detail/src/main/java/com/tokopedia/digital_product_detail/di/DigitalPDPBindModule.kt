package com.tokopedia.digital_product_detail.di

import com.tokopedia.digital_product_detail.data.repository.DigitalPDPTagihanListrikRepositoryImpl
import com.tokopedia.digital_product_detail.data.repository.DigitalPDPTelcoRepositoryImpl
import com.tokopedia.digital_product_detail.data.repository.DigitalPDPTokenListrikRepositoryImpl
import com.tokopedia.digital_product_detail.data.repository.RechargeAddToCartRepositoryImpl
import com.tokopedia.digital_product_detail.data.repository.RechargeCatalogInputMultiTabRepositoryImpl
import com.tokopedia.digital_product_detail.data.repository.RechargeCatalogMenuDetailRepositoryImpl
import com.tokopedia.digital_product_detail.data.repository.RechargeCatalogOperatorSelectGroupRepositoryImpl
import com.tokopedia.digital_product_detail.data.repository.RechargeCatalogPrefixSelectRepositoryImpl
import com.tokopedia.common.topupbills.favoritepdp.data.repository.RechargeFavoriteNumberRepositoryImpl
import com.tokopedia.digital_product_detail.data.repository.RechargeInquiryRepositoryImpl
import com.tokopedia.digital_product_detail.data.repository.RechargeRecommendationRepositoryImpl
import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPTagihanListrikRepository
import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPTelcoRepository
import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPTokenListrikRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeAddToCartRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogMenuDetailRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogOperatorSelectGroupRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogPrefixSelectRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogProductInputMultiTabRepository
import com.tokopedia.common.topupbills.favoritepdp.domain.repository.RechargeFavoriteNumberRepository
import com.tokopedia.digital_product_detail.data.repository.RechargeMCCMProductsRepositoryImpl
import com.tokopedia.digital_product_detail.data.repository.RechargeCheckBalanceRepositoryImpl
import com.tokopedia.digital_product_detail.domain.repository.RechargeCheckBalanceRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeInquiryRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeMCCMProductsRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeRecommendationRepository
import dagger.Binds
import dagger.Module

@Module
abstract class DigitalPDPBindModule {

    /**
     * Repository
     */
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
    abstract fun bindRechargeInputMultiTabRepository(repo: RechargeCatalogInputMultiTabRepositoryImpl): RechargeCatalogProductInputMultiTabRepository

    @Binds
    @DigitalPDPScope
    abstract fun bindRechargeSelectGroupRepository(repo: RechargeCatalogOperatorSelectGroupRepositoryImpl): RechargeCatalogOperatorSelectGroupRepository

    @Binds
    @DigitalPDPScope
    abstract fun bindRechargeInquiryRepository(repo: RechargeInquiryRepositoryImpl): RechargeInquiryRepository

    @Binds
    @DigitalPDPScope
    abstract fun bindRechargeRecommendationRepository(repo: RechargeRecommendationRepositoryImpl): RechargeRecommendationRepository

    @Binds
    @DigitalPDPScope
    abstract fun bindRechargeMCCMProductsRepository(repo: RechargeMCCMProductsRepositoryImpl): RechargeMCCMProductsRepository

    @Binds
    @DigitalPDPScope
    abstract fun bindDigitalPDPPulsaRepository(repo: DigitalPDPTelcoRepositoryImpl): DigitalPDPTelcoRepository

    @Binds
    @DigitalPDPScope
    abstract fun bindDigitalPDPTokenListrikRepository(repo: DigitalPDPTokenListrikRepositoryImpl): DigitalPDPTokenListrikRepository

    @Binds
    @DigitalPDPScope
    abstract fun bindDigitalPDPTagihanListrikRepository(repo: DigitalPDPTagihanListrikRepositoryImpl): DigitalPDPTagihanListrikRepository

    @Binds
    @DigitalPDPScope
    abstract fun bindRechargeCheckBalanceRepository(repo: RechargeCheckBalanceRepositoryImpl): RechargeCheckBalanceRepository
}

