package com.tokopedia.tkpd.deeplink.data.source;


import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.product.manage.item.common.data.source.cloud.DataResponse;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by okasurya on 1/5/18.
 */

public interface ShopInfoSource {
    Observable<Response<DataResponse<ShopModel>>> getShopInfo(RequestParams requestParams);
}
