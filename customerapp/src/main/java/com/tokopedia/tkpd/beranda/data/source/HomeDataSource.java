package com.tokopedia.tkpd.beranda.data.source;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.beranda.data.mapper.HomeMapper;
import com.tokopedia.tkpd.beranda.data.source.api.HomeDataApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by henrypriyono on 26/01/18.
 */

public class HomeDataSource {
    private HomeDataApi homeDataApi;
    private HomeMapper homeMapper;
    private Context context;
    private GlobalCacheManager cacheManager;
    private Gson gson;

    public HomeDataSource(HomeDataApi homeDataApi,
                          HomeMapper homeMapper,
                          Context context,
                          GlobalCacheManager cacheManager,
                          Gson gson) {
        this.homeDataApi = homeDataApi;
        this.homeMapper = homeMapper;
        this.context = context;
        this.cacheManager = cacheManager;
        this.gson = gson;
    }

    private Action1<List<Visitable>> saveToCache() {
        return new Action1<List<Visitable>>() {
            @Override
            public void call(List<Visitable> itemList) {
                Type listType = new TypeToken<List<Visitable>>() {}.getType();
                cacheManager.setKey(TkpdCache.Key.HOME_DATA_CACHE);
                cacheManager.setValue(gson.toJson(itemList, listType));
                cacheManager.store();
            }
        };
    }

    public Observable<List<Visitable>> getCache() {
        return Observable.just(true).map(new Func1<Boolean, List<Visitable>>() {
            @Override
            public List<Visitable> call(Boolean aBoolean) {
                String cache = cacheManager.getValueString(TkpdCache.Key.HOME_DATA_CACHE);
                if (cache != null) {
                    Type listType = new TypeToken<List<Visitable>>() {}.getType();
                    return gson.fromJson(cache, listType);
                }
                throw new RuntimeException("Cache is empty!!");
            }
        });
    }

    public Observable<List<Visitable>> getHomeData() {
        return homeDataApi.getHomeData(getRequestPayload()).map(homeMapper);
    }

    private String getRequestPayload() {
        return loadRawString(context.getResources(), R.raw.home_data_query);
    }

    private String loadRawString(Resources resources, int resId) {
        InputStream rawResource = resources.openRawResource(resId);
        String content = streamToString(rawResource);
        try {rawResource.close();} catch (IOException e) {}
        return content;
    }

    private String streamToString(InputStream in) {
        String temp;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuilder.append(temp + "\n");
            }
        } catch (IOException e) {}
        return stringBuilder.toString();
    }
}
