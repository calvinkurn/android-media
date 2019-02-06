package com.tokopedia.train.homepage.presentation.presenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.TrainRouter;
import com.tokopedia.train.common.util.TrainDateUtil;
import com.tokopedia.train.homepage.domain.GetTrainPromoUseCase;
import com.tokopedia.train.homepage.presentation.TrainHomepageCache;
import com.tokopedia.train.homepage.presentation.listener.TrainHomepageView;
import com.tokopedia.train.homepage.presentation.model.TrainHomepageViewModel;
import com.tokopedia.train.homepage.presentation.model.TrainPassengerViewModel;
import com.tokopedia.train.homepage.presentation.model.TrainPromoViewModel;
import com.tokopedia.train.homepage.presentation.model.TrainSearchPassDataViewModel;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationAndCityViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author Rizky on 21/02/18.
 */

public class TrainHomepagePresenterImpl extends BaseDaggerPresenter<TrainHomepageView> implements TrainHomepagePresenter {

    private final int DEFAULT_RANGE_OF_DEPARTURE_AND_ARRIVAL = 2;
    private final int MAX_BOOKING_DAYS_FROM_TODAY = 90;

    private TrainHomepageCache trainHomepageCache;
    private UserSession userSession;
    private GetTrainPromoUseCase getTrainPromoUseCase;
    private TrainRouter trainRouter;

    @Inject
    public TrainHomepagePresenterImpl(TrainHomepageCache trainHomepageCache,
                                      GetTrainPromoUseCase getTrainPromoUseCase,
                                      UserSession userSession, TrainRouter trainRouter) {
        this.trainHomepageCache = trainHomepageCache;
        this.getTrainPromoUseCase = getTrainPromoUseCase;
        this.userSession = userSession;
        this.trainRouter = trainRouter;
    }

    @Override
    public void singleTrip() {
        getView().getHomepageViewModel().setOneWay(true);
        getView().renderSingleTripView(getView().getHomepageViewModel());
    }

    @Override
    public void roundTrip() {
        getView().getHomepageViewModel().setOneWay(false);
        getView().renderRoundTripView(getView().getHomepageViewModel());
    }

    @Override
    public void onDepartureDateButtonClicked() {
        Date minDate = TrainDateUtil.getCurrentDate();
        Date maxDate = TrainDateUtil.addTimeToCurrentDate(Calendar.DATE, MAX_BOOKING_DAYS_FROM_TODAY);
        Date selectedDate = TrainDateUtil.stringToDate(getView().getHomepageViewModel().getDepartureDate());
        getView().showDepartureDatePickerDialog(selectedDate, minDate, maxDate);
    }

    @Override
    public void onReturnDateButtonClicked() {
        Date selectedDate = TrainDateUtil.stringToDate(getView().getHomepageViewModel().getReturnDate());
        Date minDate = TrainDateUtil.stringToDate(getView().getHomepageViewModel().getDepartureDate());
        Date maxDate = TrainDateUtil.addTimeToCurrentDate(Calendar.DATE, MAX_BOOKING_DAYS_FROM_TODAY);
        getView().showReturnDatePickerDialog(selectedDate, minDate, maxDate);
    }

    @Override
    public void onDepartureDateChange(int year, int month, int dayOfMonth) {
        Calendar now = TrainDateUtil.getCurrentCalendar();
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, month);
        now.set(Calendar.DATE, dayOfMonth);

        Date limitDate = TrainDateUtil.addTimeToCurrentDate(Calendar.DATE, MAX_BOOKING_DAYS_FROM_TODAY);
        Date newDepartureDate = now.getTime();

