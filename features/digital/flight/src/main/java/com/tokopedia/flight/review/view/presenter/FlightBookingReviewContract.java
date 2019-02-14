package com.tokopedia.flight.review.view.presenter;

import android.content.Context;
import android.support.annotation.StringRes;

import com.tokopedia.flight.booking.view.presenter.FlightBaseBookingContact;
import com.tokopedia.flight.booking.view.viewmodel.BaseCartData;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightInsuranceViewModel;
import com.tokopedia.flight.review.data.model.AttributesVoucher;
import com.tokopedia.flight.review.view.model.FlightBookingReviewModel;
import com.tokopedia.flight.review.view.model.FlightCheckoutViewModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 11/10/17.
 */

public interface FlightBookingReviewContract {

    interface View extends FlightBaseBookingContact.View{
//
//        void onErrorCheckVoucherCode(Throwable t);
//
//        void onSuccessCheckVoucherCode(AttributesVoucher attributesVoucher);

        void onErrorSubmitData(Throwable e);

        void onSuccessSubmitData();

        void hideProgressDialog();

        void showProgressDialog();

        void setTimeStamp(String timestamp);

        void setTotalPrice(int totalPrice);

        BaseCartData getCurrentCartData();

        FlightBookingReviewModel getCurrentBookingReviewModel();

        String getDepartureTripId();

        String getReturnTripId();

        String getIdEmpotencyKey(String s);

        boolean isRoundTrip();

        void onErrorVerifyCode(Throwable e);

        void showCheckoutLoading();

        void hideCheckoutLoading();

        void navigateToTopPay(FlightCheckoutViewModel flightCheckoutViewModel);

        void navigateToOrderList();

        void showPaymentFailedErrorMessage(@StringRes int resId);

        Context getActivity();

        void showErrorInSnackbar(String message);

        void showErrorInEmptyState(String message);

        void showVoucherContainer();

        void hideVoucherContainer();

        void setNeedToRefreshOnPassengerInfo();

        void updateFinalTotal(AttributesVoucher attributesVoucher, FlightBookingReviewModel currentBookingReviewModel);

        void renderCouponInfoData();

        void renderVoucherInfoData();

        String getComboKey();

        void stopTrace();
    }

    interface Presenter extends FlightBaseBookingContact.Presenter<View>{

        void onViewCreated();

        void verifyBooking(String promoCode, int price, int adult, String cartId,
                           List<FlightBookingPassengerViewModel> flightPassengerViewModels,
                           String contactName, String country, String email, String phone, List<FlightInsuranceViewModel> insurances);

        void onPaymentSuccess();

        void onPaymentFailed();

        void onPaymentCancelled();

        void onCancelAppliedVoucher();
    }
}
