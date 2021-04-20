package com.tokopedia.review.feature.reputationhistory.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.review.feature.reputationhistory.data.source.cloud.apiservice.api.ShopApi;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * @author hendry on 4/4/17.
 */

public class ShopInfoCloud {
    private final ShopApi api;
    private final Context context;

    public static final String SHOP_ID = "shop_id";
    public static final String SHOW_ALL = "show_all";

    @Inject
    public ShopInfoCloud(@ApplicationContext Context context,
                         ShopApi api) {
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