        if (newDepartureDate.after(limitDate)) {
            getView().showDepartureDateMax90Days(R.string.kai_homepage_departure_max_90_days_from_today_error);
        } else if (newDepartureDate.before(TrainDateUtil.getCurrentDate())) {
            getView().showDepartureDateShouldAtLeastToday(R.string.kai_homepage_departure_should_atleast_today_error);
        } else {
            String newDepartureDateStr = TrainDateUtil.dateToString(newDepartureDate, TrainDateUtil.DEFAULT_FORMAT);
            getView().getHomepageViewModel().setDepartureDate(newDepartureDateStr);
            String newDepartureDateFmtStr = TrainDateUtil.dateToString(newDepartureDate, TrainDateUtil.DEFAULT_VIEW_FORMAT);
            getView().getHomepageViewModel().setDepartureDateFmt(newDepartureDateFmtStr);
            if (!getView().getHomepageViewModel().isOneWay()) {
                Date currentReturnDate = TrainDateUtil.stringToDate(getView().getHomepageViewModel().getReturnDate());
                if (currentReturnDate.compareTo(newDepartureDate) < 0) {
                    Date reAssignReturnDate = TrainDateUtil.addDate(newDepartureDate, DEFAULT_RANGE_OF_DEPARTURE_AND_ARRIVAL);
                    if (reAssignReturnDate.after(limitDate)) {
                        getView().getHomepageViewModel().setReturnDate(TrainDateUtil.dateToString(newDepartureDate, TrainDateUtil.DEFAULT_FORMAT));
                        getView().getHomepageViewModel().setReturnDateFmt(TrainDateUtil.dateToString(newDepartureDate, TrainDateUtil.DEFAULT_VIEW_FORMAT));
                    } else {
                        getView().getHomepageViewModel().setReturnDate(TrainDateUtil.dateToString(reAssignReturnDate, TrainDateUtil.DEFAULT_FORMAT));
                        getView().getHomepageViewModel().setReturnDateFmt(TrainDateUtil.dateToString(reAssignReturnDate, TrainDateUtil.DEFAULT_VIEW_FORMAT));
                    }
                }
                getView().renderRoundTripView(getView().getHomepageViewModel());
            } else {
                Date reAssignReturnDate = TrainDateUtil.addDate(newDepartureDate, DEFAULT_RANGE_OF_DEPARTURE_AND_ARRIVAL);
                if (reAssignReturnDate.after(limitDate)) {
                    getView().getHomepageViewModel().setReturnDate(TrainDateUtil.dateToString(newDepartureDate, TrainDateUtil.DEFAULT_FORMAT));
                    getView().getHomepageViewModel().setReturnDateFmt(TrainDateUtil.dateToString(newDepartureDate, TrainDateUtil.DEFAULT_VIEW_FORMAT));
                } else {
                    getView().getHomepageViewModel().setReturnDate(TrainDateUtil.dateToString(reAssignReturnDate, TrainDateUtil.DEFAULT_FORMAT));
                    getView().getHomepageViewModel().setReturnDateFmt(TrainDateUtil.dateToString(reAssignReturnDate, TrainDateUtil.DEFAULT_VIEW_FORMAT));
                }
                getView().renderSingleTripView(getView().getHomepageViewModel());
            }
        }
    }

    @Override
    public void onReturnDateChange(int year, int month, int dayOfMonth) {
        Calendar now = TrainDateUtil.getCurrentCalendar();
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, month);
        now.set(Calendar.DATE, dayOfMonth);

        Date newReturnDate = now.getTime();

        Date departureDate = TrainDateUtil.stringToDate(getView().getHomepageViewModel().getDepartureDate());
        Date limitDate = TrainDateUtil.addTimeToCurrentDate(Calendar.DATE, MAX_BOOKING_DAYS_FROM_TODAY);

        if (newReturnDate.after(limitDate)) {
            getView().showReturnDateMax100Days(R.string.kai_homepage_return_max_100_days_from_today_error);
        } else if (newReturnDate.before(departureDate)) {
            getView().showReturnDateShouldGreaterOrEqual(R.string.kai_homepage_return_should_greater_equal_error);
        } else {
            String newReturnDateStr = TrainDateUtil.dateToString(newReturnDate, TrainDateUtil.DEFAULT_FORMAT);
            getView().getHomepageViewModel().setReturnDate(newReturnDateStr);
            String newReturnDateFmtStr = TrainDateUtil.dateToString(newReturnDate, TrainDateUtil.DEFAULT_VIEW_FORMAT);
            getView().getHomepageViewModel().setReturnDateFmt(newReturnDateFmtStr);
            getView().renderRoundTripView(getView().getHomepageViewModel());
        }
    }

    @Override
    public void initialize() {
        if (trainRouter.isTrainNativeEnable()) {
            if (userSession.isLoggedIn()) {
                onInitialize();
            } else {
                getView().navigateToLoginPage();
            }
        } else {
            getView().navigateToKaiWebView();
        }
    }

    private void onInitialize() {
        getView().setHomepageViewModel(trainHomepageCache.buildTrainHomepageViewModelFromCache());
        final TrainHomepageViewModel trainHomepageViewModel = getView().getHomepageViewModel();

        if (trainHomepageViewModel.getDepartureDate() != null && !trainHomepageViewModel.getDepartureDate().isEmpty()) {
            Calendar departureCalendar = TrainDateUtil.getCurrentCalendar();
            departureCalendar.setTime(TrainDateUtil.stringToDate(trainHomepageViewModel.getDepartureDate()));
            onDepartureDateChange(departureCalendar.get(Calendar.YEAR), departureCalendar.get(Calendar.MONTH),
                    departureCalendar.get(Calendar.DATE));
        }

        if (!trainHomepageViewModel.getReturnDate().isEmpty()) {
            Calendar returnCalendar = TrainDateUtil.getCurrentCalendar();
            returnCalendar.setTime(TrainDateUtil.stringToDate(trainHomepageViewModel.getReturnDate()));
            onReturnDateChange(returnCalendar.get(Calendar.YEAR), returnCalendar.get(Calendar.MONTH),
                    returnCalendar.get(Calendar.DATE));
        }

        renderUi();
    }

    @Override
    public void onOriginStationChanged(TrainStationAndCityViewModel viewModel) {
        TrainHomepageViewModel homepageViewModel = getView().getHomepageViewModel();
        homepageViewModel.setOriginStation(viewModel);
        getView().setHomepageViewModel(homepageViewModel);
        renderUi();
    }

    @Override
    public void onDepartureStationChanged(TrainStationAndCityViewModel viewModel) {
        TrainHomepageViewModel homepageViewModel = getView().getHomepageViewModel();
        homepageViewModel.setDestinationStation(viewModel);
        getView().setHomepageViewModel(homepageViewModel);
        renderUi();
    }

    @Override
    public void onSubmitButtonClicked() {
        if (validateFields()) {
            TrainHomepageViewModel viewModel = getView().getHomepageViewModel();

            String returnDate = viewModel.getReturnDate().replace("-", "");
            String departureDate = viewModel.getDepartureDate().replace("-", "");

            TrainSearchPassDataViewModel passDataViewModel = new TrainSearchPassDataViewModel(
                    viewModel.getTrainPassengerViewModel().getAdult(),
                    viewModel.getTrainPassengerViewModel().getInfant(),
                    departureDate,
                    returnDate,
                    viewModel.getOriginStation().getStationCode(),
                    viewModel.getOriginStation().getCityName(),
                    viewModel.getDestinationStation().getStationCode(),
                    viewModel.getDestinationStation().getCityName(),
                    viewModel.isOneWay()
            );
            getView().navigateToSearchPage(passDataViewModel);
        }
    }

    @Override
    public void onTrainPassengerChange(TrainPassengerViewModel trainPassengerViewModel) {
        TrainHomepageViewModel trainHomepageViewModel = getView().getHomepageViewModel();
        trainHomepageViewModel.setTrainPassengerViewModel(trainPassengerViewModel);
        trainHomepageViewModel.setPassengerFmt(buildPassengerTextFormatted(trainPassengerViewModel));
        getView().setHomepageViewModel(trainHomepageViewModel);
        renderUi();
    }

    @Override
    public void onSavedStateAvailable(TrainHomepageViewModel trainHomepageViewModel) {
        getView().setHomepageViewModel(trainHomepageViewModel);
        renderUi();
    }

    @Override
    public void onReverseStationButtonClicked() {
        TrainHomepageViewModel viewModel = getView().getHomepageViewModel();
        TrainStationAndCityViewModel tempDestinationStation = viewModel.getDestinationStation();
        viewModel.setDestinationStation(viewModel.getOriginStation());
        viewModel.setOriginStation(tempDestinationStation);
        getView().setHomepageViewModel(viewModel);
        renderUi();
    }

    @Override
    public void saveHomepageViewModelToCache(TrainHomepageViewModel viewModel) {
        trainHomepageCache.saveToCache(viewModel);
    }

    @Override
    public void onLoginRecieved() {
        if (userSession.isLoggedIn()) {
            onInitialize();
        } else {
            getView().closePage();
        }
    }

    private boolean validateFields() {
        boolean isValid = true;
        TrainHomepageViewModel viewModel = getView().getHomepageViewModel();

        if (viewModel.getOriginStation() == null) {
            getView().showOriginStationEmptyError(R.string.train_homepage_origin_should_not_empty_error_message);
            isValid = false;
        } else if (viewModel.getDestinationStation() == null) {
            getView().showDestinationStationEmptyError(R.string.train_homepage_destination_should_not_empty_error_message);
            isValid = false;
        } else if ((!TextUtils.isEmpty(viewModel.getOriginStation().getStationCode()) &&
                !TextUtils.isEmpty(viewModel.getDestinationStation().getStationCode()) &&
                viewModel.getOriginStation().getStationCode().equalsIgnoreCase(viewModel.getDestinationStation().getStationCode())) ||
                (TextUtils.isEmpty(viewModel.getOriginStation().getStationCode()) &&
                        TextUtils.isEmpty(viewModel.getDestinationStation().getStationCode()) &&
                        viewModel.getOriginStation().getCityName().equalsIgnoreCase(viewModel.getDestinationStation().getCityName()))
                ) {
            getView().showOriginAndDestinationShouldNotSameError(R.string.train_homepage_origin_destination_should_not_same_error_message);
            isValid = false;
        } else if (!viewModel.getDestinationStation().getIslandName()
                .equalsIgnoreCase(viewModel.getOriginStation().getIslandName())) {
            getView().showOriginAndDestinationIslandShouldBeTheSame(R.string.train_homepage_origin_destination_island_should_be_the_same_error_message);
            isValid = false;
        }
        return isValid;
    }

    private void renderUi() {
        if (getView().getHomepageViewModel().isOneWay()) {
            singleTrip();
        } else {
            roundTrip();
        }
        getView().stopTrace();
    }

    private void setupViewModel() {
        Date departureDate = TrainDateUtil.addTimeToCurrentDate(Calendar.DATE, 1); // departure date = today + 1
        String departureDateString = TrainDateUtil.dateToString(departureDate, TrainDateUtil.DEFAULT_FORMAT);
        String departureDateFmtString = TrainDateUtil.dateToString(departureDate, TrainDateUtil.DEFAULT_VIEW_FORMAT);

        Date returnDate = TrainDateUtil.addTimeToSpesificDate(departureDate, Calendar.DATE, DEFAULT_RANGE_OF_DEPARTURE_AND_ARRIVAL); // return date = departure date + 2
        String returnDateString = TrainDateUtil.dateToString(returnDate, TrainDateUtil.DEFAULT_FORMAT);
        String returnDateFmtString = TrainDateUtil.dateToString(returnDate, TrainDateUtil.DEFAULT_VIEW_FORMAT);

        TrainPassengerViewModel passData = new TrainPassengerViewModel.Builder()
                .setAdult(1)
                .build();
        String passengerFmt = buildPassengerTextFormatted(passData);

        TrainHomepageViewModel viewModel = new TrainHomepageViewModel.Builder()
                .setTrainPassengerViewModel(passData)
                .setIsOneWay(true)
                .setDepartureDate(departureDateString)
                .setReturnDate(returnDateString)
                .setDepartureDateFmt(departureDateFmtString)
                .setReturnDateFmt(returnDateFmtString)
                .setPassengerFmt(passengerFmt)
                .build();

        getView().setHomepageViewModel(viewModel);
    }

    @NonNull
    private String buildPassengerTextFormatted(TrainPassengerViewModel passData) {
        String passengerFmt = "";
        if (passData.getAdult() > 0) {
            passengerFmt = passData.getAdult() + " " + getView().getActivity().getString(R.string.kai_homepage_adult_passenger);
            if (passData.getInfant() > 0) {
                passengerFmt += ", " + passData.getInfant() + " " + getView().getActivity().getString(R.string.kai_homepage_infant_passenger);
            }
        }
        return passengerFmt;
    }

    @Override
    public void getTrainPromoList() {
        getTrainPromoUseCase.execute(getTrainPromoUseCase.create(),
                new Subscriber<List<TrainPromoViewModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hidePromoList();
                    }

                    @Override
                    public void onNext(List<TrainPromoViewModel> trainPromoViewModelList) {
                        if (trainPromoViewModelList.size() > 0) {
                            getView().showPromoList();
                            getView().renderPromoList(trainPromoViewModelList);
                        } else {
                            getView().hidePromoList();
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        if (getTrainPromoUseCase != null) getTrainPromoUseCase.unsubscribe();
    }
}
