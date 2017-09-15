package com.tokopedia.posapp.react.datasource.cache;

import com.google.gson.Gson;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.posapp.database.model.CartDb_Table;
import com.tokopedia.posapp.database.manager.CartDbManager;
import com.tokopedia.posapp.database.QueryParameter;
import com.tokopedia.posapp.domain.model.cart.CartDomain;
import com.tokopedia.posapp.react.datasource.model.CacheResult;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 8/28/17.
 */

public class ReactCartCacheSource implements ReactCacheSource {
    CartDbManager cartDbManager;
    Gson gson;

    public ReactCartCacheSource() {
        this.cartDbManager = new CartDbManager();
        this.gson = new Gson();
    }

    @Override
    public Observable<String> getData(String productId) {
        return Observable.just(productId)
                .map(getDataById());
    }

    @Override
    public Observable<String> getListData(int offset, int limit) {
        QueryParameter param = new QueryParameter();
        param.setOffset(offset);
        param.setLimit(limit);

        return Observable.just(param)
                .map(getList());
    }

    @Override
    public Observable<String> getAllData() {
        return Observable.just(true)
                .map(getMockData());
    }

    private Func1<String, String> getDataById() {
        return new Func1<String, String>() {
            @Override
            public String call(String productId) {
                CartDomain cartDomain = cartDbManager.first(
                        ConditionGroup.clause().and(CartDb_Table.productId.eq(productId))
                );
                CacheResult<CartDomain> result = new CacheResult<>();
                result.data = cartDomain;
                return gson.toJson(result);
            }
        };
    }

    private Func1<QueryParameter, String> getList() {
        return new Func1<QueryParameter, String>() {
            @Override
            public String call(QueryParameter queryParameter) {
                List<CartDomain> cartDomains = cartDbManager.getListData(
                        queryParameter.getOffset(), queryParameter.getLimit()
                );
                CacheResult<CartDomain> result = new CacheResult<>();
                result.datas = new ArrayList<>();
                result.datas.addAll(cartDomains);
                return gson.toJson(result);
            }
        };
    }

    private Func1<Boolean, String> getAll() {
        return new Func1<Boolean, String>() {
            @Override
            public String call(Boolean aBoolean) {
                List<CartDomain> cartDBList = cartDbManager.getAllData();
                CacheResult<CartDomain> result = new CacheResult<>();
                result.datas = new ArrayList<>();
                result.datas.addAll(cartDBList);
                return gson.toJson(result);
            }
        };
    }

    public Func1<? super Boolean, ? extends String> getMockData() {
        return new Func1<Boolean, String>() {
            @Override
            public String call(Boolean aBoolean) {
                CacheResult<ListResponse<CartResponse>> result = new CacheResult<>();

                ListResponse<CartResponse> list = new ListResponse<>();
                List<CartResponse> cartResponses = new ArrayList<>();
                CartResponse cartResponse = new CartResponse();
                cartResponse.setProductId("160551106");
                cartResponse.setPrice(20000);
                cartResponse.setProductName("Lovana Bodylotion Milky Cupcake 250ml");
                cartResponse.setQuantity(1);
                cartResponse.setImageUrl("https://ecs7.tokopedia.net/img/cache/200-square/product-1/2017/3/17/17437275/17437275_1878b3d2-ce9c-4ab3-b345-64da204ec935.jpg");
                cartResponses.add(cartResponse);

                cartResponse = new CartResponse();
                cartResponse.setProductId("160533448");
                cartResponse.setPrice(10000);
                cartResponse.setProductName("Happy Urang Aring 55ml");
                cartResponse.setQuantity(2);
                cartResponse.setImageUrl("https://ecs7.tokopedia.net/img/cache/200-square/product-1/2017/4/28/160533448/160533448_8ee45562-709b-4da1-8505-355282ac5459_1000_1000.jpg");
                cartResponses.add(cartResponse);

                cartResponse = new CartResponse();
                cartResponse.setProductId("193938857");
                cartResponse.setPrice(30000);
                cartResponse.setProductName("Oh Man! Baby Pomade Nutri Green 45gr");
                cartResponse.setQuantity(3);
                cartResponse.setImageUrl("https://ecs7.tokopedia.net/img/cache/200-square/product-1/2017/8/10/193938857/193938857_022ba5db-40b1-4ca2-b460-aed833272f5b_1000_1000.jpg");
                cartResponses.add(cartResponse);

                list.setList(cartResponses);
                result.data = list;

                return gson.toJson(result);
            }
        };
    }
}
