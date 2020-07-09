package com.tokopedia.topads.dashboard.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.product.manage.item.common.data.source.cloud.DataResponse;
import com.tokopedia.product.manage.item.common.data.source.cloud.ShopApi;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by hadi.putra on 03/05/18.
 */

public class ShopInfoCloud {
    private final ShopApi api;
    private final Context context;

    public static final String SHOP_ID = "shop_id";
    public static final String SHOW_ALL = "show_all";

    public ShopInfoCloud(Context context, ShopApi api) {
        this.api = api;
        this.context = context;
    }

    public Observable<Response<DataResponse<ShopModel>>> getShopInfo() {

        UserSessionInterface userSession = new UserSession(context);
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
