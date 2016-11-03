package com.tokopedia.tkpd.network.apiservices.shop.apis;

import com.tokopedia.tkpd.shop.model.OpenShopPictureModel;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author sebastianuskh on 9/28/16.
 */
public interface OpenShopPicture {

    @FormUrlEncoded
    @POST("open_shop_picture.pl")
    Observable<OpenShopPictureModel> openShopPicture(@FieldMap Map<String, String> params);
}
