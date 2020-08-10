package com.tokopedia.contactus.inboxticket2.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.contactus.R
import com.tokopedia.contactus.inboxticket2.domain.usecase.*
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract.InboxBasePresenter
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract.InboxDetailPresenter
import com.tokopedia.contactus.inboxticket2.view.presenter.InboxDetailPresenterImpl
import com.tokopedia.contactus.inboxticket2.view.presenter.InboxListPresenterImpl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
class InboxModule(private val context: Context) {

    @InboxScope
    @Provides
    fun provideContext(): Context {
        return context
    }

    @InboxScope
    @Provides
    fun provideSharedPreference(context: Context?): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    @Named("InboxListPresenter")
    fun provideTicketListPresenter(useCase: GetTicketListUseCase, userSession: UserSessionInterface): InboxBasePresenter {
        return InboxListPresenterImpl(useCase, userSession)
    }

    @Provides
    @Named("InboxDetailPresenter")
    fun provideInboxListPresenter(messageUseCase: PostMessageUseCase,
                                  messageUseCase2: PostMessageUseCase2,
                                  ratingUseCase: PostRatingUseCase,
                                  inboxOptionUseCase: InboxOptionUseCase,
                                  submitRatingUseCase: SubmitRatingUseCase,
                                  closeTicketByUserUseCase: CloseTicketByUserUseCase,
                                  uploadImageUseCase: UploadImageUseCase,
                                  userSession: UserSessionInterface,
                                  dispatcher: CoroutineDispatcher): InboxDetailPresenter {
        return InboxDetailPresenterImpl(messageUseCase, messageUseCase2, ratingUseCase, inboxOptionUseCase, submitRatingUseCase, closeTicketByUserUseCase, uploadImageUseCase, userSession, dispatcher)
    }


    @Provides
    fun getDefaultDispatcher(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    @Provides
    fun provideUserSession(): UserSessionInterface {
        return UserSession(context)
    }

    @Named("close_ticket_by_user")
    @Provides
    fun provideCloseTicketQuery(): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.close_ticket_by_user)
    }

    @Named("inbox_question_query")
    @Provides
    fun provideInboxQuestionQuery(): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.inbox_question_query)
    }

    @Named("submit_rating")
    @Provides
    fun provideSubmitRatingQuery(): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.submit_rating)
    }

    @Named("reply_ticket")
    @Provides
    fun provideReplyTicketQuery(): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.reply_ticket_query)
    }
}