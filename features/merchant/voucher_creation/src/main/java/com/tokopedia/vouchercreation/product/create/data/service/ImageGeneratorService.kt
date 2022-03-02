package com.tokopedia.vouchercreation.product.create.data.service

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ImageGeneratorService {
    @GET("v2/preview/{sourceId}")
    suspend fun previewImage(
        @Header("Authorization") accessToken : String,
        @Path("sourceId") sourceId: String,
        @Query("platform") platform: String,
        @Query("is_public") isPublic: String,
        @Query("voucher_benefit_type") voucherBenefitType: String,
        @Query("voucher_cashback_type") voucherCashbackType: String,
        @Query("voucher_cashback_percentage") voucherCashbackPercentage: Int,
        @Query("voucher_nominal_amount") voucherNominalAmount: Int,
        @Query("voucher_nominal_symbol") voucherNominalSymbol: String,
        @Query("shop_logo") shopLogo: String,
        @Query("shop_name") shopName: String,
        @Query("voucher_code") voucherCode: String,
        @Query("voucher_start_time") voucherStartTime: String,
        @Query("voucher_finish_time") voucherFinishTime: String,
        @Query("product_count") productCount: Int,
        @Query("product_image_1") firstProductImageUrl: String,
        @Query("product_image_2") secondProductImageUrl: String,
        @Query("product_image_3") thirdProductImageUrl: String,
        @Query("audience_target") audienceTarget: String
    ): ResponseBody
}