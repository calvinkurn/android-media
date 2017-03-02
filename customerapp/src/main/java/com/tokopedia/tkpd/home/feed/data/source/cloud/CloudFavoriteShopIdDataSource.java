package com.tokopedia.tkpd.home.feed.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.base.common.service.ServiceV4;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.home.feed.data.mapper.GetShopIdMapper;
import com.tokopedia.tkpd.home.feed.data.source.FavoritShopIdDataSource;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * @author Kulomady on 12/9/16.
 */
public class CloudFavoriteShopIdDataSource extends FavoritShopIdDataSource {

    private static final String KEY_LIMIT = "Limit";
    private static final String VALUE_LIMIT_DEFAULT = "";

    private Context context;
    private final ServiceV4 serviceV4;
    private final GetShopIdMapper getShopIdMapper;

    public CloudFavoriteShopIdDataSource(Context context, ServiceV4 serviceV4,
                                         GetShopIdMapper getShopIdMapper) {
        this.context = context;

        this.serviceV4 = serviceV4;
        this.getShopIdMapper = getShopIdMapper;
    }

    @Override
    public Observable<List<String>> getListShopId() {
        TKPDMapParam<String, String> noLimitParams = new TKPDMapParam<>();
        noLimitParams.put(KEY_LIMIT, VALUE_LIMIT_DEFAULT);
        HashMap<String, String> listShopIdParams
                = AuthUtil.generateParamsNetwork(context, noLimitParams);

        return serviceV4
                .getListFaveShopId(listShopIdParams)
                .map(getShopIdMapper);
    }

}
