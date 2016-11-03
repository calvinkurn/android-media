package com.tokopedia.tkpd.facade;

import android.content.Context;

import com.tokopedia.tkpd.discovery.model.TopAdsResponse;
import com.tokopedia.tkpd.network.v4.OnNetworkTimeout;
import com.tokopedia.tkpd.var.TkpdUrl;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ricoharisin on 2/13/15.
 * modified by steven & gumay.
 */
@Deprecated
public class FacadeTopAds {

    public static final String URL = TkpdUrl.GET_TOPADS;
    public Context mContext;

    public HashMap<Integer, JSONObject> mapResult = new HashMap<>();

    public static FacadeTopAds createInstancesTopAds(Context context) {
        FacadeTopAds facade = new FacadeTopAds();
        facade.mContext = context;
        return facade;
    }

    public void GetTopAds(TopAdsParamHolder param,
                          int page,
                          final OnNetworkTimeout onNetworkTimeoutListener,
                          final Callback<TopAdsResponse> callback) {
    }

    public static class TopAdsParamHolder {
        public String item;
        public String src;
        public int page;
        public String depId;
        public int hotlist;
        public String query;
        public String pmin;
        public String pmax;
        public String fshop;
        public String floc;
        public boolean wholesale;
        public String shipping;
        public boolean preorder;
    }

    public interface Callback<T> {
        public void onSuccess(T result);

        public void onError(Throwable throwable);
    }

}
