package com.tokopedia.posapp.react.datasource.cache;

import com.google.gson.Gson;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.core.database.model.CartDB;
import com.tokopedia.core.database.model.CartDB_Table;
import com.tokopedia.posapp.database.manager.CartDbManager;
import com.tokopedia.posapp.database.QueryParameter;
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
                .map(getAll());
    }

    private Func1<String, String> getDataById() {
        return new Func1<String, String>() {
            @Override
            public String call(String productId) {
                CartDB cart = cartDbManager.first(
                        ConditionGroup.clause().and(CartDB_Table.productId.eq(productId))
                );
                CacheResult<CartDB> result = new CacheResult<>();
                result.data = cart;
                return gson.toJson(result);
            }
        };
    }

    private Func1<QueryParameter, String> getList() {
        return new Func1<QueryParameter, String>() {
            @Override
            public String call(QueryParameter queryParameter) {
                List<CartDB> cartDBList = cartDbManager.getListData(
                        queryParameter.getOffset(), queryParameter.getLimit()
                );
                CacheResult<CartDB> result = new CacheResult<>();
                result.datas = new ArrayList<>();
                result.datas.addAll(cartDBList);
                return gson.toJson(result);
            }
        };
    }

    private Func1<Boolean, String> getAll() {
        return new Func1<Boolean, String>() {
            @Override
            public String call(Boolean aBoolean) {
                List<CartDB> cartDBList = cartDbManager.getAllData();
                CacheResult<CartDB> result = new CacheResult<>();
                result.datas = new ArrayList<>();
                result.datas.addAll(cartDBList);
                return gson.toJson(result);
            }
        };
    }
}
