package com.tokopedia.train.homepage.presentation.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.TrainRouter;
import com.tokopedia.train.common.constant.TrainAppScreen;
import com.tokopedia.train.common.constant.TrainUrl;
import com.tokopedia.train.common.presentation.TextInputView;
import com.tokopedia.train.common.util.TrainAnalytics;
import com.tokopedia.train.homepage.di.TrainHomepageComponent;
import com.tokopedia.train.homepage.presentation.activity.TrainPassengerPickerActivity;
import com.tokopedia.train.homepage.presentation.listener.TrainHomepageView;
import com.tokopedia.train.homepage.presentation.model.TrainHomepageViewModel;
import com.tokopedia.train.homepage.presentation.model.TrainPassengerViewModel;
import com.tokopedia.train.homepage.presentation.model.TrainPromoViewModel;
import com.tokopedia.train.homepage.presentation.model.TrainSearchPassDataViewModel;
import com.tokopedia.train.homepage.presentation.presenter.TrainHomepagePresenterImpl;
import com.tokopedia.train.homepage.presentation.widget.TrainPromoListView;
import com.tokopedia.train.search.presentation.activity.TrainSearchDepartureActivity;
import com.tokopedia.train.station.presentation.TrainStationsActivity;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationAndCityViewModel;
import com.tokopedia.travelcalendar.view.bottomsheet.TravelCalendarBottomSheet;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by rizkyfadillah on 2/19/18.
 */
public class TrainHomepageFragment extends BaseDaggerFragment implements TrainHomepageView {

    private static final String PROMO_PATH = "promo";
    private static final String TAG_DEPARTURE_CALENDAR = "trainCalendarDeparture";
    private static final String TAG_RETURN_CALENDAR = "trainCalendarReturn";
    private static final int ORIGIN_STATION_REQUEST_CODE = 1001;
    private static final int DESTINATION_STATION_REQUEST_CODE = 1002;
    private static final int PASSENGER_REQUEST_CODE = 1004;
    private static final int DATE_PICKER_DEPARTURE_REQUEST_CODE = 1005;
    private static final int DATE_PICKER_RETURN_REQUEST_CODE = 1006;
    private static final int DEPARTURE_SCHEDULE_REQUEST_CODE = 1007;
    private static final int REQUEST_CODE_LOGIN = 2008;

    private AppCompatButton buttonOneWayTrip;
    private AppCompatButton buttonRoundTrip;
    private LinearLayout layoutOriginStation;
    private LinearLayout layoutDestinationStation;
    private AppCompatTextView tvOriginStationLabel;
    private AppCompatTextView tvDestinationStationLabel;
    private AppCompatImageView imageReverseOriginDestitation;
    private TextInputView textInputViewDateDeparture;
    private TextInputView textInputViewDateReturn;
    private TextInputView textInputViewPassenger;
    private AppCompatButton buttonSearchTicket;
    private View separatorDateReturn;
    private TrainPromoListView trainPromoView;
    private List<TrainPromoViewModel> trainPromoViewModelList;

    private TrainHomepageViewModel viewModel;

    private AbstractionRouter abstractionRouter;

    @Inject
    TrainAnalytics trainAnalytics;

    @Inject
    TrainRouter trainRouter;

    @Inject
    TrainHomepagePresenterImpl trainHomepagePresenterImpl;

