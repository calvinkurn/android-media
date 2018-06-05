package com.tokopedia.topads.sourcetagging.data.source;

import android.content.Context;
import android.content.SharedPreferences;

import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceTaggingConstant;
import com.tokopedia.topads.sourcetagging.data.TopAdsSourceTaggingModel;

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

    public TopAdsSourceTaggingLocal(Context context) {
        this.preferences = context.getSharedPreferences(TopAdsSourceTaggingConstant.KEY_SOURCE_PREFERENCE, Context.MODE_PRIVATE);
    }

    public Observable<Void> savingSource(final TopAdsSourceTaggingModel data){
        final String formatedInput = data.getSource()+TopAdsSourceTaggingConstant.SEPARATOR+data.getTimestamp();
        return Observable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(TopAdsSourceTaggingConstant.KEY_TAGGING_SOURCE, formatedInput);
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
                return preferences.getString(TopAdsSourceTaggingConstant.KEY_TAGGING_SOURCE, null);
            }
        });
    }
}
