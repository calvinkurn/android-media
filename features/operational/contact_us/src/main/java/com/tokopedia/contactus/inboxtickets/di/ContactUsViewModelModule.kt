package com.tokopedia.contactus.inboxtickets.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.contactus.inboxtickets.view.inbox.InboxContactUsViewModel
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailViewModel
import com.tokopedia.contactus.inboxtickets.view.viewModel.ContactUsRatingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ContactUsViewModelModule {

    @Binds
    @InboxScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory // ktlint-disable max-line-length

    @Binds
    @InboxScope
    @IntoMap
    @ViewModelKey(ContactUsRatingViewModel::class)
    internal abstract fun bindContactUsViewModel(viewModel: ContactUsRatingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InboxContactUsViewModel::class)
    internal abstract fun provideInboxViewModel(viewModel: InboxContactUsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InboxDetailViewModel::class)
    internal abstract fun provideTicketViewModel(viewModel: InboxDetailViewModel): ViewModel
}
