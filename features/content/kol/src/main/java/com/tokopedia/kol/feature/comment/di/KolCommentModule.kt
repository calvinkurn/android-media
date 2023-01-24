package com.tokopedia.kol.feature.comment.di

import com.tokopedia.feedcomponent.di.FeedComponentModule
import com.tokopedia.kol.feature.comment.view.listener.KolComment
import com.tokopedia.kol.feature.comment.di.KolCommentScope
import com.tokopedia.kol.feature.comment.domain.interactor.GetKolCommentsUseCase
import com.tokopedia.kol.feature.comment.domain.interactor.SendKolCommentUseCase
import com.tokopedia.kol.feature.comment.domain.interactor.DeleteKolCommentUseCase
import com.tokopedia.feedcomponent.domain.usecase.GetMentionableUserUseCase
import com.tokopedia.kol.feature.comment.view.presenter.KolCommentPresenter
import com.tokopedia.kol.feature.report.view.presenter.ContentReportPresenter
import com.tokopedia.kol.feature.comment.view.adapter.typefactory.KolCommentTypeFactory
import com.tokopedia.kol.feature.comment.view.adapter.typefactory.KolCommentTypeFactoryImpl
import com.tokopedia.kol.feature.report.domain.usecase.SendReportUseCase
import com.tokopedia.kol.feature.report.view.listener.ContentReportContract
import dagger.Module
import dagger.Provides

/**
 * @author by milhamj on 18/04/18.
 */
@Module(includes = [FeedComponentModule::class])
class KolCommentModule(
    private val viewListener: KolComment.View?,
    private val viewHolderListener: KolComment.View.ViewHolder?
) {
    @KolCommentScope
    @Provides
    fun providesPresenter(
        getKolCommentsUseCase: GetKolCommentsUseCase?,
        sendKolCommentUseCase: SendKolCommentUseCase?,
        deleteKolCommentUseCase: DeleteKolCommentUseCase?,
        getMentionableUserUseCase: GetMentionableUserUseCase?,
        sendReportUseCase: SendReportUseCase?
    ): KolComment.Presenter {
        return KolCommentPresenter(getKolCommentsUseCase,
            sendKolCommentUseCase,
            deleteKolCommentUseCase,
            getMentionableUserUseCase,
            sendReportUseCase
        )
    }

    @Provides
    @KolCommentScope
    fun providePresenter(contentReportPresenter: ContentReportPresenter): ContentReportContract.Presenter {
        return contentReportPresenter
    }

    @KolCommentScope
    @Provides
    fun providesTypeFactory(): KolCommentTypeFactory {
        return KolCommentTypeFactoryImpl(viewListener, viewHolderListener)
    }
}
