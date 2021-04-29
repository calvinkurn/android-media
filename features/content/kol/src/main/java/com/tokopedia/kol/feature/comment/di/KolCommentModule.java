package com.tokopedia.kol.feature.comment.di;

import com.tokopedia.feedcomponent.di.FeedComponentModule;
import com.tokopedia.feedcomponent.domain.usecase.GetMentionableUserUseCase;
import com.tokopedia.kol.feature.comment.domain.interactor.DeleteKolCommentUseCase;
import com.tokopedia.kol.feature.comment.domain.interactor.GetKolCommentsUseCase;
import com.tokopedia.kol.feature.comment.domain.interactor.SendKolCommentUseCase;
import com.tokopedia.kol.feature.comment.view.adapter.typefactory.KolCommentTypeFactory;
import com.tokopedia.kol.feature.comment.view.adapter.typefactory.KolCommentTypeFactoryImpl;
import com.tokopedia.kol.feature.comment.view.listener.KolComment;
import com.tokopedia.kol.feature.comment.view.presenter.KolCommentPresenter;
import com.tokopedia.kol.feature.report.view.listener.ContentReportContract;
import com.tokopedia.kol.feature.report.view.presenter.ContentReportPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author by milhamj on 18/04/18.
 */

@Module(includes = {FeedComponentModule.class})
public class KolCommentModule {
    private final KolComment.View viewListener;
    private final KolComment.View.ViewHolder viewHolderListener;

    public KolCommentModule(KolComment.View viewListener,
                            KolComment.View.ViewHolder viewHolderListener) {
        this.viewListener = viewListener;
        this.viewHolderListener = viewHolderListener;
    }

    @KolCommentScope
    @Provides
    KolComment.Presenter providesPresenter(GetKolCommentsUseCase getKolCommentsUseCase,
                                           SendKolCommentUseCase sendKolCommentUseCase,
                                           DeleteKolCommentUseCase deleteKolCommentUseCase,
                                           GetMentionableUserUseCase getMentionableUserUseCase) {
        return new KolCommentPresenter(getKolCommentsUseCase,
                sendKolCommentUseCase,
                deleteKolCommentUseCase,
                getMentionableUserUseCase
        );
    }
    @Provides
    @KolCommentScope
    public ContentReportContract.Presenter providePresenter(ContentReportPresenter contentReportPresenter){
        return contentReportPresenter;
    }

    @KolCommentScope
    @Provides
    KolCommentTypeFactory providesTypeFactory() {
        return new KolCommentTypeFactoryImpl(viewListener, viewHolderListener);
    }
}
