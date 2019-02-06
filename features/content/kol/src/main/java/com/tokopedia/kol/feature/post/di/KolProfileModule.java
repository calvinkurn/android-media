package com.tokopedia.kol.feature.post.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.kol.common.data.source.api.KolApi;
import com.tokopedia.kol.feature.post.data.mapper.LikeKolPostMapper;
import com.tokopedia.kol.feature.post.data.source.LikeKolPostSourceCloud;
import com.tokopedia.kol.feature.post.domain.usecase.FollowKolPostGqlUseCase;
import com.tokopedia.kol.feature.post.domain.usecase.GetContentListUseCase;
import com.tokopedia.kol.feature.post.domain.usecase.LikeKolPostUseCase;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.listener.KolPostShopContract;
import com.tokopedia.kol.feature.post.view.presenter.KolPostPresenter;
import com.tokopedia.kol.feature.post.view.presenter.KolPostShopPresenter;
import com.tokopedia.kol.feature.postdetail.domain.interactor.GetKolPostDetailUseCase;
import com.tokopedia.kol.feature.postdetail.view.listener.KolPostDetailContract;
import com.tokopedia.kol.feature.postdetail.view.presenter.KolPostDetailPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author by milhamj on 12/02/18.
 */

@Module
public class KolProfileModule {
    @KolProfileScope
    @Provides
    KolPostListener.Presenter providesPresenter(KolPostPresenter presenter) {
        return presenter;
    }

    @KolProfileScope
    @Provides
    LikeKolPostSourceCloud provideLikeKolPostSourceCloud(@ApplicationContext Context context, KolApi kolApi, LikeKolPostMapper likeKolPostMapper) {
        return new LikeKolPostSourceCloud(context, kolApi, likeKolPostMapper);
    }

    @KolProfileScope
    @Provides
    KolPostDetailContract.Presenter
    provideKolPostDetailPresenter(GetKolPostDetailUseCase getKolPostDetailUseCase,
                                  LikeKolPostUseCase likeKolPostUseCase,
                                  FollowKolPostGqlUseCase followKolPostGqlUseCase) {
        return new KolPostDetailPresenter(getKolPostDetailUseCase, likeKolPostUseCase,
                followKolPostGqlUseCase);
    }

    @KolProfileScope
    @Provides
    KolPostShopContract.Presenter
    provideKolPostShopPresenter(GetContentListUseCase getContentListUseCase) {
        return new KolPostShopPresenter(getContentListUseCase);
    }
}
