package com.tokopedia.kol.feature.post.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.affiliatecommon.data.network.TopAdsApi;
import com.tokopedia.affiliatecommon.domain.DeletePostUseCase;
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.kol.R;
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
import com.tokopedia.network.CommonNetwork;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.vote.di.VoteModule;
import com.tokopedia.vote.domain.usecase.SendVoteUseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author by milhamj on 12/02/18.
 */

@Module(includes = {VoteModule.class})
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
    KolPostDetailContract.Presenter provideKolPostDetailPresenter(GetPostDetailUseCase getPostDetailUseCase,
                                  LikeKolPostUseCase likeKolPostUseCase,
                                  FollowKolPostGqlUseCase followKolPostGqlUseCase,
                                  ToggleFavouriteShopUseCase toggleFavouriteShopUseCase,
                                  SendVoteUseCase sendVoteUseCase,
                                  TrackAffiliateClickUseCase trackAffiliateClickUseCase,
                                  DeletePostUseCase deletePostUseCase,
                                  AddToCartUseCase atcUseCase,
                                  UserSessionInterface userSession) {
        return new KolPostDetailPresenter(getPostDetailUseCase,
                likeKolPostUseCase,
                followKolPostGqlUseCase,
                toggleFavouriteShopUseCase,
                sendVoteUseCase,
                trackAffiliateClickUseCase,
                deletePostUseCase,
                atcUseCase,
                userSession);
    }

    @KolProfileScope
    @Provides
    ToggleFavouriteShopUseCase provideToggleFavouriteShopUseCase(@ApplicationContext Context context) {
        return new ToggleFavouriteShopUseCase(new GraphqlUseCase(), context.getResources());
    }

    @KolProfileScope
    @Provides
    UserSession provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @KolProfileScope
    @Provides
    Retrofit provideWsRetrofitDomain(@ApplicationContext Context context,
                                     UserSession userSession) {
        if (!(context instanceof NetworkRouter)) {
            throw new IllegalStateException("Application must implement NetworkRouter");
        }

        return CommonNetwork.createRetrofit(
                context,
                TokopediaUrl.Companion.getInstance().getTA(),
                (NetworkRouter) context,
                userSession
        );
    }

    @KolProfileScope
    @Provides
    TopAdsApi provideTopAdsApi(Retrofit retrofit) {
        return retrofit.create(TopAdsApi.class);
    }

    @KolProfileScope
    @Provides
    KolPostShopContract.Presenter
    provideKolPostShopPresenter(GetContentListUseCase getContentListUseCase) {
        return new KolPostShopPresenter(getContentListUseCase);
    }

    @Provides
    @KolProfileScope
    @Named("atcMutation")
    String provideAddToCartMutation(@ApplicationContext Context context) {
        return GraphqlHelper.loadRawString(context.getResources(), R.raw.mutation_add_to_cart);
    }
}