    public TrainHomepageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_train_homepage, container, false);

        buttonOneWayTrip = view.findViewById(R.id.button_one_way_trip);
        buttonRoundTrip = view.findViewById(R.id.button_round_trip);
        layoutOriginStation = view.findViewById(R.id.layout_origin_station);
        layoutDestinationStation = view.findViewById(R.id.layout_destination_station);
        tvOriginStationLabel = view.findViewById(R.id.tv_origin_station_label);
        tvDestinationStationLabel = view.findViewById(R.id.tv_destination_station_label);
        imageReverseOriginDestitation = view.findViewById(R.id.image_reverse_origin_destination);
        textInputViewDateDeparture = view.findViewById(R.id.text_input_view_date_departure);
        textInputViewDateReturn = view.findViewById(R.id.text_input_view_date_return);
        textInputViewPassenger = view.findViewById(R.id.text_input_view_passenger);
        buttonSearchTicket = view.findViewById(R.id.button_search_ticket);
        separatorDateReturn = view.findViewById(R.id.separator_date_return);
        trainPromoView = view.findViewById(R.id.train_promo_view);

        abstractionRouter = (AbstractionRouter) getActivity().getApplication();

        layoutOriginStation.setOnClickListener(view12 -> startActivityForResult(
                TrainStationsActivity.getCallingIntent(
                        getActivity(),
                        getString(R.string.train_station_origin_toolbar),
                        getString(R.string.train_station_origin_hint)),
                ORIGIN_STATION_REQUEST_CODE));

        layoutDestinationStation.setOnClickListener(view13 -> startActivityForResult(
                TrainStationsActivity.getCallingIntent(getActivity(),
                        getString(R.string.train_station_destination_toolbar),
                        getString(R.string.train_station_destination_hint)),
                DESTINATION_STATION_REQUEST_CODE));

        buttonOneWayTrip.setSelected(true);

        buttonOneWayTrip.setOnClickListener(v -> {
            trainHomepagePresenterImpl.singleTrip();

            trainAnalytics.eventChooseSingleTrip();
        });

        buttonRoundTrip.setOnClickListener(v -> {
            trainHomepagePresenterImpl.roundTrip();

            trainAnalytics.eventChooseRoundTrip();
        });

        imageReverseOriginDestitation.setOnClickListener(v -> {
            trainHomepagePresenterImpl.onReverseStationButtonClicked();
            Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
            shake.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            imageReverseOriginDestitation.startAnimation(shake);
        });

        textInputViewDateDeparture.setOnClickListener(v -> trainHomepagePresenterImpl.onDepartureDateButtonClicked());

        textInputViewDateReturn.setOnClickListener(v -> trainHomepagePresenterImpl.onReturnDateButtonClicked());

        textInputViewPassenger.setOnClickListener(v -> startActivityForResult(TrainPassengerPickerActivity.getCallingIntent(getActivity(),
                getHomepageViewModel().getTrainPassengerViewModel()),
                PASSENGER_REQUEST_CODE));

        buttonSearchTicket.setOnClickListener(view1 -> {
            trainHomepagePresenterImpl.onSubmitButtonClicked();

            String trip = viewModel.isOneWay() ? "single trip" : "round trip";
            String origin = viewModel.getOriginStation().getStationCode();
            String destination = viewModel.getDestinationStation().getStationCode();
            int numOfPassenger = viewModel.getTrainPassengerViewModel().getAdult() +
                    viewModel.getTrainPassengerViewModel().getInfant();

            trainAnalytics.eventClickFindTicket(trip, origin, destination, numOfPassenger);
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        trainHomepagePresenterImpl.attachView(this);
        trainHomepagePresenterImpl.initialize();
        initializePromoList();
    }

    private void initializePromoList() {
        trainPromoViewModelList = new ArrayList<>();
        trainPromoView.setListener(new TrainPromoListView.ActionListener() {
            @Override
            public void showToastMessage(String message) {
                View view = getView();
                if (view != null) NetworkErrorHelper.showSnackbar(getActivity(), message);
                else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void navigateToPromoPage() {
                Intent intentToPromoList = trainRouter.getPromoListIntent(getActivity(), TrainUrl.PARAM_TRAIN_MENU_ID, TrainUrl.PARAM_TRAIN_SUBMENU_ID);
                startActivity(intentToPromoList);
            }

            @Override
            public void navigateToPromoDetail(int position) {
                if (trainPromoViewModelList.size() > position && trainPromoViewModelList.get(position).getAttributes() != null) {
                    String url = trainPromoViewModelList.get(position).getAttributes().getLinkUrl();
                    Uri uri = Uri.parse(url);
                    boolean isPromoNativeActive = isPromoNativeActive();
                    if (isPromoNativeActive && uri != null
                            && uri.getPathSegments() != null
                            && uri.getPathSegments().size() == 2
                            && uri.getPathSegments().get(0).equalsIgnoreCase(PROMO_PATH)) {
                        String slug = uri.getPathSegments().get(1);
                        startActivity(trainRouter.getPromoDetailIntent(getActivity(), slug));
                    } else {
                        startActivity(trainRouter.getBannerWebViewIntent(getActivity(), url));
                    }
                }
            }

            private boolean isPromoNativeActive() {
                if (getActivity() != null && getActivity().getApplication() instanceof TrainRouter) {
                    return trainRouter.isPromoNativeEnable();
                } else {
                    return false;
                }
            }
        });
        trainHomepagePresenterImpl.getTrainPromoList();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        setHasOptionsMenu(true);
    }

    @Override
    public void renderSingleTripView(TrainHomepageViewModel trainHomepageViewModel) {
        buttonOneWayTrip.setSelected(true);
        buttonRoundTrip.setSelected(false);

        textInputViewDateReturn.setVisibility(View.GONE);
        separatorDateReturn.setVisibility(View.GONE);

        textInputViewDateDeparture.setText(trainHomepageViewModel.getDepartureDateFmt());
        textInputViewPassenger.setText(trainHomepageViewModel.getPassengerFmt());
        if (trainHomepageViewModel.getOriginStation() != null) {
            tvOriginStationLabel.setText(trainHomepageViewModel.getStationTextForView(getContext(), true));
        } else {
            tvOriginStationLabel.setText(null);
            tvOriginStationLabel.setHint(R.string.train_homepage_origin_station_hint);
        }
        if (trainHomepageViewModel.getDestinationStation() != null) {
            tvDestinationStationLabel.setText(trainHomepageViewModel.getStationTextForView(getContext(), false));
        } else {
            tvDestinationStationLabel.setText(null);
            tvDestinationStationLabel.setHint(R.string.train_homepage_destination_station_hint);
        }
    }

    @Override
    public void renderRoundTripView(TrainHomepageViewModel trainHomepageViewModel) {
        buttonOneWayTrip.setSelected(false);
        buttonRoundTrip.setSelected(true);

        textInputViewDateReturn.setVisibility(View.VISIBLE);
        separatorDateReturn.setVisibility(View.VISIBLE);

        textInputViewDateDeparture.setText(trainHomepageViewModel.getDepartureDateFmt());
        textInputViewDateReturn.setText(trainHomepageViewModel.getReturnDateFmt());
        textInputViewPassenger.setText(trainHomepageViewModel.getPassengerFmt());
        if (trainHomepageViewModel.getOriginStation() != null) {
            tvOriginStationLabel.setText(trainHomepageViewModel.getStationTextForView(getContext(), true));
        } else {
            tvOriginStationLabel.setText(null);
            tvOriginStationLabel.setHint(R.string.train_homepage_origin_station_hint);
        }
        if (trainHomepageViewModel.getDestinationStation() != null) {
            tvDestinationStationLabel.setText(trainHomepageViewModel.getStationTextForView(getContext(), false));
        } else {
            tvDestinationStationLabel.setText(null);
            tvDestinationStationLabel.setHint(R.string.train_homepage_destination_station_hint);
        }
    }

    @Override
    public void showDepartureDatePickerDialog(Date selectedDate, Date minDate, Date maxDate) {
        setCalendarDatePicker(selectedDate, minDate, maxDate, getActivity().getString(R.string.travel_calendar_label_choose_departure_trip_date), TAG_DEPARTURE_CALENDAR);
    }

    @Override
    public void showReturnDatePickerDialog(Date selectedDate, Date minDate, Date maxDate) {
        setCalendarDatePicker(selectedDate, minDate, maxDate, getActivity().getString(R.string.travel_calendar_label_choose_return_trip_date), TAG_RETURN_CALENDAR);
    }

    private void setCalendarDatePicker(Date selectedDate, Date minDate, Date maxDate, String title,
                                       String tagFragment) {
        TravelCalendarBottomSheet travelCalendarBottomSheet = new TravelCalendarBottomSheet.Builder()
                .setMinDate(minDate)
                .setMaxDate(maxDate)
                .setSelectedDate(selectedDate)
                .setShowHoliday(true)
                .setTitle(title)
                .build();
        travelCalendarBottomSheet.setListener(new TravelCalendarBottomSheet.ActionListener() {
            @Override
            public void onClickDate(@NotNull Date dateSelected) {
                Calendar calendarSelected = Calendar.getInstance();
                calendarSelected.setTime(dateSelected);
                if (tagFragment.equals(TAG_DEPARTURE_CALENDAR)) {
                    trainHomepagePresenterImpl.onDepartureDateChange(calendarSelected.get(Calendar.YEAR),
                            calendarSelected.get(Calendar.MONTH), calendarSelected.get(Calendar.DATE));
                } else {
                    trainHomepagePresenterImpl.onReturnDateChange(calendarSelected.get(Calendar.YEAR),
                            calendarSelected.get(Calendar.MONTH), calendarSelected.get(Calendar.DATE));
                }
            }
        });
        travelCalendarBottomSheet.show(getActivity().getSupportFragmentManager(), tagFragment);
    }

    @Override
    public void showDepartureDateShouldAtLeastToday(@StringRes int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showDepartureDateMax90Days(@StringRes int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showReturnDateShouldGreaterOrEqual(@StringRes int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showReturnDateMax100Days(@StringRes int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void setHomepageViewModel(TrainHomepageViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void navigateToSearchPage(TrainSearchPassDataViewModel passDataViewModel) {
        Intent intent = TrainSearchDepartureActivity.getCallingIntent(getActivity(), passDataViewModel);
        startActivityForResult(intent, DEPARTURE_SCHEDULE_REQUEST_CODE);
    }

    @SuppressWarnings("Range")
    private void showMessageErrorInSnackBar(int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(resId));
    }

    @Override
    protected String getScreenName() {
        return TrainAppScreen.HOMEPAGE;
    }

    @Override
    protected void initInjector() {
        getComponent(TrainHomepageComponent.class).inject(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ORIGIN_STATION_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    TrainStationAndCityViewModel viewModel = data.getParcelableExtra(TrainStationsActivity.EXTRA_SELECTED_STATION);
                    trainHomepagePresenterImpl.onOriginStationChanged(viewModel);
                }
                break;
            case DESTINATION_STATION_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    TrainStationAndCityViewModel viewModel = data.getParcelableExtra(TrainStationsActivity.EXTRA_SELECTED_STATION);
                    trainHomepagePresenterImpl.onDepartureStationChanged(viewModel);
                }
                break;
            case PASSENGER_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    TrainPassengerViewModel passengerViewModel = data.getParcelableExtra(TrainPassengerPickerActivity.EXTRA_PASS_DATA);
                    trainHomepagePresenterImpl.onTrainPassengerChange(passengerViewModel);
                }
                break;
            case REQUEST_CODE_LOGIN:
                trainHomepagePresenterImpl.onLoginRecieved();
                break;
        }
    }

    @Override
    public TrainHomepageViewModel getHomepageViewModel() {
        return viewModel;
    }

    @Override
    public void showOriginStationEmptyError(int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showDestinationStationEmptyError(int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showOriginAndDestinationShouldNotSameError(int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showOriginAndDestinationIslandShouldBeTheSame(int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void navigateToLoginPage() {
        startActivityForResult(trainRouter.getLoginIntent(), REQUEST_CODE_LOGIN);
    }

    @Override
    public void renderPromoList(List<TrainPromoViewModel> trainPromoViewModelList) {
        this.trainPromoViewModelList = trainPromoViewModelList;
        trainPromoView.setPromoListData(trainPromoViewModelList);
    }

    @Override
    public void hidePromoList() {
        trainPromoView.hidePromoList();
    }

    @Override
    public void showPromoList() {
        trainPromoView.showPromoList();
    }

    @Override
    public void navigateToKaiWebView() {
        startActivity(trainRouter.getWebviewActivity(getActivity(), TrainUrl.KAI_WEBVIEW));
        getActivity().finish();
    }

    @Override
    public void closePage() {
        getActivity().finish();
    }

    @Override
    public void onStop() {
        super.onStop();

        trainHomepagePresenterImpl.saveHomepageViewModelToCache(viewModel);
    }

    public void onDestroy() {
        super.onDestroy();
        trainHomepagePresenterImpl.onDestroy();
    }

}