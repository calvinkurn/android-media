package com.tokopedia.contactus.inboxticket2.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.tokopedia.contactus.inboxticket2.domain.usecase.*
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract.InboxBasePresenter
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract.InboxDetailPresenter
import com.tokopedia.contactus.inboxticket2.view.contract.ProvideRatingContract
import com.tokopedia.contactus.inboxticket2.view.presenter.InboxDetailPresenterImpl
import com.tokopedia.contactus.inboxticket2.view.presenter.InboxListPresenterImpl
import com.tokopedia.contactus.inboxticket2.view.presenter.ProvideRatingFragmentPresenter
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class InboxModule(private val context: Context) {

    @InboxScope
    @Provides
    fun provideContext(): Context {
        return context.applicationContext
    }

    @InboxScope
    @Provides
    fun provideSharedPreference(context: Context?): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    @Named("InboxListPresenter")
    fun provideTicketListPresenter(useCase: GetTicketListUseCase?): InboxBasePresenter {
        return InboxListPresenterImpl(useCase!!)
    }

    @Provides
    @Named("InboxDetailPresenter")
    fun provideInboxListPresenter(messageUseCase: PostMessageUseCase,
                                  messageUseCase2: PostMessageUseCase2,
                                  ratingUseCase: PostRatingUseCase,
                                  inboxOptionUseCase: InboxOptionUseCase,
                                  submitRatingUseCase: SubmitRatingUseCase,
                                  closeTicketByUserUseCase: CloseTicketByUserUseCase): InboxDetailPresenter {
        return InboxDetailPresenterImpl(messageUseCase, messageUseCase2, ratingUseCase, inboxOptionUseCase, submitRatingUseCase, closeTicketByUserUseCase)
    }

    @InboxScope
    @Provides
    fun provideRatingPresenter(submitRatingUseCase: SubmitRatingUseCase?): ProvideRatingContract.ProvideRatingPresenter {
        return ProvideRatingFragmentPresenter(submitRatingUseCase)
    }

}