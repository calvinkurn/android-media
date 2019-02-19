package com.tokopedia.flight.dashboard.view.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.common.travel.ticker.TravelTickerFlightPage;
import com.tokopedia.common.travel.ticker.TravelTickerInstanceId;
import com.tokopedia.common.travel.ticker.domain.TravelTickerUseCase;
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerViewModel;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.domain.interactor.FlightAirportVersionCheckUseCase;
import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel;
import com.tokopedia.flight.banner.data.source.cloud.model.BannerDetail;
import com.tokopedia.flight.banner.domain.interactor.BannerGetDataUseCase;
import com.tokopedia.flight.common.constant.FlightUrl;
import com.tokopedia.flight.common.util.FlightAnalytics;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.dashboard.data.cloud.entity.flightclass.FlightClassEntity;
import com.tokopedia.flight.dashboard.domain.GetFlightAirportWithParamUseCase;
import com.tokopedia.flight.dashboard.domain.GetFlightClassByIdUseCase;
import com.tokopedia.flight.dashboard.view.fragment.cache.FlightDashboardCache;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightClassViewModel;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightDashboardAirportAndClassWrapper;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightDashboardPassDataViewModel;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightDashboardViewModel;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.mapper.FlightClassViewModelMapper;
import com.tokopedia.flight.dashboard.view.validator.FlightDashboardValidator;
import com.tokopedia.flight.dashboard.view.validator.FlightSelectPassengerValidator;
import com.tokopedia.flight.search.domain.usecase.FlightDeleteAllFlightSearchDataUseCase;
import com.tokopedia.flight_dbflow.FlightAirportDB;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by alvarisi on 10/30/17.
 */

