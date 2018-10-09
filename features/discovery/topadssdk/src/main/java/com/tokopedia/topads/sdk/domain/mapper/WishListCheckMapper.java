package com.tokopedia.topads.sdk.domain.mapper;

import android.content.Context;
import android.util.Log;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.Mapper;
import com.tokopedia.topads.sdk.domain.model.PreferedCategory;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;
import com.tokopedia.topads.sdk.network.JsonResponseConverter;
import com.tokopedia.topads.sdk.network.RawHttpRequestExecutor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 7/5/18.
 */

public class WishListCheckMapper extends Mapper<TopAdsModel> {

    private static final String TAG = WishListCheckMapper.class.getSimpleName();
    private RawHttpRequestExecutor executor;
    private JsonResponseConverter converter;
    private TopAdsModel topAdsModel;

    public WishListCheckMapper(RawHttpRequestExecutor executor, TopAdsModel model) {
        this.executor = executor;
        converter = JsonResponseConverter.newInstance();
        this.topAdsModel = model;
    }

    @Override
    public TopAdsModel getModel() {
        try {
            JSONObject object = converter.convertResponse(executor.makeRequest());
            JSONArray ids = object.getJSONObject("data").getJSONArray("ids");
            if(ids!=null){
                for (int i = 0; i < ids.length(); i++) {
                    if(topAdsModel.getData().get(i).getProduct().getId()
                            .equalsIgnoreCase(ids.getString(i))){
                        topAdsModel.getData().get(i).getProduct().setWishlist(true);
                    } else {
                        topAdsModel.getData().get(i).getProduct().setWishlist(false);
                    }
                }
            }
        } catch (Exception e) {
        }
        return topAdsModel;
    }
}
