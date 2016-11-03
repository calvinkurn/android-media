package com.tokopedia.tkpd.payment.interactor;

import android.content.Context;

import com.tokopedia.tkpd.payment.model.responsecalculateshipping.CalculateShipping;
import com.tokopedia.tkpd.payment.model.responsecartstep1.CarStep1Data;
import com.tokopedia.tkpd.payment.model.responsecartstep2.CartStep2Data;
import com.tokopedia.tkpd.payment.model.responsecreditcardstep1.DataCredit;
import com.tokopedia.tkpd.payment.model.responsedynamicpayment.DynamicPaymentData;
import com.tokopedia.tkpd.payment.model.responsethankspayment.ThanksPaymentData;
import com.tokopedia.tkpd.payment.model.responsevoucher.VoucherCodeData;

import java.util.Map;

/**
 * @author by Angga.Prasetiyo
 *         on 19/05/2016.
 */
public interface PaymentNetInteractor {

    void getParameterDynamicPayment(Context context, Map<String, String> params,
                                    OnGetParameterDynamicPayment listener);

    void postStep1(Context context, Map<String, String> params, OnPostStep1 listener);

    void postStep2(Context context, Map<String, String> params, OnPostStep2 listener);

    void getThanksDynamicPayment(Context context, Map<String, String> params,
                                 OnGetThanksDynamicPayment listener);

    void checkVoucher(Context context, Map<String, String> params,
                      OnCheckVoucher listener);

    void updateInsurance(Context context, Map<String, String> params,
                         OnUpdateInsurance listener);

    void updateCart(Context context, Map<String, String> params,
                    OnUpdateCart listener);

    void cancelCart(Context context, Map<String, String> params,
                    OnCancelCart listener);

    void cancelProduct(Context context, Map<String, String> params,
                       OnCancelProduct listener);

    void editAddress(Context context, Map<String, String> params,
                     OnEditAddress listener);

    void calculateShipping(Context context, Map<String, String> params,
                           OnCalculateShipping listener);

    void unSubscribeObservable();

    void postStep1CreditCard(Context context,
                             Map<String, String> params, OnStep1CreditCard listener);

    void postSprintAsia(Context context,
                        Map<String, String> params, OnSprintAsia listener);

    void saveNewLocation(Context context, Map<String, String> params,
                         OnSaveNewLocation listener);

    interface OnGetParameterDynamicPayment {

        void onSuccess(DynamicPaymentData data);

        void onError(String message);

        void onNoConnection();

    }

    interface OnGetThanksDynamicPayment {

        void onSuccess(ThanksPaymentData data);

        void onError(String message);

        void onNoConnection();

    }

    interface OnPostStep1 {

        void onSuccess(CarStep1Data data);

        void onError(String message);

        void onNoConnection();

    }

    interface OnCheckVoucher {
        void onSuccess(VoucherCodeData data);

        void onError(String message);

        void onVoucherError(String message);
    }

    interface OnStep1CreditCard {

        void onSuccessVeritrans(DataCredit dataCredit);

        void onError(String message);

        void onTimeout(String message);

        void onNoConnection();

        void onSuccessSprintAsia(DataCredit dataCredit);
    }

    interface OnSprintAsia {

        void onSuccess(String htmlSource);

        void onError(String message);

        void onTimeout(String message);
    }

    interface OnPostStep2 {
        void onSuccess(CartStep2Data data);

        void onError(String message);

        void onTimeout(String message);

        void onNoConnection();
    }

    interface OnUpdateInsurance {

        void onSuccess(String message);

        void onError(String message);

        void onNoConnection();

    }

    interface OnUpdateCart {

        void onSuccess(String message);

        void onError(String message);

        void onNoConnection();

    }

    interface OnCancelCart {

        void onSuccess(String message);

        void onError(String message);

        void onNoConnection();

    }

    interface OnCancelProduct {

        void onSuccess(String message);

        void onError(String message);

        void onNoConnection();

    }

    interface OnEditAddress {

        void onSuccess(String message);

        void onError(String message);

        void onNoConnection();

    }

    interface OnCalculateShipping {

        void onSuccess(CalculateShipping calculateShipping);

        void onError(String message);

        void onNoConnection();
    }

    interface OnSaveNewLocation {

        void onSuccess(String message);

        void onError(String message);

        void onNoConnection();
    }
}
