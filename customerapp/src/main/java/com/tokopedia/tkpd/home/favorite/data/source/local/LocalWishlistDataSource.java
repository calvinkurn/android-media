package com.tokopedia.tkpd.home.favorite.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.core.network.entity.wishlist.GqlWishListDataResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.data.ObservableFactory;
import com.tokopedia.graphql.data.model.CacheType;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.favorite.data.mapper.WishlistMapper;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

import static com.tokopedia.tkpd.home.favorite.domain.interactor.GetWishlistUtil.KEY_COUNT;
import static com.tokopedia.tkpd.home.favorite.domain.interactor.GetWishlistUtil.KEY_PAGE;
import static com.tokopedia.tkpd.home.presenter.WishListImpl.ITEM_COUNT;
import static com.tokopedia.tkpd.home.presenter.WishListImpl.PAGE_NO;
import static com.tokopedia.tkpd.home.presenter.WishListImpl.PARAM_USER_ID;

/**
 * @author Kulomady on 2/13/17.
 */
public class LocalWishlistDataSource {

    private final Context context;

    public LocalWishlistDataSource(Context context) {
        this.context = context;
        GraphqlClient.init(context);
    }

    public Observable<DomainWishlist> getWishlist(String userId, TKPDMapParam<String, Object> param) {

        Map<String, Object> variables = new HashMap<>();

        variables.put(PARAM_USER_ID, Integer.parseInt(userId));
        variables.put(PAGE_NO, param.get(KEY_PAGE));
        variables.put(ITEM_COUNT, param.get(KEY_COUNT));

        GraphqlRequest graphqlRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.query_get_wishlist),
                GqlWishListDataResponse.class,
                variables);

        List<GraphqlRequest> graphqlRequestList = new ArrayList<>();
        graphqlRequestList.add(graphqlRequest);

        GraphqlCacheStrategy graphqlCacheStrategy =
                new GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build();

        Observable<GraphqlResponse> observable = ObservableFactory.create(graphqlRequestList,
                graphqlCacheStrategy);


        return observable
                .map(new WishlistMapper(context))
                .onErrorReturn(nullResponse());
    }

    @NonNull
    private Func1<Throwable, DomainWishlist> nullResponse() {
        return throwable -> null;
    }
}
