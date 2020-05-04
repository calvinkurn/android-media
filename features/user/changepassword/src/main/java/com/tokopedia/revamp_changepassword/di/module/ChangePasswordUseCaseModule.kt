package com.tokopedia.revamp_changepassword.di.module

import android.content.Context
import com.tokopedia.revamp_changepassword.di.ChangePasswordScope
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.revamp_changepassword.data.ProfileCompletionData
import com.tokopedia.revamp_changepassword.di.ChangePasswordContext
import com.tokopedia.revamp_changepassword.domain.usecase.GetProfileCompletionUseCase
import dagger.Module
import dagger.Provides

@Module
class ChangePasswordUseCaseModule {

    @ChangePasswordScope
    @Provides
    fun provideGetProfileCompletionUseCase(@ChangePasswordContext context: Context, useCase: GraphqlUseCase<ProfileCompletionData>): GetProfileCompletionUseCase {
        return GetProfileCompletionUseCase(context, useCase)
    }
}