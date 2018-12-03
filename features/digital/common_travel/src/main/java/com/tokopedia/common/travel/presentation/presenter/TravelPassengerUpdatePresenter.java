package com.tokopedia.common.travel.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.domain.AddTravelPassengerUseCase;
import com.tokopedia.common.travel.domain.EditTravelPassengerUseCase;
import com.tokopedia.common.travel.domain.provider.TravelProvider;
import com.tokopedia.common.travel.presentation.contract.TravelPassengerUpdateContract;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;
import com.tokopedia.common.travel.utils.TravelDateUtil;
import com.tokopedia.common.travel.utils.typedef.TravelBookingPassenger;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nabillasabbaha on 26/06/18.
 */
public class TravelPassengerUpdatePresenter extends BaseDaggerPresenter<TravelPassengerUpdateContract.View>
        implements TravelPassengerUpdateContract.Presenter {
    private static final String PASSENGER_NAME_REGEX = "^[a-zA-Z\\s]*$";
    private static final String PASSENGER_ID_NUMBER_REGEX = "^[A-Za-z0-9]+$";
    private static final int MAX_CONTACT_NAME = 60;
    private static final int MAX_IDENTITY_NUMBER = 20;
    private static final int MIN_IDENTITY_NUMBER = 5;

    private AddTravelPassengerUseCase addTravelPassengerUseCase;
    private EditTravelPassengerUseCase editTravelPassengerUseCase;
    private CompositeSubscription compositeSubscription;
    private TravelProvider travelProvider;

    @Inject
    public TravelPassengerUpdatePresenter(AddTravelPassengerUseCase addTravelPassengerUseCase,
                                          EditTravelPassengerUseCase editTravelPassengerUseCase,
                                          TravelProvider travelProvider) {
        this.addTravelPassengerUseCase = addTravelPassengerUseCase;
        this.editTravelPassengerUseCase = editTravelPassengerUseCase;
        this.travelProvider = travelProvider;
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void submitAddPassengerData() {
        if (isAllDataValid()) {
            compositeSubscription.add(
                    addTravelPassengerUseCase.createObservable(
                            addTravelPassengerUseCase.create(getView().getRequestParamAddPassenger(),
                                    getView().getTravelPlatformType()))
                            .subscribeOn(travelProvider.computation())
                            .unsubscribeOn(travelProvider.computation())
                            .observeOn(travelProvider.computation())
                            .subscribe(new Subscriber<TravelPassenger>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    if (isViewAttached())
                                        getView().showMessageErrorInSnackBar(e);
                                }

                                @Override
                                public void onNext(TravelPassenger travelPassenger) {
                                    getView().navigateToPassengerList();
                                }
                            })
            );
        }
    }

    @Override
    public void onChangeBirthdate(int year, int month, int dayOfMonth) {
        Calendar calendarNow = Calendar.getInstance();
        calendarNow.set(Calendar.YEAR, year);
        calendarNow.set(Calendar.MONTH, month);
        calendarNow.set(Calendar.DATE, dayOfMonth);
        Date dateSelected = calendarNow.getTime();
        dateSelected = TravelDateUtil.removeTime(dateSelected);

        if (getView().getPaxType() == TravelBookingPassenger.INFANT) {
            if (dateSelected.before(getView().getUpperBirthDate()) ||
                    dateSelected.after(getView().getLowerBirthDate())) {
                getView().showMessageErrorInSnackBar(R.string.error_message_pick_infant_passenger);
            } else {
                getView().showBirthdateChange(dateSelected);
            }
        } else if (getView().getPaxType() == TravelBookingPassenger.ADULT){
            if (dateSelected.after(getView().getLowerBirthDate())) {
                getView().showMessageErrorInSnackBar(R.string.error_message_pick_adult_passenger);
            } else {
                getView().showBirthdateChange(dateSelected);
            }
        }
    }

    @Override
    public void submitEditPassengerData() {
        if (isAllDataValid()) {
            compositeSubscription.add(
                    editTravelPassengerUseCase.createObservable(
                            editTravelPassengerUseCase.create(getView().getRequestParamEditPassenger(),
                                    getView().getTravelPlatformType()))
                            .subscribeOn(travelProvider.computation())
                            .unsubscribeOn(travelProvider.computation())
                            .observeOn(travelProvider.computation())
                            .subscribe(new Subscriber<TravelPassenger>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    if (isViewAttached())
                                        getView().showMessageErrorInSnackBar(e);
                                }

                                @Override
                                public void onNext(TravelPassenger travelPassenger) {
                                    getView().navigateToPassengerList();
                                }
                            })
            );
        }
    }

    private boolean isAllDataValid() {
        boolean allDataValid = true;
        if (getView().getSalutationTitle() == null || getView().getSalutationTitle().length() == 0) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_error_salutation);
        } else if (getView().getFirstName() == null || getView().getFirstName().length() == 0) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_error_first_name);
        } else if (getView().getFirstName().length() > MAX_CONTACT_NAME) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_contact_name_max);
        } else if (!getView().getFirstName().matches(PASSENGER_NAME_REGEX)) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_contact_name_containt_alphabet);
        } else if (getView().getLastName() == null || getView().getLastName().length() == 0) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_error_last_name);
        } else if (getView().getLastName().length() > MAX_CONTACT_NAME) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_contact_name_max);
        } else if (!getView().getLastName().matches(PASSENGER_NAME_REGEX)) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_contact_name_containt_alphabet);
        } else if (getView().getPaxType() == TravelBookingPassenger.INFANT &&
                (getView().getBirthdate() == null || getView().getBirthdate().length() == 0)) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_birthdate_empty);
        } else if (getView().getPaxType() == TravelBookingPassenger.ADULT &&
                (getView().getIdentityNumber() == null || getView().getIdentityNumber().length() == 0)) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_error_identity_number);
        } else if (getView().getPaxType() == TravelBookingPassenger.ADULT &&
                getView().getIdentityNumber().length() < MIN_IDENTITY_NUMBER) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_error_identity_number_min);
        } else if (getView().getPaxType() == TravelBookingPassenger.ADULT &&
                getView().getIdentityNumber().length() > MAX_IDENTITY_NUMBER) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_error_identity_number_max);
        } else if (getView().getPaxType() == TravelBookingPassenger.ADULT &&
                !getView().getIdentityNumber().matches(PASSENGER_ID_NUMBER_REGEX)) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_error_identity_alphanumeric);
        }

        return allDataValid;
    }

    @Override
    public void onDestroyView() {
        detachView();
        addTravelPassengerUseCase.unsubscribe();

        if (compositeSubscription != null && compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }
    }
}
