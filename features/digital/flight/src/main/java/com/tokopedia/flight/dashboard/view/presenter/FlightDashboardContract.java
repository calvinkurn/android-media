package com.tokopedia.flight.dashboard.view.presenter;

import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel;
import com.tokopedia.flight.banner.data.source.cloud.model.BannerDetail;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightClassViewModel;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightDashboardPassDataViewModel;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightDashboardViewModel;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;

import java.util.Date;
import java.util.List;

/**
 * Created by alvarisi on 10/30/17.
 */

public interface FlightDashboardContract {
    interface View extends CustomerView {

        void renderSingleTripView();

        void renderRoundTripView();

        FlightDashboardViewModel getCurrentDashboardViewModel();

        void showDepartureDatePickerDialog(Date selectedDate, Date minDate, Date maxDate);

        void setDashBoardViewModel(FlightDashboardViewModel viewModel);

        void showReturnDatePickerDialog(Date selectedDate, Date minDate, Date maxDate);

        CharSequence getString(@StringRes int resID);

        void showDepartureEmptyErrorMessage(@StringRes int resId);

        void showArrivalEmptyErrorMessage(@StringRes int resId);

        void showArrivalAndDestinationAreSameError(@StringRes int resID);

        void showDepartureDateShouldAtLeastToday(@StringRes int resID);

        void showDepartureDateMaxTwoYears(@StringRes int resID);

        void showReturnDateShouldGreaterOrEqual(@StringRes int resId);

        void showReturnDateMaxTwoYears(@StringRes int resId);

        void showPassengerAtLeastOneAdult(@StringRes int resId);

        void showFlightClassPassengerIsEmpty(@StringRes int resId);

        void showApplinkErrorMessage(@StringRes int resId);

        void navigateToSearchPage(FlightDashboardViewModel currentDashboardViewModel);

        void showAirportShouldDifferentCity(@StringRes int resId);

        void navigateToLoginPage();

        void closePage();

        void renderBannerView(List<BannerDetail> bannerList);

        void hideBannerView();

        String getScreenName();

        boolean isFromApplink();

        String getTripArguments();

        String getAdultPassengerArguments();

        String getChildPassengerArguments();

        String getInfantPassengerArguments();

        String getClassArguments();

        FlightDashboardPassDataViewModel getDashboardPassData();

        void setDashboardPassData(FlightDashboardPassDataViewModel flightDashboardPassDataViewModel);

        void hideProgressBar();

        void showFormContainer();

        void startAirportSyncInBackground(long airportVersion);

        void showDepartureCalendarDatePicker(Date selectedDate, Date minDate, Date time);

        void showReturnCalendarDatePicker(Date selectedDate, Date minDate, Date maxDate);

        void stopTrace();
    }

    interface Presenter extends CustomerPresenter<View> {

        void onSingleTripChecked();

        void onRoundTripChecked();

        void initialize();

        void onReverseAirportButtonClicked();

        void onDepartureDateButtonClicked();

        void onDepartureDateChange(int year, int month, int dayOfMonth, boolean showError);

        void onReturnDateButtonClicked();

        void onReturnDateChange(int year, int month, int dayOfMonth, boolean showError);

        void onFlightClassesChange(FlightClassViewModel viewModel);

        void onFlightPassengerChange(FlightPassengerViewModel passengerViewModel);

        void onDepartureAirportChange(FlightAirportViewModel departureAirport);

        void onArrivalAirportChange(FlightAirportViewModel arrivalAirport);

        void onSearchTicketButtonClicked();

        void onDestroyView();

        void onLoginResultReceived();

        void onBannerItemClick(int position, BannerDetail bannerDetail);

        void actionOnPromoScrolled(int position, BannerDetail bannerData);
    }
}
