package com.tokopedia.topads.common.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.tokopedia.topads.common.constant.TopAdsConstant;
import com.tokopedia.topads.common.constant.TopAdsSourceOption;

/**
 * Created by hadi.putra on 16/04/18.
 */

public class TopAdsSourceTracking {

    private SharedPreferences preferences;

    public TopAdsSourceTracking(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public TopAdsSourceTracking(Context context, String key) {
        this.preferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
    }

    public void savingSource(@TopAdsSourceOption String source){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TopAdsConstant.KEY_TAGGING_SOURCE, source);
        editor.apply();

    }

    public void deleteSource(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().apply();
    }

    public String getSource(){
        return preferences.getString(TopAdsConstant.KEY_TAGGING_SOURCE, null);
    }
}
