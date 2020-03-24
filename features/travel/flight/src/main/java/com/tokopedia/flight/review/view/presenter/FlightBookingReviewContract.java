package com.tokopedia.flight.review.view.presenter;

import android.content.Context;

import androidx.annotation.StringRes;

import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerViewModel;
import com.tokopedia.flight.bookingV2.presentation.model.BaseCartData;
import com.tokopedia.flight.bookingV2.presentation.model.FlightBookingPassengerModel;
import com.tokopedia.flight.bookingV2.presentation.model.FlightInsuranceModel;
import com.tokopedia.flight.review.data.model.AttributesVoucher;
import com.tokopedia.flight.review.view.model.FlightBookingReviewModel;
import com.tokopedia.flight.review.view.model.FlightCheckoutModel;
import com.tokopedia.promocheckout.common.view.model.PromoData;

import java.util.List;

/**
 * Created by zulfikarrahman on 11/10/17.
 */

public interface FlightBookingReviewContract {

    interface View extends FlightBaseBookingContact.View {

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

        void navigateToTopPay(FlightCheckoutModel flightCheckoutViewModel);

        void navigateToOrderList();

        void showPaymentFailedErrorMessage(@StringRes int resId);

        Context getActivity();

        void showErrorInSnackbar(String message);

        void showErrorInEmptyState(String message);

        void showVoucherContainer();

        void hideVoucherContainer();

        void setNeedToRefreshOnPassengerInfo();

        void updateFinalTotal(AttributesVoucher attributesVoucher, FlightBookingReviewModel currentBookingReviewModel);

        void renderAutoApplyPromo(PromoData promoData);

        void renderTickerView(TravelTickerViewModel travelTickerViewModel);

        String getComboKey();

        void stopTrace();
    }

    interface Presenter extends FlightBaseBookingContact.Presenter<View> {

        void onViewCreated();

        void verifyBooking(String promoCode, int price, int adult, String cartId,
                           List<FlightBookingPassengerModel> flightPassengerViewModels,
                           String contactName, String country, String email, String phone, List<FlightInsuranceModel> insurances);

        void onPaymentSuccess();

        void onPaymentFailed();

        void onPaymentCancelled();

        void onCancelAppliedVoucher();

        void fetchTickerData();

    }
}
