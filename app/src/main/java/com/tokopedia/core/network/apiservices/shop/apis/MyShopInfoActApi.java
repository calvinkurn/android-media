package com.tokopedia.core.network.apiservices.shop.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.shop.model.UpdateShopImageModel;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public interface MyShopInfoActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_UPDATE_SHOP_INFO)
    Observable<Response<TkpdResponse>> updateInfo(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_UPDATE_SHOP_PICTURE)
    Observable<Response<TkpdResponse>> updatePicture(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_UPDATE_SHOP_PICTURE)
    Observable<UpdateShopImageModel> updatePictureNew(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_UPDATE_SHOP_CLOSE)
    Observable<Response<TkpdResponse>> updateShopClose(@FieldMap Map<String, String> params);
}
