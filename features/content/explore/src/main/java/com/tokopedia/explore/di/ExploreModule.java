package com.tokopedia.explore.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.affiliatecommon.data.network.TopAdsApi;
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase;
import com.tokopedia.explore.R;
import com.tokopedia.explore.data.CoroutineThread;
import com.tokopedia.explore.data.ExploreConstant;
import com.tokopedia.explore.domain.entity.GetExploreData;
import com.tokopedia.explore.domain.interactor.GetExploreDataUseCase;
import com.tokopedia.explore.view.listener.ContentExploreContract;
import com.tokopedia.explore.view.presenter.ContentExplorePresenter;
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author by milhamj on 23/07/18.
 */

@ExploreScope
@Module
public class ExploreModule {

    @ExploreScope
    @Provides
    ContentExploreContract.Presenter provideContentExplorePresenter(GetExploreDataUseCase getExploreDataUseCase, TrackAffiliateClickUseCase trackAffiliateClickUseCase) {
        return new ContentExplorePresenter(getExploreDataUseCase, trackAffiliateClickUseCase);
    }

    @ExploreScope
    @Provides
    public UserSessionInterface provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @ExploreScope
    @Provides
    CoroutineThread provideCoroutineThread(){
        return new CoroutineThread();
    }

    @ExploreScope
    @Provides
    GraphqlRepository provideCoroutineGqlRepo(){
        return GraphqlInteractor.getInstance().getGraphqlRepository();
    }

    @ExploreScope
    @Provides
    @Named(ExploreConstant.KEY_QUERY_EXPLORE_HASHTAG)
    String provideRawQueryExploreHashtag(@ApplicationContext Context context){
        return GraphqlHelper.loadRawString(context.getResources(), R.raw.query_get_explore_hashtag);
    }

    @ExploreScope
    @Provides
    GraphqlUseCase<GetExploreData> provideGetExploreHashtagUseCase(@Named(ExploreConstant.KEY_QUERY_EXPLORE_HASHTAG)
                                                                   String rawQuery,
                                                                   GraphqlRepository graphqlRepository){
        GraphqlUseCase<GetExploreData> useCase = new GraphqlUseCase<>(graphqlRepository);
        useCase.setGraphqlQuery(rawQuery);
        useCase.setTypeClass(GetExploreData.class);
        return useCase;
    }

    @ExploreScope
    @Provides
    TopAdsApi provideTopAdsApi(Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.build().create(TopAdsApi.class);
    }
}
