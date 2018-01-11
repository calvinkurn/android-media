package com.tokopedia.tkpd.deeplink.data.repository;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.BuildConfig;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.deeplink.Deeplink;
import com.tokopedia.tkpd.deeplink.Response;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Rizky on 04/01/18.
 */

public class DeeplinkRepositoryImpl implements DeeplinkRepository {

    private final String KEY_MAPPING = "KEY_MAPPING";
    private final String KEY_VERSION = "KEY_VERSION";

    private Context context;

    public DeeplinkRepositoryImpl(Context context) {
        this.context = context;
    }

    @Override
    public Observable<List<Deeplink>> mapUrl() {
        return Observable.concat(getDeeplinksFromDb(), getDeeplinksFromFile())
                .first(new Func1<List<Deeplink>, Boolean>() {
                    @Override
                    public Boolean call(List<Deeplink> deeplinks) {
                        return deeplinks != null && !deeplinks.isEmpty();
                    }
                });
    }

    private Observable<List<Deeplink>> getDeeplinksFromDb() {
        return Observable.just(new GlobalCacheManager())
                .map(new Func1<GlobalCacheManager, List<Deeplink>>() {
                    @Override
                    public List<Deeplink> call(GlobalCacheManager globalCacheManager) {
                        if (Integer.valueOf(globalCacheManager.getValueString(KEY_VERSION)) <=
                                BuildConfig.VERSION_CODE) {
                            return new ArrayList<>();
                        } else {
                            String cache = globalCacheManager.getValueString(KEY_MAPPING);
                            Gson gson = new Gson();
                            Response response = gson.fromJson(cache, Response.class);
                            return response.deeplinks;
                        }
                    }
                })
                .onErrorReturn(new Func1<Throwable, List<Deeplink>>() {
                    @Override
                    public List<Deeplink> call(Throwable throwable) {
                        return new ArrayList<>();
                    }
                });
    }

    private Observable<List<Deeplink>> getDeeplinksFromFile() {
        return Observable.just(true)
                .map(new Func1<Boolean, List<Deeplink>>() {
                    @Override
                    public List<Deeplink> call(Boolean bool) {
                        return readDeeplinksFromFile();
                    }
                })
                .doOnNext(new Action1<List<Deeplink>>() {
                    @Override
                    public void call(List<Deeplink> deeplinks) {
                        saveVersionToCache();
                        saveMappingToCache(deeplinks);
                    }
                });
    }

    private void saveMappingToCache(List<Deeplink> deeplinks) {
        GlobalCacheManager mappingCache = new GlobalCacheManager();
        Gson gson = new Gson();
        if (deeplinks != null && !deeplinks.isEmpty()) {
            mappingCache.setKey(KEY_MAPPING);
            Response response = new Response();
            response.deeplinks = deeplinks;
            mappingCache.setValue(gson.toJson(response));
        }
    }

    private void saveVersionToCache() {
        GlobalCacheManager versionCache = new GlobalCacheManager();
        versionCache.setKey(KEY_VERSION);
        Gson gson = new Gson();
        versionCache.setValue(gson.toJson(BuildConfig.VERSION_CODE));
    }

    private List<Deeplink> readDeeplinksFromFile() {
        InputStream inputStream = context.getResources().openRawResource(R.raw.Whitelist);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        Gson gson = new Gson();
        Response response = gson.fromJson(reader, Response.class);
        return response.deeplinks;
    }

}
