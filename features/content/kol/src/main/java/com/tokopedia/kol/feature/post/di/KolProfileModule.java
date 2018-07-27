package com.tokopedia.kol.feature.post.di;

import com.tokopedia.kol.common.data.source.api.KolApi;
import com.tokopedia.kol.feature.comment.domain.interactor.GetKolCommentsUseCase;
import com.tokopedia.kol.feature.post.data.mapper.LikeKolPostMapper;
import com.tokopedia.kol.feature.post.data.source.LikeKolPostSourceCloud;
import com.tokopedia.kol.feature.post.domain.interactor.GetKolPostUseCase;
import com.tokopedia.kol.feature.post.domain.interactor.LikeKolPostUseCase;
import com.tokopedia.kol.feature.post.view.adapter.typefactory.KolPostTypeFactory;
import com.tokopedia.kol.feature.post.view.adapter.typefactory.KolPostTypeFactoryImpl;
import com.tokopedia.kol.feature.post.view.listener.KolPostDetailContract;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.presenter.KolPostDetailPresenter;
import com.tokopedia.kol.feature.post.view.presenter.KolPostPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author by milhamj on 12/02/18.
 */

@Module
public class KolProfileModule {
    private final KolPostListener.View.ViewHolder viewListener;

    public KolProfileModule(KolPostListener.View.ViewHolder viewListener) {
        this.viewListener = viewListener;
    }

    @KolProfileScope
    @Provides
    KolPostListener.Presenter providesPresenter(GetKolPostUseCase getKolPostUseCase,
                                                LikeKolPostUseCase likeKolPostUseCase) {
        return new KolPostPresenter(getKolPostUseCase, likeKolPostUseCase);
    }

    @KolProfileScope
    @Provides
    KolPostTypeFactory provideKolTypeFactory() {
        return new KolPostTypeFactoryImpl(viewListener);
    }


    @KolProfileScope
    @Provides
    LikeKolPostSourceCloud provideLikeKolPostSourceCloud(KolApi kolApi, LikeKolPostMapper likeKolPostMapper) {
        return new LikeKolPostSourceCloud(viewListener.getContext(), kolApi, likeKolPostMapper);
    }


    @KolProfileScope
    @Provides
    KolPostDetailContract.Presenter provideKolPostDetailPresenter(GetKolCommentsUseCase getKolCommentsUseCase,
                                                                  LikeKolPostUseCase likeKolPostUseCase) {
        return new KolPostDetailPresenter(getKolCommentsUseCase, likeKolPostUseCase);
    }
}
