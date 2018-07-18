package com.tokopedia.tkpd.home.favorite.data.source.cloud;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.entity.wishlist.GqlWishListDataResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.var.TkpdCache;
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

import static com.tokopedia.tkpd.home.favorite.domain.interactor.GetWishlistUsecase.KEY_COUNT;
import static com.tokopedia.tkpd.home.favorite.domain.interactor.GetWishlistUsecase.KEY_PAGE;
import static com.tokopedia.tkpd.home.presenter.WishListImpl.ITEM_COUNT;
import static com.tokopedia.tkpd.home.presenter.WishListImpl.PAGE_NO;
import static com.tokopedia.tkpd.home.presenter.WishListImpl.PARAM_USER_ID;

/**
 * @author Kulomady on 1/18/17.
 */
public class CloudWishlistDataStore {

    private Context context;
//    private MojitoService mojitoService;
//    private Gson gson;

    public CloudWishlistDataStore(Context context/*, Gson gson, MojitoService mojitoService*/) {
        this.context = context;
        /*this.gson = gson;
        this.mojitoService = mojitoService;*/
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
                new GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build();

        Observable<GraphqlResponse> observable = ObservableFactory.create(graphqlRequestList,
                graphqlCacheStrategy);

        return observable
                /*.doOnNext(HttpResponseValidator
                        .validate(new HttpResponseValidator.HttpValidationListener() {
                            @Override
                            public void OnPassValidation(GraphqlResponse response) {
                                saveResponseToCache(response);
                            }
                        }))*/
                .map(new WishlistMapper(context/*, gson*/));
    }

    private void saveResponseToCache(GraphqlResponse response) {
        new GlobalCacheManager()
                .setKey(TkpdCache.Key.WISHLIST)
                .setValue(/*response.getData()*/"")
                .store();
    }
}
