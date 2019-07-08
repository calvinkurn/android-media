package com.tokopedia.profilecompletion.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.data.ProfileCompletionQueriesConstant
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


@ProfileCompletionScope
@Module
class ProfileCompletionQueryModule {

    @ProfileCompletionScope
    @Provides
    @IntoMap
    @StringKey(ProfileCompletionQueriesConstant.MUTATION_CHANGE_GENDER)
    fun provideRawMutationChangeGender(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_change_gender)

    @ProfileCompletionScope
    @Provides
    @IntoMap
    @StringKey(ProfileCompletionQueriesConstant.MUTATION_ADD_EMAIL)
    fun provideRawMutationAddEmail(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_add_email)

    @ProfileCompletionScope
    @Provides
    @IntoMap
    @StringKey(ProfileCompletionQueriesConstant.MUTATION_ADD_PHONE)
    fun provideRawMutationAddPhone(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_add_phone)

    @ProfileCompletionScope
    @Provides
    @IntoMap
    @StringKey(ProfileCompletionQueriesConstant.QUERY_PROFILE_COMPLETION)
    fun provideRawQueryProfileCompletion(@ApplicationContext context: Context): String =
        GraphqlHelper.loadRawString(context.resources, R.raw.query_user_profile_completion)

}