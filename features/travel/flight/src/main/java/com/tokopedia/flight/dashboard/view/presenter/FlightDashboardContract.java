package com.tokopedia.flight.dashboard.view.presenter;

import androidx.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel;
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel;
import com.tokopedia.flight.airport.view.model.FlightAirportModel;
import com.tokopedia.flight.dashboard.view.fragment.model.FlightClassModel;
import com.tokopedia.flight.dashboard.view.fragment.model.FlightDashboardModel;
import com.tokopedia.flight.dashboard.view.fragment.model.FlightDashboardPassDataModel;
import com.tokopedia.flight.dashboard.view.fragment.model.FlightPassengerModel;

import java.util.Date;
import java.util.List;

/**
 * Created by alvarisi on 10/30/17.
 */

public interface FlightDashboardContract {
    interface View extends CustomerView {

        void renderSingleTripView();

        void renderRoundTripView();

        FlightDashboardModel getCurrentDashboardViewModel();

        void showDepartureDatePickerDialog(Date selectedDate, Date minDate, Date maxDate);

        void setDashBoardViewModel(FlightDashboardModel viewModel);

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

        void navigateToSearchPage(FlightDashboardModel currentDashboardViewModel);

        void showAirportShouldDifferentCity(@StringRes int resId);

        void renderBannerView(List<TravelCollectiveBannerModel.Banner> bannerList);

        void hideBannerView();

        void renderTickerView(TravelTickerModel travelTickerModel);

        String getScreenName();

        boolean isFromApplink();

        String getTripArguments();

        String getAdultPassengerArguments();

        String getChildPassengerArguments();

        String getInfantPassengerArguments();

        String getClassArguments();

        Boolean isAutoSearch();

        FlightDashboardPassDataModel getDashboardPassData();

        void setDashboardPassData(FlightDashboardPassDataModel flightDashboardPassDataViewModel);

        void hideProgressBar();

        void showFormContainer();

        void showDepartureCalendarDatePicker(Date selectedDate, Date minDate, Date time);

        void showReturnCalendarDatePicker(Date selectedDate, Date minDate, Date maxDate);

        void stopTrace();
    }

    interface Presenter extends CustomerPresenter<View> {

        void sendAnalyticsOpenScreen(String screenName);

        void onSingleTripChecked();

        void onRoundTripChecked();

        void initialize();

        void initializeOnResume();

        void onReverseAirportButtonClicked();

        void onDepartureDateButtonClicked();

        void onDepartureDateChange(int year, int month, int dayOfMonth, boolean showError);

        void onReturnDateButtonClicked();

        void onReturnDateChange(int year, int month, int dayOfMonth, boolean showError);

        void onFlightClassesChange(FlightClassModel viewModel);

        void onFlightPassengerChange(FlightPassengerModel passengerViewModel);

        void onDepartureAirportChange(FlightAirportModel departureAirport);

        void onArrivalAirportChange(FlightAirportModel arrivalAirport);

        void onSearchTicketButtonClicked();

        void onDestroyView();

        void onBannerItemClick(int position, TravelCollectiveBannerModel.Banner bannerDetail);

        void getBannerData(String query);

        void actionOnPromoScrolled(int position, TravelCollectiveBannerModel.Banner bannerData);

        void fetchTickerData();
    }
}
