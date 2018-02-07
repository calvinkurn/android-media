package com.tokopedia.tkpd.deeplink.data.repository;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.BuildConfig;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.deeplink.Whitelist;
import com.tokopedia.tkpd.deeplink.WhitelistItem;

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
    private GlobalCacheManager globalCacheManager;

    public DeeplinkRepositoryImpl(Context context, GlobalCacheManager globalCacheManager) {
        this.context = context;
        this.globalCacheManager = globalCacheManager;
    }

    @Override
    public Observable<List<WhitelistItem>> mapUrl() {
        return Observable.concat(getWhitelistFromDB(), getWhitelistFromFile())
                .first(new Func1<List<WhitelistItem>, Boolean>() {
                    @Override
                    public Boolean call(List<WhitelistItem> whitelistItems) {
                        return whitelistItems != null && !whitelistItems.isEmpty();
                    }
                });
    }

    private Observable<List<WhitelistItem>> getWhitelistFromDB() {
        return Observable.just(new GlobalCacheManager())
                .map(new Func1<GlobalCacheManager, List<WhitelistItem>>() {
                    @Override
                    public List<WhitelistItem> call(GlobalCacheManager globalCacheManager) {
                        if (!TextUtils.isEmpty(globalCacheManager.getValueString(KEY_VERSION))) {
                            if (Integer.valueOf(globalCacheManager.getValueString(KEY_VERSION)) <=
                                    BuildConfig.VERSION_CODE) {
                                return new ArrayList<>();
                            } else {
                                String cache = globalCacheManager.getValueString(KEY_MAPPING);
                                Gson gson = new Gson();
                                Whitelist whitelist = gson.fromJson(cache, Whitelist.class);
                                return whitelist.data;
                            }
                        } else {
                            return new ArrayList<>();
                        }
                    }
                })
                .onErrorReturn(new Func1<Throwable, List<WhitelistItem>>() {
                    @Override
                    public List<WhitelistItem> call(Throwable throwable) {
                        return new ArrayList<>();
                    }
                });
    }

    private Observable<List<WhitelistItem>> getWhitelistFromFile() {
        return Observable.just(true)
                .map(new Func1<Boolean, List<WhitelistItem>>() {
                    @Override
                    public List<WhitelistItem> call(Boolean bool) {
                        return readWhitelistFromFile();
                    }
                })
                .doOnNext(new Action1<List<WhitelistItem>>() {
                    @Override
                    public void call(List<WhitelistItem> whitelistItems) {
                        saveVersionToCache();
                        saveMappingToCache(whitelistItems);
                    }
                });
    }

    private void saveMappingToCache(List<WhitelistItem> whitelistItems) {
        Gson gson = new Gson();
        if (whitelistItems != null && !whitelistItems.isEmpty()) {
            globalCacheManager.setKey(KEY_MAPPING);
            Whitelist whitelist = new Whitelist();
            whitelist.data = whitelistItems;
            globalCacheManager.setValue(gson.toJson(whitelist));
        }
    }

    private void saveVersionToCache() {
        globalCacheManager.setKey(KEY_VERSION);
        Gson gson = new Gson();
        globalCacheManager.setValue(gson.toJson(BuildConfig.VERSION_CODE));
    }

    private List<WhitelistItem> readWhitelistFromFile() {
        InputStream inputStream = context.getResources().openRawResource(R.raw.whitelist);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        Gson gson = new Gson();
        Whitelist whitelist = gson.fromJson(reader, Whitelist.class);
        return whitelist.data;
    }

}
