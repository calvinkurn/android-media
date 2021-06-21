package com.tokopedia.kol.feature.post.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.affiliatecommon.data.network.TopAdsApi;
import com.tokopedia.feedcomponent.di.FeedComponentModule;
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.kol.feature.postdetail.view.listener.KolPostDetailContract;
import com.tokopedia.kol.feature.postdetail.view.presenter.KolPostDetailPresenter;
import com.tokopedia.network.CommonNetwork;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.user.session.UserSession;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author by milhamj on 12/02/18.
 */

@Module(includes = {FeedComponentModule.class})
public class KolProfileModule {

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

    @Provides
    @KolProfileScope
    @Named("atcMutation")
    String provideAddToCartMutation(@ApplicationContext Context context) {
        return GraphqlHelper.loadRawString(context.getResources(), com.tokopedia.atc_common.R.raw.mutation_add_to_cart);
    }

    @KolProfileScope
    @Provides
    GraphqlRepository provideGraphqlRepository() {
        return GraphqlInteractor.getInstance().getGraphqlRepository();
    }
}
