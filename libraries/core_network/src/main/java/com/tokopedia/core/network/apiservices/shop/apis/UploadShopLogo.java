package com.tokopedia.core.network.apiservices.shop.apis;


import com.tokopedia.core.shop.model.OpenShopPictureModel;
import com.tokopedia.core.shop.model.openShopValidationData.OpenShopValidationData;
import com.tokopedia.core.shop.model.uploadShopLogoData.UploadShopLogoData;

import okhttp3.RequestBody;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author Toped18 on 5/24/2016.
 */

@Deprecated
public interface UploadShopLogo {

    @Multipart
    @POST("")
    Observable<UploadShopLogoData> uploadShopLogoV4(
            @Url String url,
            @Header("Content-MD5") String contentMD5,// 1
            @Header("Date") String date,// 2
            @Header("Authorization") String authorization, // 3
            @Header("X-Method") String xMethod,// 4
            @Part("user_id") RequestBody userId,// 5
            @Part("device_id") RequestBody deviceId, // 6
            @Part("hash") RequestBody hash,// 7
            @Part("device_time") RequestBody deviceTime,// 8
            @Part("logo\"; filename=\"image.jpg") RequestBody imageFile, // "; filename="image.jpg"
            @Part("new_add") RequestBody newAdd,
            @Part("resolution") RequestBody resolution,
            @Part("server_id") RequestBody serverId
    );

    @Multipart
    @POST("")
    Observable<OpenShopValidationData> createShopValidation(
            @Url String url,
            @Header("Content-MD5") String contentMD5,// a
            @Header("Date") String date,// b
            @Header("Authorization") String authorization, // c
            @Header("X-Method") String xMethod,// d
            @Part("i_paket") RequestBody iPaket,// 1
            @Part("jne_diff_district") RequestBody jneDiffDistrict, // 2
            @Part("jne_fee") RequestBody jneFee,// 3
            @Part("jne_fee_value") RequestBody jneFreeValue,// 4
            @Part("jne_min_weight") RequestBody jneMinWeight, // 5
            @Part("jne_min_weight_value") RequestBody jneMinWeightValue, // 6
            @Part("jne_tiket") RequestBody jneTiket, // 7
            @Part("pos_fee") RequestBody posFee, // 8
            @Part("pos_fee_value") RequestBody posFeeValue, // 9
            @Part("pos_min_weight") RequestBody posMinWeight, // 10
            @Part("pos_min_weight_value") RequestBody posMinWeightValue, // 11
            @Part("rpx_tiket") RequestBody rpxTiket, // 12
            @Part("server_id") RequestBody serverId, //  13
            @Part("shipment_ids") RequestBody shipmentIds, // 14
            @Part("shop_courier_origin") RequestBody shopCourierOrigin, // 15
            @Part("shop_domain") RequestBody shopDomain, // 16
            @Part("shop_short_desc") RequestBody shopShortDesc, // 17
            @Part("shop_logo") RequestBody shopLogo, // 18
            @Part("shop_name") RequestBody shopName, // 19
            @Part("shop_postal") RequestBody shopPostal, //20
            @Part("shop_tag_line") RequestBody shopTagLine, // 21
            @Part("tiki_fee") RequestBody tikiFee, // 22
            @Part("tiki_fee_value") RequestBody tikiFeeValue, // 23
            @Part("payment_ids") RequestBody paymenIds // 23

    );

    @Multipart
    @POST("")
    Observable<OpenShopPictureModel> openShopPicture(
            @Url String url,
            @Header("Content-MD5") String contentMD5,// 1
            @Header("Date") String date,// 2
            @Header("Authorization") String authorization, // 3
            @Header("X-Method") String xMethod,// 4
            @Part("user_id") RequestBody userId,// 5
            @Part("device_id") RequestBody deviceId, // 6
            @Part("hash") RequestBody hash,// 7
            @Part("device_time") RequestBody deviceTime,// 8
            @Part("shop_logo") RequestBody shopLogo,
            @Part("server_id") RequestBody serverId
    );
}
