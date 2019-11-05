package com.tokopedia.kol.feature.post.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.affiliatecommon.data.network.TopAdsApi;
import com.tokopedia.feedcomponent.di.CoroutineDispatcherModule;
import com.tokopedia.feedcomponent.di.FeedComponentModule;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.kol.R;
import com.tokopedia.kol.feature.post.data.query.LikeKolPostQueryProvider;
import com.tokopedia.kol.feature.post.domain.usecase.GetContentListUseCase;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.listener.KolPostShopContract;
import com.tokopedia.kol.feature.post.view.presenter.KolPostPresenter;
import com.tokopedia.kol.feature.post.view.presenter.KolPostShopPresenter;
import com.tokopedia.kol.feature.postdetail.view.listener.KolPostDetailContract;
import com.tokopedia.kol.feature.postdetail.view.presenter.KolPostDetailPresenter;
import com.tokopedia.network.CommonNetwork;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.vote.di.VoteModule;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

import static com.tokopedia.kol.feature.post.domain.usecase.LikeKolPostUseCase.MUTATION_LIKE_KOL_POST;

/**
 * @author by milhamj on 12/02/18.
 */

@Module(includes = {VoteModule.class, FeedComponentModule.class, CoroutineDispatcherModule.class})
public class KolProfileModule {
    @KolProfileScope
    @Provides
    KolPostListener.Presenter providesPresenter(KolPostPresenter presenter) {
        return presenter;
    }

    @KolProfileScope
    @Provides
    KolPostDetailContract.Presenter provideKolPostDetailPresenter(KolPostDetailPresenter presenter) {
        return presenter;
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

    @KolProfileScope
    @Provides
    @Named(MUTATION_LIKE_KOL_POST)
    String provideLikeKolPostMutation() {
        return LikeKolPostQueryProvider.INSTANCE.getQuery();
    }
}
