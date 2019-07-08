package com.tokopedia.profilecompletion.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addemail.data.AddEmailPojo
import com.tokopedia.profilecompletion.addphone.data.AddPhonePojo
import com.tokopedia.profilecompletion.addphone.data.CheckPhonePojo
import com.tokopedia.profilecompletion.changegender.data.ChangeGenderPojo
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

    @Provides
    fun provideChangeGenderGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<ChangeGenderPojo> = GraphqlUseCase(graphqlRepository)

    @ProfileCompletionScope
    @Provides
    @IntoMap
    @StringKey(ProfileCompletionQueriesConstant.MUTATION_ADD_EMAIL)
    fun provideRawMutationAddEmail(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_add_email)


    @Provides
    fun provideAddEmailGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<AddEmailPojo> = GraphqlUseCase(graphqlRepository)

    @ProfileCompletionScope
    @Provides
    @IntoMap
    @StringKey(ProfileCompletionQueriesConstant.MUTATION_ADD_PHONE)
    fun provideRawMutationAddPhone(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_add_phone)

    @Provides
    fun provideAddPhoneGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<AddPhonePojo> = GraphqlUseCase(graphqlRepository)

    @ProfileCompletionScope
    @Provides
    @IntoMap
    @StringKey(ProfileCompletionQueriesConstant.MUTATION_CHECK_PHONE)
    fun provideRawMutationCheckPhone(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_check_phone)

    @Provides
    fun provideCheckPhoneGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<CheckPhonePojo> = GraphqlUseCase(graphqlRepository)

}