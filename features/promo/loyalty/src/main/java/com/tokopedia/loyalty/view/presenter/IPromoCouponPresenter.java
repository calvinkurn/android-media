package com.tokopedia.loyalty.view.presenter;

import android.app.Activity;

import com.google.gson.JsonObject;
import com.tokopedia.loyalty.view.data.CouponData;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public interface IPromoCouponPresenter {

    String VOUCHER_CODE = "voucher_code";

    String CATEGORY_ID = "category_id";

    void processGetCouponList(String platform);

    void processGetEventCouponList(int categoryId, int productId);

    void processCheckMarketPlaceCartListPromoCode(Activity activity, CouponData couponData,
                                                  String paramUpdateCart);

    void submitDigitalVoucher(CouponData couponData, String categoryId);

    void submitTrainVoucher(CouponData couponData, String reservationId, String reservationCode);

    void submitEventVoucher(CouponData couponData, JsonObject requestBody, boolean flag);

    void parseAndSubmitEventVoucher(String json, CouponData couponData, String platform);

    void detachView();

    void submitFlightVoucher(CouponData data, String cartId);

    void submitDealVoucher(CouponData couponData, JsonObject requestBody, boolean flag);
}