package com.tokopedia.contactus.inboxticket2.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.contactus.R
import com.tokopedia.contactus.inboxticket2.data.UploadImageResponse
import com.tokopedia.contactus.inboxticket2.domain.usecase.*
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract.InboxBasePresenter
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract
import com.tokopedia.contactus.inboxticket2.view.presenter.InboxDetailPresenter
import com.tokopedia.contactus.inboxticket2.view.presenter.InboxListPresenter
import com.tokopedia.imageuploader.di.ImageUploaderModule
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderQualifier
import com.tokopedia.imageuploader.domain.GenerateHostRepository
import com.tokopedia.imageuploader.domain.UploadImageRepository
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.utils.ImageUploaderUtils
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module(includes = [ImageUploaderModule::class])
class InboxModule(private val context: Context) {

    @InboxScope
    @Provides
    fun provideContext(): Context {
        return context
    }

    @Provides
    @ApplicationContext
    fun provideApplicationContext(): Context {
        return context.applicationContext
    }

    @InboxScope
    @Provides
    fun provideSharedPreference(context: Context?): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    @Named("InboxListPresenter")
    fun provideTicketListPresenter(useCase: GetTicketListUseCase, chipTopBotStatusUseCase: ChipTopBotStatusUseCase, userSession: UserSessionInterface): InboxBasePresenter {
        return InboxListPresenter(useCase, chipTopBotStatusUseCase, userSession)
    }

    @Provides
    @Named("InboxDetailPresenter")
    fun provideInboxListPresenter(messageUseCase: PostMessageUseCase,
                                  messageUseCase2: PostMessageUseCase2,
                                  ratingUseCase: PostRatingUseCase,
                                  inboxOptionUseCase: InboxOptionUseCase,
                                  submitRatingUseCase: SubmitRatingUseCase,
                                  closeTicketByUserUseCase: CloseTicketByUserUseCase,
                                  contactUsUploadImageUseCase: ContactUsUploadImageUseCase,
                                  userSession: UserSessionInterface,
                                  dispatcher: CoroutineDispatchers): InboxDetailContract.Presenter {
        return InboxDetailPresenter(messageUseCase, messageUseCase2, ratingUseCase, inboxOptionUseCase, submitRatingUseCase, closeTicketByUserUseCase, contactUsUploadImageUseCase, userSession, dispatcher)
    }

    @Provides
    fun provideCoroutineDispatchers(): CoroutineDispatchers = CoroutineDispatchersProvider

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

    @Provides
    fun provideUploadImageUseCase(
            @ImageUploaderQualifier uploadImageRepository: UploadImageRepository,
            @ImageUploaderQualifier generateHostRepository: GenerateHostRepository,
            @ImageUploaderQualifier gson: Gson,
            @ImageUploaderQualifier userSession: UserSessionInterface,
            @ImageUploaderQualifier imageUploaderUtils: ImageUploaderUtils):
            UploadImageUseCase<UploadImageResponse> {
        return UploadImageUseCase(uploadImageRepository, generateHostRepository, gson, userSession,
                UploadImageResponse::class.java, imageUploaderUtils)
    }
}