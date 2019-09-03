package com.tokopedia.power_merchant.subscribe.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gm.common.di.GmCommonModule
import com.tokopedia.power_merchant.subscribe.view.contract.PmSubscribeContract
import com.tokopedia.power_merchant.subscribe.view.contract.PmTermsContract
import com.tokopedia.power_merchant.subscribe.view.presenter.PmSubscribePresenter
import com.tokopedia.power_merchant.subscribe.view.presenter.PmTermsPresenter
import com.tokopedia.power_merchant.subscribe.view.viewmodel.PMCancellationQuestionnaireViewModel
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
@PowerMerchantSubscribeScope
abstract class ViewModelModule {

    @PowerMerchantSubscribeScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @PowerMerchantSubscribeScope
    @ViewModelKey(PMCancellationQuestionnaireViewModel::class)
    internal abstract fun pmCancellationQuestionnaireViewModel(viewModel: PMCancellationQuestionnaireViewModel): ViewModel

}