package com.tokopedia.loginregister.invalid_phone_number.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.invalid_phone_number.data.model.RegisterCheckModel
import com.tokopedia.loginregister.invalid_phone_number.domain.InvalidPhoneNumberUseCase
import com.tokopedia.loginregister.invalid_phone_number.domain.InvalidPhoneNumberUseCaseContract
import com.tokopedia.loginregister.invalid_phone_number.view.viewmodel.InvalidPhoneNumberViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class InvalidPhoneNumberModule {

    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    fun provideInvalidPhoneNumberUseCase(
        graphqlRepository: GraphqlRepository
    ): GraphqlUseCase<RegisterCheckModel> = GraphqlUseCase(graphqlRepository)

}

@Module
abstract class InteractorModule {
    @Binds
    abstract fun provideInvalidPhoneNumberUseCase(
        invalidPhoneNumberUseCase: InvalidPhoneNumberUseCase
    ): InvalidPhoneNumberUseCaseContract
}

@Module
abstract class InvalidPhoneNumberViewModelModule {
    @Binds
    @InvalidPhoneNumberScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(InvalidPhoneNumberViewModel::class)
    internal abstract fun provideViewModel(viewModel: InvalidPhoneNumberViewModel): ViewModel
}