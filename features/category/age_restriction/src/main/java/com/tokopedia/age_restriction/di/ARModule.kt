package com.tokopedia.age_restriction.di

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.age_restriction.ARQueryConstants.Companion.gql_user_profile_dob_update
import com.tokopedia.age_restriction.R
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

import dagger.Module
import dagger.Provides
import javax.inject.Named


@Module
class ARModule {

    @ARScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Named(gql_user_profile_dob_update)
    @ARScope
    @Provides
    fun provideUpdateDobQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_user_profile_dob_update)
    }


}