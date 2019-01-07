package com.tokopedia.common.travel.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.domain.AddTravelPassengerUseCase;
import com.tokopedia.common.travel.domain.EditTravelPassengerUseCase;
import com.tokopedia.common.travel.domain.provider.TravelProvider;
import com.tokopedia.common.travel.presentation.contract.TravelPassengerUpdateContract;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;
import com.tokopedia.common.travel.utils.TravelDateUtil;
import com.tokopedia.common.travel.utils.TravelPassengerValidator;
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

    private AddTravelPassengerUseCase addTravelPassengerUseCase;
    private EditTravelPassengerUseCase editTravelPassengerUseCase;
    private CompositeSubscription compositeSubscription;
    private TravelProvider travelProvider;
    private TravelPassengerValidator travelPassengerValidator;

    @Inject
    public TravelPassengerUpdatePresenter(AddTravelPassengerUseCase addTravelPassengerUseCase,
                                          EditTravelPassengerUseCase editTravelPassengerUseCase,
                                          TravelProvider travelProvider,
                                          TravelPassengerValidator travelPassengerValidator) {
        this.addTravelPassengerUseCase = addTravelPassengerUseCase;
        this.editTravelPassengerUseCase = editTravelPassengerUseCase;
        this.travelProvider = travelProvider;
        this.travelPassengerValidator = travelPassengerValidator;
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
                getView().showMessageErrorInSnackBar(R.string.travel_error_msg_pick_infant_passenger);
            } else {
                getView().showBirthdateChange(dateSelected);
            }
        } else if (getView().getPaxType() == TravelBookingPassenger.ADULT) {
            if (dateSelected.after(getView().getLowerBirthDate())) {
                getView().showMessageErrorInSnackBar(R.string.travel_error_msg_pick_adult_passenger);
            } else {
                getView().showBirthdateChange(dateSelected);
            }
        }
    }

    @Override
    public void submitEditPassengerData(String idPassenger) {
        if (isAllDataValid()) {
            editTravelPassengerUseCase.setIdPassengerPrevious(idPassenger);
            compositeSubscription.add(
                    editTravelPassengerUseCase.createObservable(
                            editTravelPassengerUseCase.create(getView().getRequestParamEditPassenger(),
                                    getView().getTravelPlatformType()))
                            .subscribeOn(travelProvider.computation())
                            .unsubscribeOn(travelProvider.computation())
                            .observeOn(travelProvider.computation())
                            .subscribe(new Subscriber<Boolean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    if (isViewAttached()) {
                                        getView().showMessageErrorInSnackBar(e);
                                    }
                                }

                                @Override
                                public void onNext(Boolean isUpdated) {
                                    getView().navigateToPassengerList();
                                }
                            })
            );
        }
    }

    private boolean isAllDataValid() {
        boolean allDataValid = true;
        if (travelPassengerValidator.isSalutationEmpty(getView().getSalutationTitle())) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_error_salutation);
        } else if (travelPassengerValidator.isNameEmpty(getView().getFirstName())) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_error_first_name);
        } else if (travelPassengerValidator.isNameMoreThanMax(getView().getFirstName())) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_contact_name_max);
        } else if (travelPassengerValidator.isNameUseSpecialCharacter(getView().getFirstName())) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_contact_name_containt_alphabet);
        } else if (travelPassengerValidator.isNameEmpty(getView().getLastName())) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_error_last_name);
        } else if (travelPassengerValidator.isNameMoreThanMax(getView().getLastName())) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_contact_name_max);
        } else if (travelPassengerValidator.isNameMoreThanTwoWords(getView().getLastName())) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_contact_last_name_word);
        } else if (travelPassengerValidator.isNameUseSpecialCharacter(getView().getLastName())) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_contact_name_containt_alphabet);
        } else if (getView().getPaxType() == TravelBookingPassenger.INFANT &&
                travelPassengerValidator.isBirthdateEmpty(getView().getBirthdate())) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_birthdate_empty);
        } else if (getView().getPaxType() == TravelBookingPassenger.ADULT && travelPassengerValidator.isIdentityNumberEmpty(getView().getIdentityNumber())) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_error_identity_number);
        } else if (getView().getPaxType() == TravelBookingPassenger.ADULT && travelPassengerValidator.isIdentityNumberLessThanMin(getView().getIdentityNumber())) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_error_identity_number_min);
        } else if (getView().getPaxType() == TravelBookingPassenger.ADULT && travelPassengerValidator.isIdentityNumberMoreThanMax(getView().getIdentityNumber())) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_error_identity_number_max);
        } else if (getView().getPaxType() == TravelBookingPassenger.ADULT && travelPassengerValidator.isIdNumberUseSpecialCharacter(getView().getIdentityNumber())) {
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
