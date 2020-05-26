package com.tokopedia.topads.dashboard.data.source.cloud;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.topads.common.model.shopmodel.ShopModel;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.api.TopAdsShopApi;
import com.tokopedia.user.session.UserSession;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by hadi.putra on 03/05/18.
 */

public class ShopInfoCloud {
    private final TopAdsShopApi api;
    private final Context context;

    public static final String SHOP_ID = "shop_id";
    public static final String SHOW_ALL = "show_all";

    public ShopInfoCloud(Context context, TopAdsShopApi api) {
        this.api = api;
        this.context = context;
    }

    public Observable<Response<DataResponse<ShopModel>>> getShopInfo() {
        UserSession userSession = new UserSession(context);
        String userId = userSession.getUserId();
        String deviceId = userSession.getDeviceId();
        String shopId = userSession.getShopId();

        Map<String, String> params = new HashMap<>();
        params.put(SHOP_ID, shopId);
        params.put(SHOW_ALL, "1");
        params = AuthUtil.generateParams(userId, deviceId, params);
        return api.getShopInfo(params);
    }
}
