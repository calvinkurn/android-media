package com.tokopedia.tkpd.network.apiservices.product.apis;

import com.tokopedia.tkpd.myproduct.model.AddProductWithoutImageModel;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.tkpd.myproduct.model.EditProductPictureModel;
import com.tokopedia.tkpd.myproduct.model.ProductSubmitModel;
import com.tokopedia.tkpd.myproduct.model.ProductValidationModel;
import com.tokopedia.tkpd.selling.orderReject.model.ResponseEditDescription;
import com.tokopedia.tkpd.selling.orderReject.model.ResponseEditPrice;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * ProductActApi
 * Created by Angga.Prasetiyo on 04/12/2015.
 */
public interface ProductActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_ADD_PRODUCT_SUBMIT)
    Observable<ProductSubmitModel> addSubmit(@FieldMap Map<String, String> params);

	@FormUrlEncoded
    @POST("/v4/action/product/add_product_submit.pl")
    Observable<ProductSubmitModel> addSubmit(
            @Header("Content-MD5") String contentMD5,// 1
            @Header("Date") String date,// 2
            @Header("Authorization") String authorization, // 3
            @Header("X-Method") String xMethod,// 4
            @Field("user_id") String userId,// 5
            @Field("device_id") String deviceId, // 6
            @Field("hash") String hash,// 7
            @Field("device_time") String deviceTime,// 8
            @Field("click_name") String clickName,
            @Field("file_uploaded") String fileUploaded,
            @Field("post_key") String postKey,
            @Field("product_etalase_id") String productEtalaseId,
            @Field("product_etalase_name") String productEtalaseName,
            @Field("product_upload_to") String productUploadTo
    );

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_ADD_VALIDATION)
    Observable<ProductValidationModel> addValidation(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/v4/action/product/add_product_validation.pl")
    Observable<AddProductWithoutImageModel> addValidationWithoutImage(
            @FieldMap Map<String, String> params
    );


    @FormUrlEncoded
    @POST("/v4/action/product/add_product_validation.pl")
    Observable<AddProductWithoutImageModel> addValidationWithoutImage(
            @Header("Content-MD5") String contentMD5,// 1
            @Header("Date") String date,// 2
            @Header("Authorization") String authorization, // 3
            @Header("X-Method") String xMethod,// 4
            @Field("user_id") String userId,// 5
            @Field("device_id") String deviceId, // 6
            @Field("hash") String hash,// 7
            @Field("device_time") String deviceTime,// 8
            @Field("click_name") String click_name,// 9
            @Field("duplicate") String duplicate,// 10
            @Field("product_catalog_id") String productCatalogId, // 11
            @Field("product_condition") String productCondition,// 12
            @Field("product_department_id") String productDepartmentId, // 13
            @Field("product_description") String productDescription,// 14
            @Field("product_etalase_id") String productEtalaseId,// 15
            @Field("product_etalase_name") String productEtalaseName, // 16
            @Field("product_min_order") String productMinOrder,// 17
            @Field("product_must_insurance") String productMustInsurance,// 18
            @Field("product_name") String productName,// 19
            @Field("product_price") String productPrice,// 23
            @Field("product_price_currency") String productPriceCurrency,//24
            @Field("product_returnable") String productReturnable,// 25
            @Field("product_upload_to") String productUploadTo,// 26
            @Field("product_weight") String productWeight,// 27
            @Field("product_weight_unit") String productWeightUnit,// 28
            @Field("prd_prc_1") String prdPrc1,// 29
            @Field("prd_prc_2") String prdPrc2,// 30
            @Field("prd_prc_3") String prdPrc3,// 31
            @Field("prd_prc_4") String prdPrc4,// 32
            @Field("prd_prc_5") String prdPrc5,// 33
            @Field("qty_max_1") String qtyMax1,// 34
            @Field("qty_max_2") String qtyMax2,// 35
            @Field("qty_max_3") String qtyMax3,// 36
            @Field("qty_max_4") String qtyMax4,// 37
            @Field("qty_max_5") String qtyMax5,// 38
            @Field("qty_min_1") String qtyMin1,// 39
            @Field("qty_min_2") String qtyMin2,// 40
            @Field("qty_min_3") String qtyMin3,// 41
            @Field("qty_min_4") String qtyMin4,// 42
            @Field("qty_min_5") String qtyMin5,// 43
            @Field("po_process_type") String poProcessType,// 44
            @Field("po_process_value") String poProcessValue// 45
    );


	@FormUrlEncoded
    @POST("/v4/action/product/add_product_validation.pl")
    Observable<ProductValidationModel> addValidation(
            @Header("Content-MD5") String contentMD5,// 1
            @Header("Date") String date,// 2
            @Header("Authorization") String authorization, // 3
            @Header("X-Method") String xMethod,// 4
            @Field("user_id") String userId,// 5
            @Field("device_id") String deviceId, // 6
            @Field("hash") String hash,// 7
            @Field("device_time") String deviceTime,// 8
            @Field("click_name") String click_name,// 9
            @Field("duplicate") String duplicate,// 10
            @Field("product_catalog_id") String productCatalogId, // 11
            @Field("product_condition") String productCondition,// 12
            @Field("product_department_id") String productDepartmentId, // 13
            @Field("product_description") String productDescription,// 14
            @Field("product_etalase_id") String productEtalaseId,// 15
            @Field("product_etalase_name") String productEtalaseName, // 16
            @Field("product_min_order") String productMinOrder,// 17
            @Field("product_must_insurance") String productMustInsurance,// 18
            @Field("product_name") String productName,// 19
            @Field("product_photo") String productPhoto,// 20
            @Field("product_photo_default") String productPhotoDefault,// 21
            @Field("product_photo_desc") String productPhotoDesc,// 22
            @Field("product_price") String productPrice,// 23
            @Field("product_price_currency") String productPriceCurrency,//24
            @Field("product_returnable") String productReturnable,// 25
            @Field("product_upload_to") String productUploadTo,// 26
            @Field("product_weight") String productWeight,// 27
            @Field("product_weight_unit") String productWeightUnit,// 28
            @Field("prd_prc_1") String prdPrc1,// 29
            @Field("prd_prc_2") String prdPrc2,// 30
            @Field("prd_prc_3") String prdPrc3,// 31
            @Field("prd_prc_4") String prdPrc4,// 32
            @Field("prd_prc_5") String prdPrc5,// 33
            @Field("qty_max_1") String qtyMax1,// 34
            @Field("qty_max_2") String qtyMax2,// 35
            @Field("qty_max_3") String qtyMax3,// 36
            @Field("qty_max_4") String qtyMax4,// 37
            @Field("qty_max_5") String qtyMax5,// 38
            @Field("qty_min_1") String qtyMin1,// 39
            @Field("qty_min_2") String qtyMin2,// 40
            @Field("qty_min_3") String qtyMin3,// 41
            @Field("qty_min_4") String qtyMin4,// 42
            @Field("qty_min_5") String qtyMin5,// 43
            @Field("po_process_type") String poProcessType,// 44
            @Field("po_process_value") String poProcessValue,// 45
            @Field("server_id") String serverId// 46
    );

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_DELETE_PRODUCT)
    Observable<Response<TkpdResponse>> delete(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_DELETE_PICTURE)
    Observable<Response<TkpdResponse>> deletePicture(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_EDIT_CATEGORY)
    Observable<Response<TkpdResponse>> editCategory(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_EDIT_ETALASE)
    Observable<Response<TkpdResponse>> editEtalase(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_EDIT_INSURANCE)
    Observable<Response<TkpdResponse>> editInsurance(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_EDIT_PRICE)
    Observable<Response<TkpdResponse>> editPrice(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_EDIT_PRODUCT)
    Observable<Response<TkpdResponse>> edit(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_EDIT_PICTURE)
    Observable<Response<TkpdResponse>> editPicture(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("v4/action/product/"+TkpdBaseURL.Product.PATH_EDIT_PICTURE)
    Observable<EditProductPictureModel> editPicture(
            @Header(NetworkCalculator.CONTENT_MD5) String contentMD5,// 1
            @Header(NetworkCalculator.DATE) String date,// 2
            @Header(NetworkCalculator.AUTHORIZATION) String authorization, // 3
            @Header(NetworkCalculator.X_METHOD) String xMethod,// 4
            @Field(NetworkCalculator.USER_ID) String userId,// 5
            @Field(NetworkCalculator.DEVICE_ID) String deviceId, // 6
            @Field(NetworkCalculator.HASH) String hash,// 7
            @Field(NetworkCalculator.DEVICE_TIME) String deviceTime,// 8
            @Field("pic_obj") String picObj
     );

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_EDIT_RETURNABLE)
    Observable<Response<TkpdResponse>> editReturnable(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_MOVE_TO_WAREHOUSE)
    Observable<Response<TkpdResponse>> moveToWarehouse(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_PROMOTE_PRODUCT)
    Observable<Response<TkpdResponse>> promote(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_REPORT_PRODUCT)
    Observable<Response<TkpdResponse>> report(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_EDIT_DESCRIPTION)
    Observable<ResponseEditDescription> editDescription(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_EDIT_WEIGHT_PRICE)
    Observable<ResponseEditPrice> editWeightPrice(@FieldMap Map<String, String> params);
}
