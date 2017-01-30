package com.tokopedia.tkpd.home.feed.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.base.common.service.ServiceVersion4;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.home.feed.data.mapper.GetShopIdMapperResult;
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

    private Context mContext;
    private final ServiceVersion4 mServiceVersion4;
    private final GetShopIdMapperResult mGetShopIdMapperResult;

    public CloudFavoriteShopIdDataSource(Context context, ServiceVersion4 serviceVersion4,
                                         GetShopIdMapperResult getShopIdMapperResult) {
        mContext = context;

        mServiceVersion4 = serviceVersion4;
        mGetShopIdMapperResult = getShopIdMapperResult;
    }

    @Override
    public Observable<List<String>> getListShopId() {
        TKPDMapParam<String, String> noLimitParams = new TKPDMapParam<>();
        noLimitParams.put(KEY_LIMIT, VALUE_LIMIT_DEFAULT);
        HashMap<String, String> listShopIdParams
                = AuthUtil.generateParamsNetwork(mContext, noLimitParams);

        return mServiceVersion4
                .getListFaveShopId(listShopIdParams)
                .map(mGetShopIdMapperResult);
    }

}
