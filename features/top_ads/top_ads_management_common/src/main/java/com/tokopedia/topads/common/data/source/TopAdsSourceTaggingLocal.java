package com.tokopedia.topads.common.data.source;

import android.content.Context;
import android.content.SharedPreferences;

import com.tokopedia.topads.common.constant.TopAdsConstant;
import com.tokopedia.topads.common.data.TopAdsSourceTaggingModel;

import java.util.concurrent.Callable;

import rx.Observable;

/**
 * Created by hadi.putra on 16/04/18.
 */

public class TopAdsSourceTaggingLocal {

    private SharedPreferences preferences;

    public TopAdsSourceTaggingLocal(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public TopAdsSourceTaggingLocal(Context context, String key) {
        this.preferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
    }

    public Observable<Void> savingSource(final TopAdsSourceTaggingModel data){
        return Observable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(TopAdsConstant.KEY_TAGGING_SOURCE, data.toString());
                editor.apply();
                return null;
            }
        });
    }

    public Observable<Void> deleteSource(){
        return Observable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear().apply();
                return null;
            }
        });
    }

    public Observable<String> getSource(){
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return preferences.getString(TopAdsConstant.KEY_TAGGING_SOURCE, null);
            }
        });
    }
}
