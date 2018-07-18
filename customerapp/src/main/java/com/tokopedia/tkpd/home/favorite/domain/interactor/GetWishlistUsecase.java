package com.tokopedia.tkpd.home.favorite.domain.interactor;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.entity.wishlist.GqlWishListDataResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.graphql.data.ObservableFactory;
import com.tokopedia.graphql.data.model.CacheType;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.favorite.domain.FavoriteRepository;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.tokopedia.tkpd.home.presenter.WishListImpl.ITEM_COUNT;
import static com.tokopedia.tkpd.home.presenter.WishListImpl.PAGE_NO;
import static com.tokopedia.tkpd.home.presenter.WishListImpl.PARAM_USER_ID;

/**
 * @author Kulomady on 2/9/17.
 */

@SuppressWarnings("WeakerAccess")
public class GetWishlistUsecase /*extends UseCase<DomainWishlist>*/ {

    public static final String KEY_COUNT = "count";
    public static final String KEY_PAGE = "page";
    public static final String KEY_IS_FORCE_REFRESH = "isForceUpdate";

    public static final String DEFAULT_PAGE_VALUE = "1";
    public static final String DEFAULT_COUNT_VALUE = "4";

    private final FavoriteRepository favoriteRepository;

    @Inject
    public GetWishlistUsecase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              FavoriteRepository favoriteRepository) {
//        super(threadExecutor, postExecutionThread);
        this.favoriteRepository = favoriteRepository;
    }

    private void getWishListData(Context context, Subscriber subscriber) {

        Map<String, Object> variables = new HashMap<>();

        variables.put(PARAM_USER_ID, Integer.parseInt(SessionHandler.getLoginID(context)));
        variables.put(PAGE_NO, DEFAULT_PAGE_VALUE);
        variables.put(ITEM_COUNT, DEFAULT_COUNT_VALUE);

        GraphqlRequest graphqlRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.query_get_wishlist),
                GqlWishListDataResponse.class,
                variables);

        List<GraphqlRequest> graphqlRequestList = new ArrayList<>();
        graphqlRequestList.add(graphqlRequest);

        GraphqlCacheStrategy graphqlCacheStrategy =
                new GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build();

        /*Observable<GraphqlResponse> observable = ObservableFactory.create(graphqlRequestList,
                graphqlCacheStrategy);*/

        GraphqlUseCase graphqlUseCase = new GraphqlUseCase();
        graphqlUseCase.addRequest(graphqlRequest);

        graphqlUseCase.setCacheStrategy(graphqlCacheStrategy);

        graphqlUseCase.execute(subscriber);


        /*compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));*/

    }

//    @Override
    public Observable<DomainWishlist> createObservable(RequestParams requestParams) {
        boolean isForceRefresh = requestParams.getBoolean(KEY_IS_FORCE_REFRESH, false);
        requestParams.clearValue(KEY_IS_FORCE_REFRESH);
        TKPDMapParam<String, Object> parameters = requestParams.getParameters();



        if(isForceRefresh) {
            return favoriteRepository.getFreshWishlist(parameters);
        }
        else {
            return favoriteRepository.getWishlist(parameters);
        }
    }

    public static RequestParams getDefaultParams() {
        RequestParams param = RequestParams.create();
        param.putString(KEY_PAGE, DEFAULT_PAGE_VALUE);
        param.putString(KEY_COUNT,DEFAULT_COUNT_VALUE);
        return param;
    }
}
