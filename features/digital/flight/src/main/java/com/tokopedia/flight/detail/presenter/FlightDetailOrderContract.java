package com.tokopedia.flight.detail.presenter;

import android.app.Activity;
import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.flight.booking.domain.subscriber.model.ProfileInfo;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationJourney;
import com.tokopedia.flight.orderlist.domain.model.FlightInsurance;
import com.tokopedia.flight.orderlist.domain.model.FlightOrder;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderDetailPassData;
import com.tokopedia.flight.review.view.model.FlightDetailPassenger;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 12/13/17.
 */

public interface FlightDetailOrderContract {
    interface View extends CustomerView {
        void showProgressDialog();

        void hideProgressDialog();

        void onErrorGetOrderDetail(Throwable e);

        void updateFlightList(List<FlightOrderJourney> journeys);

        void updatePassengerList(List<FlightDetailPassenger> flightDetailPassengers);

        void updatePrice(List<SimpleViewModel> priceList, String totalPrice);

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

        void showLihatETicket();

        void hideLihatETicket();

        void showCancellationStatus();

        void showCancellationStatusInProgress(int numberOfProcess);

        void showCancellationContainer();

        void hideCancellationContainer();

        void hideCancelButton();

        void navigateToInputEmailForm(String userId, String userEmail);

        Observable<ProfileInfo> getProfileObservable();

        void setTransactionDate(String transactionDate);

        void showLessThan6HoursDialog();

        void showRefundableCancelDialog(String id, List<FlightCancellationJourney> items);

        void showNonRefundableCancelDialog(String id, List<FlightCancellationJourney> items);

        void hideInsuranceLayout();

        void showInsuranceLayout();

        void renderInsurances(List<FlightInsurance> insurances);
    }

    interface Presenter extends CustomerPresenter<View> {
        void getDetail(String orderId, FlightOrderDetailPassData flightOrderDetailPassData);

        void actionCancelOrderButtonClicked();

        void onHelpButtonClicked(String contactUsUrl);

        void actionReorderButtonClicked();

        void onGetProfileData();

        void checkIfFlightCancellable(String invoiceId, List<FlightCancellationJourney> items);

        List<FlightCancellationJourney> transformOrderToCancellation(List<FlightOrderJourney> flightOrderJourneyList);

        void onMoreAirlineInfoClicked();
    }
}
