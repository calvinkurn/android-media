package com.tokopedia.flight.detail.presenter;

import android.app.Activity;

import androidx.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.common.travel.data.entity.TravelCrossSelling;
import com.tokopedia.flight.detail.view.model.FlightDetailOrderJourney;
import com.tokopedia.flight.detail.view.model.FlightDetailPassenger;
import com.tokopedia.flight.detail.view.model.SimpleModel;
import com.tokopedia.flight.orderlist.domain.model.FlightInsurance;
import com.tokopedia.flight.orderlist.domain.model.FlightOrder;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightCancellationJourney;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderDetailPassData;

import java.util.List;

/**
 * Created by zulfikarrahman on 12/13/17.
 */

public interface FlightDetailOrderContract {
    interface View extends CustomerView {
        void showProgressDialog();

        void hideProgressDialog();

        void onErrorGetOrderDetail(Throwable e);

        void updateFlightList(List<FlightDetailOrderJourney> journeys);

        void updatePassengerList(List<FlightDetailPassenger> flightDetailPassengers);

        void updatePrice(List<SimpleModel> priceList, String totalPrice);

        void updateOrderData(String eTicketLink, String invoiceLink, String cancelUrl);

        String getString(int id, Object... args);

        void updateViewStatus(String orderStatusString, int color, boolean isTicketVisible, boolean isScheduleVisible,
                              boolean isCancelVisible, boolean isReorderVisible);

        Activity getActivity();

        String getCancelMessage();

        void navigateToWebview(String url);

        void navigateToFlightHomePage();

        void renderFlightOrder(FlightOrder flightOrder);

        FlightOrder getFlightOrder();

        void navigateToCancellationPage(String invoiceId, List<FlightCancellationJourney> items);

        void showPaymentInfoLayout();

        void hidePaymentInfoLayout();

        void setPaymentLabel(@StringRes int resId);

        void setPaymentDescription(CharSequence description);

        void setTotalTransfer(String price);

        void hideTotalTransfer();

        void setPaymentDueDate(String dueDate);

        void hidePaymentDueDate();

        void showLihatEticket();

        void hideLihatEticket();

        void showCancellationStatus(String status);

        void showCancellationContainer();

        void hideCancellationContainer();

        void hideCancelButton();

        void navigateToInputEmailForm(String userId, String userEmail);

        void setTransactionDate(String transactionDate);

        void showRefundableCancelDialog(String id, List<FlightCancellationJourney> items);

        void showNonRefundableCancelDialog(String id, List<FlightCancellationJourney> items);

        void hideInsuranceLayout();

        void showInsuranceLayout();

        void renderInsurances(List<FlightInsurance> insurances);

        void checkIfShouldGoToCancellation();

        void showCrossSellingItems(TravelCrossSelling travelCrossSelling);

        void hideCrossSellingItems();

    }

    interface Presenter extends CustomerPresenter<View> {
        void getDetail(String orderId, FlightOrderDetailPassData flightOrderDetailPassData);

        void getCrossSellingItems(String orderId, String crossSellingQuery);

        void actionCancelOrderButtonClicked();

        void actionCancelOrderWithoutDialog();

        void onHelpButtonClicked(String contactUsUrl);

        void actionReorderButtonClicked();

        void onSendEticketButtonClicked();

        void onGetProfileData();

        List<FlightCancellationJourney> transformOrderToCancellation(List<FlightOrderJourney> flightOrderJourneyList);

        void onMoreAirlineInfoClicked();
    }
}
