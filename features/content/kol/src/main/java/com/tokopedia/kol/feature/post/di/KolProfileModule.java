package com.tokopedia.kol.feature.post.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.graphql.domain.GraphqlUseCase;
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
import com.tokopedia.kol.feature.postdetail.domain.interactor.GetPostDetailUseCase;
import com.tokopedia.kol.feature.postdetail.view.listener.KolPostDetailContract;
import com.tokopedia.kol.feature.postdetail.view.presenter.KolPostDetailPresenter;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.user.session.UserSessionInterface;

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
    provideKolPostDetailPresenter(GetPostDetailUseCase getPostDetailUseCase,
                                  LikeKolPostUseCase likeKolPostUseCase,
                                  FollowKolPostGqlUseCase followKolPostGqlUseCase,
                                  ToggleFavouriteShopUseCase toggleFavouriteShopUseCase,
                                  UserSessionInterface userSession) {
        return new KolPostDetailPresenter(getPostDetailUseCase,
                likeKolPostUseCase,
                followKolPostGqlUseCase,
                toggleFavouriteShopUseCase,
                userSession);
    }

    @KolProfileScope
    @Provides
    ToggleFavouriteShopUseCase provideToggleFavouriteShopUseCase(@ApplicationContext Context context) {
        return new ToggleFavouriteShopUseCase(new GraphqlUseCase(), context.getResources());
    }

//    @KolProfileScope
//    @Named("WS")
//    @Provides
//    Retrofit provideWsRetrofitDomain(OkHttpClient okHttpClient,
//                                     Retrofit.Builder retrofitBuilder) {
//        return retrofitBuilder.baseUrl(TkpdBaseURL.BASE_DOMAIN)
//                .client(okHttpClient)
//                .build();
//    }
//
//    @KolProfileScope
//    @Provides
//    TopAdsApi provideTopAdsApi(@Named("WS") Retrofit retrofit) {
//        return retrofit.create(TopAdsApi.class);
//    }

    @KolProfileScope
    @Provides
    KolPostShopContract.Presenter
    provideKolPostShopPresenter(GetContentListUseCase getContentListUseCase) {
        return new KolPostShopPresenter(getContentListUseCase);
    }
}
