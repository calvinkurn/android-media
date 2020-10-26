package com.tokopedia.phoneverification.di.revamp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.phoneverification.view.viewmodel.PhoneVerificationViewModel
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
abstract class PhoneVerificationViewModelModule {

    @Binds
    @PhoneVerificationScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PhoneVerificationViewModel::class)
    abstract fun phoneVerificationViewModel(viewModel: PhoneVerificationViewModel): ViewModel
}