package com.tokopedia.discovery2.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.discovery2.repository.cpmtopads.CpmTopAdsGQLRepository
import com.tokopedia.discovery2.repository.cpmtopads.CpmTopAdsRepository
import com.tokopedia.discovery2.repository.pushstatus.pushstatus.PushStatusGQLRepository
import com.tokopedia.discovery2.repository.pushstatus.pushstatus.PushStatusRepository
import com.tokopedia.discovery2.repository.tokopoints.TokopointsRepository
import com.tokopedia.discovery2.repository.tokopoints.TokopointsRestRepository
import com.tokopedia.discovery2.repository.horizontalcategory.CategoryNavigationRepository
import com.tokopedia.discovery2.repository.horizontalcategory.CategoryNavigationRestRepository
import com.tokopedia.discovery2.repository.productcards.ProductCardsRepository
import com.tokopedia.discovery2.repository.productcards.ProductCardsRestRepository
import com.tokopedia.tradein_common.repository.BaseRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class DiscoveryModule {

    @DiscoveryScope
    @Provides
    fun provideBaseRepository(): BaseRepository {
        return BaseRepository.repositoryInstance
    }

    @DiscoveryScope
    @Provides
    fun providePushStatusGQLRepository(@ApplicationContext context: Context): PushStatusRepository {
        return PushStatusGQLRepository(provideGetStringMethod(context))
    }

    @DiscoveryScope
    @Provides
    fun provideCpmTopAdsGQLRepository(@ApplicationContext context: Context): CpmTopAdsRepository {
        return CpmTopAdsGQLRepository(provideGetStringMethod(context))
    }

    @DiscoveryScope
    @Provides
    fun provideCategoryNavigationRestRepository(@ApplicationContext context: Context): CategoryNavigationRepository {
        return CategoryNavigationRestRepository()
    }

    @DiscoveryScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @DiscoveryScope
    @Provides
    fun provideTokopointsRestRepository(@ApplicationContext context: Context): TokopointsRepository {
        return TokopointsRestRepository()
    }


    @DiscoveryScope
    @Provides
    fun provideProductCardsRestRepository(@ApplicationContext context: Context): ProductCardsRepository {
        return ProductCardsRestRepository()
    }



    @DiscoveryScope
    @Provides
    fun provideGetStringMethod(@ApplicationContext context: Context): (Int) -> String {
        return { id -> GraphqlHelper.loadRawString(context.resources, id) }
    }
}