public class FlightDashboardPresenter extends BaseDaggerPresenter<FlightDashboardContract.View>
        implements FlightDashboardContract.Presenter {

    private static final String DEVICE_ID = "5";
    private static final String CATEGORY_ID = FlightUrl.CATEGORY_ID;
    private static final int INDEX_DEPARTURE_TRIP = 0;
    private static final int INDEX_RETURN_TRIP = 1;
    private static final int INDEX_ID_AIRPORT_DEPARTURE_TRIP = 0;
    private static final int INDEX_ID_AIRPORT_ARRIVAL_TRIP = 1;
    private static final int INDEX_DATE_TRIP = 2;
    private static final int DEFAULT_ADULT_PASSENGER = 1;
    private static final int DEFAULT_CHILD_PASSENGER = 0;
    private static final int DEFAULT_INFANT_PASSENGER = 0;
    private static final int DEFAULT_LAST_HOUR_IN_DAY = 23;
    private static final int DEFAULT_LAST_MIN_IN_DAY = 59;
    private static final int DEFAULT_LAST_SEC_IN_DAY = 59;
    private static final int MAX_DATE_ADDITION_YEAR = 1;
    private static final String FLIGHT_AIRPORT = "flight_airport";

    private BannerGetDataUseCase bannerGetDataUseCase;
    private FlightDashboardValidator validator;
    private GetFlightAirportWithParamUseCase getFlightAirportWithParamUseCase;
    private GetFlightClassByIdUseCase getFlightClassByIdUseCase;
    private FlightClassViewModelMapper flightClassViewModelMapper;
    private FlightDashboardCache flightDashboardCache;
    private UserSession userSession;
    private FlightAnalytics flightAnalytics;
    private CompositeSubscription compositeSubscription;
    private FlightSelectPassengerValidator passengerValidator;
    private FlightAirportVersionCheckUseCase flightAirportVersionCheckUseCase;
    private FlightModuleRouter flightModuleRouter;
    private FlightAirportViewModelMapper flightAirportViewModelMapper;
    private TravelTickerUseCase travelTickerUseCase;

    private FlightDeleteAllFlightSearchDataUseCase flightDeleteAllFlightSearchDataUseCase;

    @Inject
    public FlightDashboardPresenter(BannerGetDataUseCase bannerGetDataUseCase,
                                    FlightDashboardValidator validator,
                                    GetFlightAirportWithParamUseCase getFlightAirportWithParamUseCase,
                                    GetFlightClassByIdUseCase getFlightClassByIdUseCase,
                                    FlightClassViewModelMapper flightClassViewModelMapper,
                                    FlightDashboardCache flightDashboardCache,
                                    UserSession userSession,
                                    FlightAnalytics flightAnalytics,
                                    FlightSelectPassengerValidator passengerValidator,
                                    FlightAirportVersionCheckUseCase flightAirportVersionCheckUseCase,
                                    FlightModuleRouter flightModuleRouter,
                                    FlightAirportViewModelMapper flightAirportViewModelMapper,
                                    FlightDeleteAllFlightSearchDataUseCase flightDeleteAllFlightSearchDataUseCase,
                                    TravelTickerUseCase travelTickerUseCase) {
        this.bannerGetDataUseCase = bannerGetDataUseCase;
        this.validator = validator;
        this.getFlightAirportWithParamUseCase = getFlightAirportWithParamUseCase;
        this.getFlightClassByIdUseCase = getFlightClassByIdUseCase;
        this.flightClassViewModelMapper = flightClassViewModelMapper;
        this.flightDashboardCache = flightDashboardCache;
        this.userSession = userSession;
        this.flightAnalytics = flightAnalytics;
        this.passengerValidator = passengerValidator;
        this.flightAirportVersionCheckUseCase = flightAirportVersionCheckUseCase;
        this.flightModuleRouter = flightModuleRouter;
        this.flightAirportViewModelMapper = flightAirportViewModelMapper;
        this.flightDeleteAllFlightSearchDataUseCase = flightDeleteAllFlightSearchDataUseCase;
        this.travelTickerUseCase = travelTickerUseCase;
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onSingleTripChecked() {
        flightAnalytics.eventTripTypeClick(getView().getString(R.string.flight_dashboard_analytic_one_way).toString());
        flightDashboardCache.putRoundTrip(false);
        getView().getCurrentDashboardViewModel().setOneWay(true);
        getView().renderSingleTripView();
    }

    @Override
    public void onRoundTripChecked() {
        flightAnalytics.eventTripTypeClick(getView().getString(R.string.flight_dashboard_analytic_round_trip).toString());
        flightDashboardCache.putRoundTrip(true);
        if (!flightDashboardCache.getReturnDate().isEmpty()) {
            FlightDashboardViewModel viewModel = cloneViewModel(getView().getCurrentDashboardViewModel());
            Date returnDate = FlightDateUtil.stringToDate(flightDashboardCache.getReturnDate());
            if (returnDate.before(FlightDateUtil.stringToDate(viewModel.getDepartureDate()))) {
                flightDashboardCache.putReturnDate(viewModel.getDepartureDate());
            } else {
                viewModel.setReturnDate(FlightDateUtil.dateToString(returnDate, FlightDateUtil.DEFAULT_FORMAT));
                viewModel.setReturnDateFmt(FlightDateUtil.dateToString(returnDate, FlightDateUtil.DEFAULT_VIEW_FORMAT));
                getView().setDashBoardViewModel(viewModel);
            }
        }
        getView().getCurrentDashboardViewModel().setOneWay(false);
        getView().renderRoundTripView();
    }

    @Override
    public void initialize() {
        if (userSession.isLoggedIn()) {
            onInitialize();
        } else {
            getView().navigateToLoginPage();
        }
    }

    private void onInitialize() {
        setupViewModel();
        getBannerData();

        if (!getView().isFromApplink()) {
            actionLoadFromCache();
        } else {
            transformExtras();
        }
    }

    private void actionLoadFromCache() {
        FlightDashboardPassDataViewModel flightDashboardPassDataViewModel = getView().getDashboardPassData();
        flightDashboardPassDataViewModel.setDepartureAirportId(flightDashboardCache.getDepartureAirport());
        flightDashboardPassDataViewModel.setArrivalAirportId(flightDashboardCache.getArrivalAirport());
        flightDashboardPassDataViewModel.setDepartureDate(flightDashboardCache.getDepartureDate());
        flightDashboardPassDataViewModel.setReturnDate(flightDashboardCache.getReturnDate());
        flightDashboardPassDataViewModel.setAdultPassengerCount(flightDashboardCache.getPassengerAdult());
        flightDashboardPassDataViewModel.setChildPassengerCount(flightDashboardCache.getPassengerChild());
        flightDashboardPassDataViewModel.setInfantPassengerCount(flightDashboardCache.getPassengerInfant());
        flightDashboardPassDataViewModel.setFlightClass(flightDashboardCache.getClassCache());
        flightDashboardPassDataViewModel.setRoundTrip(flightDashboardCache.isRoundTrip());
        getView().setDashboardPassData(flightDashboardPassDataViewModel);

        actionRenderFromPassData(false);
    }

    private void setupViewModel() {
        Date currentDate = FlightDateUtil.addTimeToCurrentDate(Calendar.DATE, 1);
        Date returnDate = FlightDateUtil.addTimeToCurrentDate(Calendar.DATE, 2);
        String departureDateString = FlightDateUtil.dateToString(currentDate, FlightDateUtil.DEFAULT_FORMAT);
        String departureDateFmtString = FlightDateUtil.dateToString(currentDate, FlightDateUtil.DEFAULT_VIEW_FORMAT);
        String returnDateString = FlightDateUtil.dateToString(returnDate, FlightDateUtil.DEFAULT_FORMAT);
        String returnDateFmtString = FlightDateUtil.dateToString(returnDate, FlightDateUtil.DEFAULT_VIEW_FORMAT);
        FlightPassengerViewModel passData = new FlightPassengerViewModel.Builder()
                .setAdult(DEFAULT_ADULT_PASSENGER)
                .setChildren(DEFAULT_CHILD_PASSENGER)
                .setInfant(DEFAULT_INFANT_PASSENGER)
                .build();
        String passengerFmt = buildPassengerTextFormatted(passData);
        FlightDashboardViewModel viewModel = new FlightDashboardViewModel.Builder()
                .setFlightPassengerViewModel(passData)
                .setIsOneWay(true)
                .setDepartureDate(departureDateString)
                .setReturnDate(returnDateString)
                .setDepartureDateFmt(departureDateFmtString)
                .setReturnDateFmt(returnDateFmtString)
                .setFlightPassengerFmt(passengerFmt)
                .setDepartureAirportFmt("")
                .setArrivalAirportFmt("")
                .build();

        getView().setDashBoardViewModel(viewModel);
    }

    @NonNull
    private String buildPassengerTextFormatted(FlightPassengerViewModel passData) {
        String passengerFmt = "";
        if (passData.getAdult() > 0) {
            passengerFmt = passData.getAdult() + " " + getView().getString(R.string.flight_dashboard_adult_passenger);
            if (passData.getChildren() > 0) {
                passengerFmt += ", " + passData.getChildren() + " " + getView().getString(R.string.flight_dashboard_adult_children);
            }
            if (passData.getInfant() > 0) {
                passengerFmt += ", " + passData.getInfant() + " " + getView().getString(R.string.flight_dashboard_adult_infant);
            }
        }
        return passengerFmt;
    }

    @Override
    public void onReverseAirportButtonClicked() {
        FlightDashboardViewModel viewModel = cloneViewModel(getView().getCurrentDashboardViewModel());
        FlightAirportViewModel flightAirportDB = viewModel.getArrivalAirport();
        String destinationFmt = viewModel.getArrivalAirportFmt();
        viewModel.setArrivalAirport(viewModel.getDepartureAirport());
        viewModel.setArrivalAirportFmt(viewModel.getDepartureAirportFmt());
        viewModel.setDepartureAirport(flightAirportDB);
        viewModel.setDepartureAirportFmt(destinationFmt);
        getView().setDashBoardViewModel(viewModel);
        String airportTemp = flightDashboardCache.getArrivalAirport();
        flightDashboardCache.putArrivalAirport(flightDashboardCache.getDepartureAirport());
        flightDashboardCache.putDepartureAirport(airportTemp);
        renderUi();
    }

    @Override
    public void onDepartureDateButtonClicked() {
        Date minDate = FlightDateUtil.getCurrentDate();
        Date maxDate = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, MAX_DATE_ADDITION_YEAR);
        maxDate = FlightDateUtil.addTimeToSpesificDate(maxDate, Calendar.DATE, -1);
        Calendar maxDateCalendar = FlightDateUtil.getCurrentCalendar();
        maxDateCalendar.setTime(maxDate);
        maxDateCalendar.set(Calendar.HOUR_OF_DAY, DEFAULT_LAST_HOUR_IN_DAY);
        maxDateCalendar.set(Calendar.MINUTE, DEFAULT_LAST_MIN_IN_DAY);
        maxDateCalendar.set(Calendar.SECOND, DEFAULT_LAST_SEC_IN_DAY);

        Date selectedDate = FlightDateUtil.stringToDate(getView().getCurrentDashboardViewModel().getDepartureDate());
        getView().showDepartureCalendarDatePicker(selectedDate, minDate, maxDateCalendar.getTime());
    }

    @Override
    public void onDepartureDateChange(int year, int month, int dayOfMonth, boolean showError) {
        FlightDashboardViewModel viewModel = cloneViewModel(getView().getCurrentDashboardViewModel());
        Calendar now = FlightDateUtil.getCurrentCalendar();
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, month);
        now.set(Calendar.DATE, dayOfMonth);
        Date newDepartureDate = now.getTime();
        Date oneYears = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, MAX_DATE_ADDITION_YEAR);
        oneYears = FlightDateUtil.addTimeToSpesificDate(oneYears, Calendar.DATE, -1);
        if (newDepartureDate.after(oneYears)) {
            if (showError) {
                getView().showDepartureDateMaxTwoYears(R.string.flight_dashboard_departure_max_one_years_from_today_error);
            }
        } else if (newDepartureDate.before(FlightDateUtil.getCurrentDate())) {
            if (showError) {
                getView().showDepartureDateShouldAtLeastToday(R.string.flight_dashboard_departure_should_atleast_today_error);
            }
        } else {
            String newDepartureDateStr = FlightDateUtil.dateToString(newDepartureDate, FlightDateUtil.DEFAULT_FORMAT);
            viewModel.setDepartureDate(newDepartureDateStr);
            flightDashboardCache.putDepartureDate(newDepartureDateStr);
            String newDepartureDateFmtStr = FlightDateUtil.dateToString(newDepartureDate, FlightDateUtil.DEFAULT_VIEW_FORMAT);
            viewModel.setDepartureDateFmt(newDepartureDateFmtStr);
            if (!viewModel.isOneWay()) {
                Date currentReturnDate = FlightDateUtil.stringToDate(viewModel.getReturnDate());
                if (currentReturnDate.compareTo(newDepartureDate) < 0) {
                    Date reAssignReturnDate = FlightDateUtil.addDate(newDepartureDate, 1);
                    if (reAssignReturnDate.after(oneYears)) {
                        viewModel.setReturnDate(newDepartureDateStr);
                        viewModel.setReturnDateFmt(newDepartureDateFmtStr);
                    } else {
                        viewModel.setReturnDate(FlightDateUtil.dateToString(reAssignReturnDate, FlightDateUtil.DEFAULT_FORMAT));
                        viewModel.setReturnDateFmt(FlightDateUtil.dateToString(reAssignReturnDate, FlightDateUtil.DEFAULT_VIEW_FORMAT));
                    }
                }
                getView().setDashBoardViewModel(viewModel);
                getView().renderRoundTripView();
            } else {
                Date reAssignReturnDate = FlightDateUtil.addDate(newDepartureDate, 1);
                if (reAssignReturnDate.after(oneYears)) {
                    viewModel.setReturnDate(newDepartureDateStr);
                    viewModel.setReturnDateFmt(newDepartureDateFmtStr);
                } else {
                    viewModel.setReturnDate(FlightDateUtil.dateToString(reAssignReturnDate, FlightDateUtil.DEFAULT_FORMAT));
                    viewModel.setReturnDateFmt(FlightDateUtil.dateToString(reAssignReturnDate, FlightDateUtil.DEFAULT_VIEW_FORMAT));
                }
                getView().setDashBoardViewModel(viewModel);
                getView().renderSingleTripView();
            }
        }
    }

    @Override
    public void onReturnDateButtonClicked() {
        Date selectedDate = FlightDateUtil.stringToDate(getView().getCurrentDashboardViewModel().getReturnDate());
        Date minDate = FlightDateUtil.stringToDate(getView().getCurrentDashboardViewModel().getDepartureDate());
        Date maxDate = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, MAX_DATE_ADDITION_YEAR);
        maxDate = FlightDateUtil.addTimeToSpesificDate(maxDate, Calendar.DATE, -1);
        Calendar maxDateCalendar = FlightDateUtil.getCurrentCalendar();
        maxDateCalendar.setTime(maxDate);
        maxDateCalendar.set(Calendar.HOUR_OF_DAY, DEFAULT_LAST_HOUR_IN_DAY);
        maxDateCalendar.set(Calendar.MINUTE, DEFAULT_LAST_MIN_IN_DAY);
        maxDateCalendar.set(Calendar.SECOND, DEFAULT_LAST_SEC_IN_DAY);

        getView().showReturnCalendarDatePicker(selectedDate, minDate, maxDateCalendar.getTime());
    }

    @Override
    public void onReturnDateChange(int year, int month, int dayOfMonth, boolean showError) {
        FlightDashboardViewModel viewModel = cloneViewModel(getView().getCurrentDashboardViewModel());
        Calendar now = FlightDateUtil.getCurrentCalendar();
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, month);
        now.set(Calendar.DATE, dayOfMonth);
        Date newReturnDate = now.getTime();
        Date twoYears = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, MAX_DATE_ADDITION_YEAR);
        twoYears = FlightDateUtil.addTimeToSpesificDate(twoYears, Calendar.DATE, -1);
        if (newReturnDate.after(twoYears)) {
            if (showError) {
                getView().showReturnDateMaxTwoYears(R.string.flight_dashboard_return_max_one_years_from_today_error);
            }
        } else if (newReturnDate.before(FlightDateUtil.stringToDate(viewModel.getDepartureDate()))) {
            if (showError) {
                getView().showReturnDateShouldGreaterOrEqual(R.string.flight_dashboard_return_should_greater_equal_error);
            }
        } else {
            String newReturnDateStr = FlightDateUtil.dateToString(newReturnDate, FlightDateUtil.DEFAULT_FORMAT);
            flightDashboardCache.putReturnDate(newReturnDateStr);
            viewModel.setReturnDate(newReturnDateStr);
            String newReturnDateFmtStr = FlightDateUtil.dateToString(newReturnDate, FlightDateUtil.DEFAULT_VIEW_FORMAT);
            viewModel.setReturnDateFmt(newReturnDateFmtStr);
            getView().setDashBoardViewModel(viewModel);
            renderUi();
        }
    }

    private void renderUi() {
        if (!getView().getCurrentDashboardViewModel().isOneWay()) {
            getView().renderRoundTripView();
        } else {
            getView().renderSingleTripView();
        }
        getView().stopTrace();
    }

    @Override
    public void onFlightClassesChange(FlightClassViewModel viewModel) {
        flightDashboardCache.putClassCache(viewModel.getId());
        flightAnalytics.eventClassClick(viewModel.getTitle());
        FlightDashboardViewModel flightDashboardViewModel = cloneViewModel(getView().getCurrentDashboardViewModel());
        flightDashboardViewModel.setFlightClass(viewModel);
        getView().setDashBoardViewModel(flightDashboardViewModel);
        renderUi();
    }

    @Override
    public void onFlightPassengerChange(FlightPassengerViewModel passengerViewModel) {
        flightDashboardCache.putPassengerCount(passengerViewModel.getAdult(), passengerViewModel.getChildren(), passengerViewModel.getInfant());
        flightAnalytics.eventPassengerClick(passengerViewModel.getAdult(), passengerViewModel.getChildren(), passengerViewModel.getInfant());
        FlightDashboardViewModel flightDashboardViewModel = cloneViewModel(getView().getCurrentDashboardViewModel());
        flightDashboardViewModel.setFlightPassengerViewModel(passengerViewModel);
        flightDashboardViewModel.setFlightPassengerFmt(buildPassengerTextFormatted(passengerViewModel));
        getView().setDashBoardViewModel(flightDashboardViewModel);
        renderUi();
    }

    @Override
    public void onDepartureAirportChange(FlightAirportViewModel departureAirport) {
        flightAnalytics.eventOriginClick(departureAirport.getCityName(), departureAirport.getAirportCode());
        FlightDashboardViewModel flightDashboardViewModel = cloneViewModel(getView().getCurrentDashboardViewModel());
        flightDashboardViewModel.setDepartureAirport(departureAirport);
        String code = buildAirportFmt(departureAirport);
        flightDashboardViewModel.setDepartureAirportFmt(code);
        getView().setDashBoardViewModel(flightDashboardViewModel);
        if (departureAirport.getCityAirports() != null && departureAirport.getCityAirports().length > 0) {
            flightDashboardCache.putDepartureAirport(departureAirport.getCityCode());
        } else {
            flightDashboardCache.putDepartureAirport(departureAirport.getAirportCode());
        }
        renderUi();
    }

    @NonNull
    private String buildAirportFmt(FlightAirportViewModel departureAirport) {
        String code = departureAirport.getAirportCode();
        if (TextUtils.isEmpty(code)) {
            code = departureAirport.getCityCode();
        }
        code = departureAirport.getCityName() + " (" + code + ")";
        return code;
    }

    @Override
    public void onArrivalAirportChange(FlightAirportViewModel arrivalAirport) {
        flightAnalytics.eventDestinationClick(arrivalAirport.getCityName(), arrivalAirport.getAirportCode());
        FlightDashboardViewModel flightDashboardViewModel = cloneViewModel(getView().getCurrentDashboardViewModel());
        flightDashboardViewModel.setArrivalAirport(arrivalAirport);
        String code = arrivalAirport.getAirportCode();
        if (TextUtils.isEmpty(code)) {
            code = arrivalAirport.getCityCode();
        }
        flightDashboardViewModel.setArrivalAirportFmt(arrivalAirport.getCityName() + " (" + code + ")");
        getView().setDashBoardViewModel(flightDashboardViewModel);
        if (arrivalAirport.getCityAirports() != null && arrivalAirport.getCityAirports().length > 0) {
            flightDashboardCache.putArrivalAirport(arrivalAirport.getCityCode());
        } else {
            flightDashboardCache.putArrivalAirport(arrivalAirport.getAirportCode());
        }
        renderUi();
    }

    @Override
    public void onSearchTicketButtonClicked() {
        if (validateSearchParam(getView().getCurrentDashboardViewModel())) {
            flightAnalytics.eventSearchClick(getView().getScreenName());
            flightDeleteAllFlightSearchDataUseCase.execute(new Subscriber<Boolean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(Boolean aBoolean) {
                    if (isViewAttached()) {
                        getView().navigateToSearchPage(getView().getCurrentDashboardViewModel());
                    }
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        if (compositeSubscription.hasSubscriptions()) compositeSubscription.unsubscribe();
        detachView();
    }

    @Override
    public void onLoginResultReceived() {
        if (userSession.isLoggedIn()) {
            onInitialize();
        } else {
            getView().closePage();
        }
    }

    private void transformExtras() {
        try {
            FlightDashboardPassDataViewModel flightDashboardPassDataViewModel = getView().getDashboardPassData();

            boolean isPassengerValid = true;

            // transform trip extras
            String[] tempExtras = getView().getTripArguments().split(",");
            String[] extrasTripDeparture = tempExtras[INDEX_DEPARTURE_TRIP].split("_");

            /**
             * tokopedia://flight/search?dest=CGK_DPS_2018-04-01,CGK_DPS_2018-05-01&a=1&c=1&i=1&s=1
             */

            flightDashboardPassDataViewModel.setDepartureAirportId(extrasTripDeparture[INDEX_ID_AIRPORT_DEPARTURE_TRIP]);
            flightDashboardPassDataViewModel.setArrivalAirportId(extrasTripDeparture[INDEX_ID_AIRPORT_ARRIVAL_TRIP]);
            flightDashboardPassDataViewModel.setRoundTrip(false);
            flightDashboardPassDataViewModel.setDepartureDate(extrasTripDeparture[INDEX_DATE_TRIP]);
            flightDashboardPassDataViewModel.setReturnDate("");

            if (tempExtras.length > 1) {
                String[] extrasTripReturn = tempExtras[INDEX_RETURN_TRIP].split("_");
                flightDashboardPassDataViewModel.setRoundTrip(true);
                flightDashboardPassDataViewModel.setReturnDate(extrasTripReturn[INDEX_DATE_TRIP]);
            }

            // transform passenger count
            if (!passengerValidator.validateInfantNotGreaterThanAdult(
                    Integer.parseInt(getView().getAdultPassengerArguments()),
                    Integer.parseInt(getView().getInfantPassengerArguments()))
                    ) {
                isPassengerValid = false;
                getView().showApplinkErrorMessage(R.string.select_passenger_infant_greater_than_adult_error_message);
                flightDashboardPassDataViewModel.setAdultPassengerCount(DEFAULT_ADULT_PASSENGER);
                flightDashboardPassDataViewModel.setChildPassengerCount(DEFAULT_CHILD_PASSENGER);
                flightDashboardPassDataViewModel.setInfantPassengerCount(DEFAULT_INFANT_PASSENGER);
            } else if (!passengerValidator.validateTotalPassenger(
                    Integer.parseInt(getView().getAdultPassengerArguments()),
                    Integer.parseInt(getView().getChildPassengerArguments()))
                    ) {
                isPassengerValid = false;
                getView().showApplinkErrorMessage(R.string.select_passenger_total_passenger_error_message);
                flightDashboardPassDataViewModel.setAdultPassengerCount(DEFAULT_ADULT_PASSENGER);
                flightDashboardPassDataViewModel.setChildPassengerCount(DEFAULT_CHILD_PASSENGER);
                flightDashboardPassDataViewModel.setInfantPassengerCount(DEFAULT_INFANT_PASSENGER);
            } else {
                flightDashboardPassDataViewModel.setAdultPassengerCount(Integer.parseInt(getView().getAdultPassengerArguments()));
                flightDashboardPassDataViewModel.setChildPassengerCount(Integer.parseInt(getView().getChildPassengerArguments()));
                flightDashboardPassDataViewModel.setInfantPassengerCount(Integer.parseInt(getView().getInfantPassengerArguments()));
            }

            // transform class
            int classId = Integer.parseInt(getView().getClassArguments());
            flightDashboardPassDataViewModel.setFlightClass(classId);

            getView().setDashboardPassData(flightDashboardPassDataViewModel);
            actionRenderFromPassData(isPassengerValid);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void actionRenderFromPassData(final boolean isSearchImmediately) {
        final FlightDashboardPassDataViewModel flightDashboardPassDataViewModel = getView().getDashboardPassData();

        if (flightDashboardPassDataViewModel.getDepartureDate() != null && !flightDashboardPassDataViewModel.getDepartureDate().isEmpty()) {
            Calendar departureCalendar = FlightDateUtil.getCurrentCalendar();
            departureCalendar.setTime(FlightDateUtil.stringToDate(flightDashboardPassDataViewModel.getDepartureDate()));
            onDepartureDateChange(departureCalendar.get(Calendar.YEAR), departureCalendar.get(Calendar.MONTH), departureCalendar.get(Calendar.DATE), false);
        }

        if (!flightDashboardPassDataViewModel.getReturnDate().isEmpty()) {
            Calendar returnDate = FlightDateUtil.getCurrentCalendar();
            returnDate.setTime(FlightDateUtil.stringToDate(flightDashboardPassDataViewModel.getReturnDate()));
            onReturnDateChange(returnDate.get(Calendar.YEAR), returnDate.get(Calendar.MONTH), returnDate.get(Calendar.DATE), false);
        }

        if (flightDashboardPassDataViewModel.isRoundTrip()) {
            onRoundTripChecked();
        } else {
            onSingleTripChecked();
        }

        onFlightPassengerChange(
                new FlightPassengerViewModel(
                        flightDashboardPassDataViewModel.getAdultPassengerCount(),
                        flightDashboardPassDataViewModel.getChildPassengerCount(),
                        flightDashboardPassDataViewModel.getInfantPassengerCount()
                )
        );

        Observable<FlightDashboardAirportAndClassWrapper> cacheObservable = getFlightClassByIdUseCase
                .createObservable(getFlightClassByIdUseCase.createRequestParams(flightDashboardPassDataViewModel.getFlightClass())).doOnNext(new Action1<FlightClassEntity>() {
                    @Override
                    public void call(FlightClassEntity flightClassEntity) {
                        if (flightClassEntity != null && getView().getCurrentDashboardViewModel().getFlightClass() == null)
                            onFlightClassesChange(
                                    flightClassViewModelMapper.transform(flightClassEntity)
                            );
                    }
                }).map(new Func1<FlightClassEntity, FlightDashboardAirportAndClassWrapper>() {
                    @Override
                    public FlightDashboardAirportAndClassWrapper call(FlightClassEntity flightClassEntity) {
                        FlightDashboardAirportAndClassWrapper wrapper = new FlightDashboardAirportAndClassWrapper();
                        if (flightClassEntity != null) {
                            wrapper.setFlightClassEntity(flightClassEntity);
                        }
                        return wrapper;
                    }
                });
        if (flightDashboardPassDataViewModel.getDepartureAirportId() != null && flightDashboardPassDataViewModel.getDepartureAirportId().length() > 0
                && flightDashboardPassDataViewModel.getArrivalAirportId() != null && flightDashboardPassDataViewModel.getArrivalAirportId().length() > 0) {
            cacheObservable = cacheObservable.zipWith(getFlightAirportWithParamUseCase
                    .createObservable(getFlightAirportWithParamUseCase.createRequestParams(flightDashboardPassDataViewModel.getDepartureAirportId())
                    ), new Func2<FlightDashboardAirportAndClassWrapper, FlightAirportDB, FlightDashboardAirportAndClassWrapper>() {
                @Override
                public FlightDashboardAirportAndClassWrapper call(FlightDashboardAirportAndClassWrapper flightDashboardAirportsWrapper, FlightAirportDB airportDB) {
                    flightDashboardAirportsWrapper.setDepartureAirport(flightAirportViewModelMapper.transform(airportDB));
                    return flightDashboardAirportsWrapper;
                }
            }).zipWith(getFlightAirportWithParamUseCase
                    .createObservable(getFlightAirportWithParamUseCase
                            .createRequestParams(flightDashboardPassDataViewModel.getArrivalAirportId())
                    ), new Func2<FlightDashboardAirportAndClassWrapper, FlightAirportDB, FlightDashboardAirportAndClassWrapper>() {
                @Override
                public FlightDashboardAirportAndClassWrapper call(FlightDashboardAirportAndClassWrapper flightDashboardAirportsWrapper, FlightAirportDB airportDB) {
                    flightDashboardAirportsWrapper.setArrivalAirport(flightAirportViewModelMapper.transform(airportDB));
                    return flightDashboardAirportsWrapper;
                }
            });
        }


        compositeSubscription.add(
                cacheObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<FlightDashboardAirportAndClassWrapper>() {
                            @Override
                            public void onCompleted() {
                                actionAirportSync();
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                if (isViewAttached()) {
                                    getView().hideProgressBar();
                                    getView().showFormContainer();
                                }
                            }

                            @Override
                            public void onNext(FlightDashboardAirportAndClassWrapper flightDashboardAirportAndClassWrapper) {
                                if (flightDashboardAirportAndClassWrapper != null) {
                                    getView().hideProgressBar();
                                    getView().showFormContainer();
                                    boolean isAvailableToSearch = true;
                                    if (flightDashboardAirportAndClassWrapper.getDepartureAirport() != null
                                            && getView().getCurrentDashboardViewModel().getDepartureAirport() == null) {
                                        onDepartureAirportChange(flightDashboardAirportAndClassWrapper.getDepartureAirport());
                                    } else {
                                        isAvailableToSearch = false;
                                    }
                                    if (flightDashboardAirportAndClassWrapper.getArrivalAirport() != null
                                            && getView().getCurrentDashboardViewModel().getArrivalAirport() == null) {
                                        onArrivalAirportChange(flightDashboardAirportAndClassWrapper.getArrivalAirport());
                                    } else {
                                        isAvailableToSearch = false;
                                    }
                                    if (flightDashboardAirportAndClassWrapper.getFlightClassEntity() != null
                                            && getView().getCurrentDashboardViewModel().getFlightClass() == null) {
                                        onFlightClassesChange(
                                                flightClassViewModelMapper.transform(flightDashboardAirportAndClassWrapper.getFlightClassEntity())
                                        );
                                    } else {
                                        isAvailableToSearch = false;
                                    }
                                    if (isSearchImmediately && isAvailableToSearch) {
                                        onSearchTicketButtonClicked();
                                    }
                                }

                            }
                        })
        );
    }

    private void actionAirportSync() {
        if (isViewAttached()) {
            flightAirportVersionCheckUseCase.execute(flightAirportVersionCheckUseCase.createRequestParams(flightModuleRouter.getLongConfig(FLIGHT_AIRPORT)),
                    new Subscriber<Boolean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            if (aBoolean) {
                                getView().startAirportSyncInBackground(flightModuleRouter.getLongConfig(FLIGHT_AIRPORT));
                            }
                        }
                    });
        }
    }

    @Override
    public void onBannerItemClick(int position, BannerDetail bannerDetail) {
        flightAnalytics.eventPromotionClick(position + 1, bannerDetail);
    }

    private void getBannerData() {
        bannerGetDataUseCase.execute(bannerGetDataUseCase.createRequestParams(DEVICE_ID, CATEGORY_ID), new Subscriber<List<BannerDetail>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    getView().hideBannerView();
                }
            }

            @Override
            public void onNext(List<BannerDetail> bannerDetailList) {
                if (isViewAttached()) {
                    if (bannerDetailList.size() > 0) {
                        getView().renderBannerView(bannerDetailList);
                    } else {
                        getView().hideBannerView();
                    }
                }
            }
        });
    }

    private boolean validateSearchParam(FlightDashboardViewModel currentDashboardViewModel) {
        boolean isValid = true;
        if (!validator.validateDepartureNotEmtpty(currentDashboardViewModel)) {
            isValid = false;
            getView().showDepartureEmptyErrorMessage(R.string.flight_dashboard_departure_empty_error);
        } else if (!validator.validateArrivalNotEmpty(currentDashboardViewModel)) {
            isValid = false;
            getView().showArrivalEmptyErrorMessage(R.string.flight_dashboard_arrival_empty_error);
        } else if (!validator.validateArrivalAndDestinationNotSame(currentDashboardViewModel)) {
            isValid = false;
            getView().showArrivalAndDestinationAreSameError(R.string.flight_dashboard_arrival_departure_same_error);
        } else if (!validator.validateDepartureDateAtLeastToday(currentDashboardViewModel)) {
            isValid = false;
            getView().showDepartureDateShouldAtLeastToday(R.string.flight_dashboard_departure_should_atleast_today_error);
        } else if (!validator.validateAirportsShouldDifferentCity(currentDashboardViewModel)) {
            isValid = false;
            getView()
                    .showAirportShouldDifferentCity(R.string.flight_dashboard_departure_should_different_city_error);
        } else if (!validator.validateReturnDateShouldGreaterOrEqualDeparture(currentDashboardViewModel)) {
            isValid = false;
            getView().showReturnDateShouldGreaterOrEqual(R.string.flight_dashboard_return_should_greater_equal_error);
        } else if (!validator.validatePassengerAtLeastOneAdult(currentDashboardViewModel)) {
            isValid = false;
            getView().showPassengerAtLeastOneAdult(R.string.flight_dashboard_at_least_one_adult_error);
        } else if (!validator.validateFlightClassNotEmpty(currentDashboardViewModel)) {
            isValid = false;
            getView().showFlightClassPassengerIsEmpty(R.string.flight_dashboard_fligh_class_is_empty);
        }
        return isValid;
    }

    @Nullable
    private FlightDashboardViewModel cloneViewModel(FlightDashboardViewModel currentDashboardViewModel) {
        FlightDashboardViewModel viewModel = null;
        try {
            viewModel = (FlightDashboardViewModel) currentDashboardViewModel.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to Clone FlightDashboardViewModel");
        }
        return viewModel;
    }

    @Override
    public void actionOnPromoScrolled(int position, BannerDetail bannerData) {
        flightAnalytics.eventPromoImpression(position, bannerData);
    }

    @Override
    public void fetchTickerData() {
        compositeSubscription.add(travelTickerUseCase.createObservable(travelTickerUseCase.createRequestParams(
                TravelTickerInstanceId.Companion.getFLIGHT(), TravelTickerFlightPage.Companion.getHOME()))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TravelTickerViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TravelTickerViewModel travelTickerViewModel) {

                    }
                }));
    }
}
