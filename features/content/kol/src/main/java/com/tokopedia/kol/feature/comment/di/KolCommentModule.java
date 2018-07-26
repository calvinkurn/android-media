package com.tokopedia.kol.feature.comment.di;

import com.tokopedia.kol.feature.comment.domain.interactor.DeleteKolCommentUseCase;
import com.tokopedia.kol.feature.comment.domain.interactor.GetKolCommentsUseCase;
import com.tokopedia.kol.feature.comment.domain.interactor.SendKolCommentUseCase;
import com.tokopedia.kol.feature.comment.view.adapter.typefactory.KolCommentTypeFactory;
import com.tokopedia.kol.feature.comment.view.adapter.typefactory.KolCommentTypeFactoryImpl;
import com.tokopedia.kol.feature.comment.view.listener.KolComment;
import com.tokopedia.kol.feature.comment.view.presenter.KolCommentPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author by milhamj on 18/04/18.
 */

@Module
public class KolCommentModule {
    private final KolComment.View viewListener;

    public KolCommentModule(KolComment.View viewListener) {
        this.viewListener = viewListener;
    }

    @KolCommentScope
    @Provides
    KolComment.Presenter providesPresenter(GetKolCommentsUseCase getKolCommentsUseCase,
                                           SendKolCommentUseCase sendKolCommentUseCase,
                                           DeleteKolCommentUseCase deleteKolCommentUseCase) {
        return new KolCommentPresenter(getKolCommentsUseCase,
                sendKolCommentUseCase,
                deleteKolCommentUseCase);
    }

    @KolCommentScope
    @Provides
    KolCommentTypeFactory providesTypeFactory() {
        return new KolCommentTypeFactoryImpl(viewListener);
    }
}
