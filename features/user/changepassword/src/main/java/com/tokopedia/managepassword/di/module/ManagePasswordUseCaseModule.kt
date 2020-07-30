package com.tokopedia.managepassword.di.module

import android.content.Context
import com.tokopedia.managepassword.di.ManagePasswordScope
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.managepassword.data.ProfileDataModel
import com.tokopedia.managepassword.di.ManagePasswordContext
import com.tokopedia.managepassword.domain.usecase.GetProfileCompletionUseCase
import dagger.Module
import dagger.Provides

@Module
class ManagePasswordUseCaseModule {

    @ManagePasswordScope
    @Provides
    fun provideGetProfileCompletionUseCase(@ManagePasswordContext context: Context, useCase: GraphqlUseCase<ProfileDataModel>): GetProfileCompletionUseCase {
        return GetProfileCompletionUseCase(context, useCase)
    }
}