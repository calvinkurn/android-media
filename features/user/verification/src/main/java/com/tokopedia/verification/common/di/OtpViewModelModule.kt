package com.tokopedia.verification.common.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.verification.notif.viewmodel.NotifViewModel
import com.tokopedia.verification.qrcode.viewmodel.LoginByQrViewModel
import com.tokopedia.verification.otp.viewmodel.VerificationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Ade Fulki on 2019-10-20.
 * ade.hadian@tokopedia.com
 */

@Module
abstract class OtpViewModelModule{

    @Binds
    @OtpScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(VerificationViewModel::class)
    internal abstract fun bindValidatorViewModel(viewModel: VerificationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NotifViewModel::class)
    internal abstract fun bindNotifViewModel(viewModel: NotifViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginByQrViewModel::class)
    internal abstract fun bindLoginByQrViewModel(viewModel: LoginByQrViewModel): ViewModel
}
