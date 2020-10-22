package com.tokopedia.managepassword.di.module

import android.content.Context
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.managepassword.addpassword.domain.data.AddPasswordResponseModel
import com.tokopedia.managepassword.addpassword.domain.usecase.AddPasswordUseCase
import com.tokopedia.managepassword.changepassword.domain.data.ChangePasswordResponseModel
import com.tokopedia.managepassword.changepassword.domain.usecase.ChangePasswordUseCase
import com.tokopedia.managepassword.di.ManagePasswordContext
import com.tokopedia.managepassword.di.ManagePasswordScope
import com.tokopedia.managepassword.forgotpassword.domain.data.ForgotPasswordResponseModel
import com.tokopedia.managepassword.forgotpassword.domain.usecase.ForgotPasswordUseCase
import com.tokopedia.managepassword.haspassword.domain.data.ProfileDataModel
import com.tokopedia.managepassword.haspassword.domain.usecase.GetProfileCompletionUseCase
import dagger.Module
import dagger.Provides

@Module
class ManagePasswordUseCaseModule {

    @ManagePasswordScope
    @Provides
    fun provideGetProfileCompletionUseCase(@ManagePasswordContext context: Context, useCase: GraphqlUseCase<ProfileDataModel>): GetProfileCompletionUseCase {
        return GetProfileCompletionUseCase(context, useCase)
    }

    @ManagePasswordScope
    @Provides
    fun provideForgotPasswordUseCase(@ManagePasswordContext context: Context,useCase: GraphqlUseCase<ForgotPasswordResponseModel>): ForgotPasswordUseCase {
        return ForgotPasswordUseCase(context, useCase)
    }

    @ManagePasswordScope
    @Provides
    fun provideChangePasswordUseCase(@ManagePasswordContext context: Context,useCase: GraphqlUseCase<ChangePasswordResponseModel>): ChangePasswordUseCase {
        return ChangePasswordUseCase(context, useCase)
    }

    @ManagePasswordScope
    @Provides
    fun provideAddPasswordUseCase(@ManagePasswordContext context: Context,useCase: GraphqlUseCase<AddPasswordResponseModel>): AddPasswordUseCase {
        return AddPasswordUseCase(context, useCase)
    }
}