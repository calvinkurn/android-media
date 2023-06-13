package com.tokopedia.feedcomponent.people.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.feedcomponent.people.mapper.ProfileMutationMapper
import com.tokopedia.feedcomponent.people.mapper.ProfileMutationMapperImpl
import dagger.Module
import dagger.Provides

/**
 * created by fachrizalmrsln on 14/10/22
 **/
@Module
class PeopleModule {

    @Provides
    fun provideShopRecommendationUiMapper(@ApplicationContext context: Context): ProfileMutationMapper {
        return ProfileMutationMapperImpl(context)
    }

}
