package com.tokopedia.contactus.inboxticket2.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.contactus.inboxticket2.view.viewModel.ContactUsRatingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ContactUsViewModelModule {

    @Binds
    @InboxScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @InboxScope
    @IntoMap
    @ViewModelKey(ContactUsRatingViewModel::class)
    internal abstract fun bindContactUsViewModel(viewModel: ContactUsRatingViewModel): ViewModel

}
