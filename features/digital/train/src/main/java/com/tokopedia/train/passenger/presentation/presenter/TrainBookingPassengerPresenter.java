package com.tokopedia.train.passenger.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.common.travel.utils.TravelDateUtil;
import com.tokopedia.common.travel.utils.TravelPassengerValidator;
import com.tokopedia.common.travel.utils.typedef.TravelBookingPassenger;
import com.tokopedia.design.component.CardWithAction;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.data.interceptor.TrainNetworkException;
import com.tokopedia.train.common.data.interceptor.model.TrainError;
import com.tokopedia.train.common.domain.TrainProvider;
import com.tokopedia.train.common.util.TrainNetworkErrorConstant;
import com.tokopedia.train.passenger.data.TrainBookingPassenger;
import com.tokopedia.train.passenger.domain.TrainSoftBookingUseCase;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.train.passenger.presentation.contract.TrainBookingPassengerContract;
import com.tokopedia.train.passenger.presentation.viewmodel.ProfileBuyerInfo;
import com.tokopedia.train.passenger.presentation.viewmodel.TrainPassengerViewModel;
import com.tokopedia.train.search.domain.GetDetailScheduleUseCase;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nabillasabbaha on 25/06/18.
 */
public class TrainBookingPassengerPresenter extends BaseDaggerPresenter<TrainBookingPassengerContract.View>
        implements TrainBookingPassengerContract.Presenter {

    private CompositeSubscription compositeSubscription;
    private GetDetailScheduleUseCase getDetailScheduleUseCase;
    private TrainSoftBookingUseCase trainSoftBookingUseCase;
    private TrainProvider trainProvider;
    private TravelPassengerValidator travelPassengerValidator;

    @Inject
    public TrainBookingPassengerPresenter(GetDetailScheduleUseCase getDetailScheduleUseCase,
                                          TrainSoftBookingUseCase trainSoftBookingUseCase,
                                          TrainProvider trainProvider,
                                          TravelPassengerValidator travelPassengerValidator) {
        this.getDetailScheduleUseCase = getDetailScheduleUseCase;
        this.trainSoftBookingUseCase = trainSoftBookingUseCase;
        this.trainProvider = trainProvider;
        this.travelPassengerValidator = travelPassengerValidator;
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void getDetailSchedule(String idSchedule, CardWithAction cardWithAction) {
        getDetailScheduleUseCase.setIdSchedule(idSchedule);
        compositeSubscription.add(getDetailScheduleUseCase.createObservable(RequestParams.EMPTY)
                .subscribeOn(trainProvider.computation())
                .unsubscribeOn(trainProvider.computation())
                .observeOn(trainProvider.uiScheduler())
                .subscribe(new Subscriber<TrainScheduleViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideDetailSchedule();
                    }

                    @Override
                    public void onNext(TrainScheduleViewModel viewModel) {
                        if (viewModel != null) {
                            if (viewModel.isReturnTrip()) {
                                getView().setCityRouteTripInfo(cardWithAction, getView().getDestinationCity(), getView().getOriginCity());
                                getView().showReturnTripInfo();
                                getView().setReturnTripRequest(getView().convertTripToRequestParam(viewModel));
                            } else {
                                getView().showDepartureTripInfo();
                                getView().setCityRouteTripInfo(cardWithAction, getView().getOriginCity(), getView().getDestinationCity());
                                getView().hideReturnTripInfo();
                                getView().setDepartureTripRequest(getView().convertTripToRequestParam(viewModel));
                            }
                            getView().loadDetailSchedule(viewModel, cardWithAction);
                        }
                    }
                }));
    }

    @Override
    public void getProfilBuyer() {
        compositeSubscription.add(getView().getObservableProfileBuyerInfo()
                .subscribeOn(trainProvider.computation())
                .unsubscribeOn(trainProvider.computation())
                .observeOn(trainProvider.uiScheduler())
                .subscribe(new Subscriber<ProfileBuyerInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().setContactName("");
                            getView().setEmail("");
                            getView().setPhoneNumber("");
                        }
                    }

                    @Override
                    public void onNext(ProfileBuyerInfo profileBuyerInfo) {
                        if (getView().getContactNameEt().length() == 0) {
                            getView().setContactName(profileBuyerInfo.getFullname());
                        }
                        if (getView().getEmailEt().length() == 0) {
                            getView().setEmail(profileBuyerInfo.getEmail());
                        }
                        if (getView().getPhoneNumberEt().length() == 0) {
                            getView().setPhoneNumber(profileBuyerInfo.getPhoneNumber());
                        }
                    }
                }));
    }

    @Override
    public void processInitPassengers(int adultPassengers, int infantPassengers) {
        List<TrainPassengerViewModel> passengerViewModelList = initPassenger(adultPassengers, infantPassengers);
        getView().setCurrentListPassenger(passengerViewModelList);
        getView().renderPassengers(passengerViewModelList);
    }

    @Override
    public void updateDataPassengers(TrainPassengerViewModel trainPassengerViewModel) {
        List<TrainPassengerViewModel> passengerViewModels = getView().getCurrentPassengerList();
        int indexPassenger = passengerViewModels.indexOf(trainPassengerViewModel);
        if (indexPassenger != -1) {
            passengerViewModels.set(indexPassenger, trainPassengerViewModel);
        }
        getView().setCurrentListPassenger(passengerViewModels);
        getView().renderPassengers(passengerViewModels);
    }

    @Override
    public void onSubmitButtonClicked() {
        if (isAllDataValid()) {
            getView().hidePage();
            getView().showLoading();
            compositeSubscription.add(trainSoftBookingUseCase.createObservable(
                    getView().getTrainSoftBookingRequestParam())
                    .subscribeOn(trainProvider.computation())
                    .unsubscribeOn(trainProvider.computation())
                    .observeOn(trainProvider.uiScheduler())
                    .subscribe(new Subscriber<TrainSoftbook>() {
                                   @Override
                                   public void onCompleted() {

                                   }

                                   @Override
                                   public void onError(Throwable e) {
                                       if (isViewAttached()) {
                                           getView().showPage();
                                           getView().hideLoading();
                                           if (e instanceof TrainNetworkException) {
                                               List<TrainError> errors = ((TrainNetworkException) e).getErrorList();
                                               if (errors.contains(new TrainError(TrainNetworkErrorConstant.SOLD_OUT)) ||
                                                       errors.contains(new TrainError(TrainNetworkErrorConstant.RUTE_NOT_FOUND)) ||
                                                       errors.contains(new TrainError(TrainNetworkErrorConstant.TOO_MANY_SOFTBOOK)) ||
                                                       errors.contains(new TrainError(TrainNetworkErrorConstant.LESS_THAN_3_HOURS))) {
                                                   getView().showNavigateToSearchDialog(e.getMessage());
                                                   return;
                                               }
                                           }
                                           getView().showErrorSoftBooking(e);
                                       }
                                   }

                                   @Override
                                   public void onNext(TrainSoftbook trainSoftbook) {
                                       getView().showPage();
                                       getView().hideLoading();
                                       getView().navigateToReview(trainSoftbook);
                                   }
                               }
                    )
            );
        }
    }

    @Override
    public void onChooseSeatButtonClicked() {
        if (isAllDataValid()) {
            getView().hidePage();
            getView().showLoading();
            compositeSubscription.add(trainSoftBookingUseCase.createObservable(getView().getTrainSoftBookingRequestParam())
                    .subscribeOn(trainProvider.computation())
                    .unsubscribeOn(trainProvider.computation())
                    .observeOn(trainProvider.uiScheduler())
                    .subscribe(new Subscriber<TrainSoftbook>() {
                                   @Override
                                   public void onCompleted() {

                                   }

                                   @Override
                                   public void onError(Throwable e) {
                                       if (isViewAttached()) {
                                           getView().showPage();
                                           getView().hideLoading();
                                           getView().showErrorSoftBooking(e);
                                       }
                                   }

                                   @Override
                                   public void onNext(TrainSoftbook trainSoftbook) {
                                       getView().showPage();
                                       getView().hideLoading();
                                       getView().navigateToChooseSeat(trainSoftbook);
                                   }
                               }
                    ));
        }
    }

    private boolean isAllDataValid() {
        boolean isValid = isValidContactInfo();
        if (isValid && !isAllPassengerFilled(getView().getCurrentPassengerList())) {
            isValid = false;
            getView().showMessageErrorInSnackBar(R.string.train_passenger_passenger_not_fullfilled_error);
        }
        return isValid;
    }

    private boolean isValidContactInfo() {
        boolean isValid = true;
        if (travelPassengerValidator.isNameEmpty(getView().getContactNameEt())) {
            isValid = false;
            getView().showMessageErrorInSnackBar(R.string.train_passenger_contact_name_empty_error);
        } else if (travelPassengerValidator.isNameUseSpecialCharacter(getView().getContactNameEt())) {
            isValid = false;
            getView().showMessageErrorInSnackBar(R.string.train_passenger_contact_name_alpha_space_error);
        } else if (travelPassengerValidator.isNameMoreThanMax(getView().getContactNameEt())) {
            isValid = false;
            getView().showMessageErrorInSnackBar(R.string.train_passenger_contact_name_max);
        } else if (travelPassengerValidator.isPhoneNumberEmpty(getView().getPhoneNumberEt())) {
            isValid = false;
            getView().showMessageErrorInSnackBar(R.string.train_passenger_contact_phone_empty_error);
        } else if (travelPassengerValidator.isPhoneNumberUseChar(getView().getPhoneNumberEt())) {
            isValid = false;
            getView().showMessageErrorInSnackBar(R.string.train_passenger_contact_phone_invalid_error);
        } else if (travelPassengerValidator.isPhoneNumberLessThanMin(getView().getPhoneNumberEt())) {
            isValid = false;
            getView().showMessageErrorInSnackBar(R.string.train_passenger_contact_phone_min_length_error);
        } else if (travelPassengerValidator.isPhoneNumberMoreThanMax(getView().getPhoneNumberEt())) {
            isValid = false;
            getView().showMessageErrorInSnackBar(R.string.train_passenger_contact_phone_max_length_error);
        } else if (travelPassengerValidator.isEmailEmpty(getView().getEmailEt())) {
            isValid = false;
            getView().showMessageErrorInSnackBar(R.string.train_passenger_contact_email_empty_error);
        } else if (travelPassengerValidator.isEmailNotValid(getView().getEmailEt())) {
            isValid = false;
            getView().showMessageErrorInSnackBar(R.string.train_passenger_contact_email_invalid_error);
        }
        return isValid;
    }

    private boolean isAllPassengerFilled(List<TrainPassengerViewModel> trainPassengerViewModels) {
        boolean isvalid = true;
        for (TrainPassengerViewModel trainPassengerViewModel : trainPassengerViewModels) {
            if (trainPassengerViewModel.getName() == null || trainPassengerViewModel.getName().length() == 0) {
                isvalid = false;
                break;
            }
        }
        return isvalid;
    }

    protected List<TrainPassengerViewModel> initPassenger(int adultPassengers, int infantPassengers) {
        List<TrainPassengerViewModel> trainPassengerViewModelList = new ArrayList<>();
        int passengerId = 1;
        for (int i = 1; i <= adultPassengers; i++) {
            TrainPassengerViewModel trainPassengerViewModel = new TrainPassengerViewModel();
            trainPassengerViewModel.setIdLocal(passengerId);
            trainPassengerViewModel.setPaxType(TrainBookingPassenger.ADULT);
            trainPassengerViewModel.setHeaderTitle(
                    formatPassengerHeader(getView().getString(R.string.train_passenger_header_title), getView().getString(R.string.train_select_passenger_adult_title)));
            trainPassengerViewModelList.add(trainPassengerViewModel);
            passengerId++;
        }

        for (int i = 1; i <= infantPassengers; i++) {
            TrainPassengerViewModel trainPassengerViewModel = new TrainPassengerViewModel();
            trainPassengerViewModel.setIdLocal(passengerId);
            trainPassengerViewModel.setPaxType(TrainBookingPassenger.INFANT);
            trainPassengerViewModel.setHeaderTitle(
                    formatPassengerHeader(getView().getString(R.string.train_passenger_header_title), getView().getString(R.string.train_select_passenger_infant_title)));
            trainPassengerViewModelList.add(trainPassengerViewModel);
            passengerId++;
        }

        return trainPassengerViewModelList;
    }

    @Override
    public void calculateUpperLowerBirthDate(int paxType) {
        Date dateLower = null, dateUpper = null;
        Date departureDate = getView().getDepartureDate();
        if (paxType == TravelBookingPassenger.INFANT) {
            dateLower = TravelDateUtil.addTimeToSpesificDate(departureDate, Calendar.DAY_OF_MONTH, -1);
            dateUpper = TravelDateUtil.addTimeToSpesificDate(departureDate, Calendar.YEAR, -3);
            dateUpper = TravelDateUtil.addTimeToSpesificDate(dateUpper, Calendar.DAY_OF_MONTH, -1);
        } else if (paxType == TravelBookingPassenger.ADULT) {
            dateLower = TravelDateUtil.addTimeToSpesificDate(departureDate, Calendar.YEAR, -3);
            dateLower = TravelDateUtil.addTimeToSpesificDate(dateLower, Calendar.DAY_OF_MONTH, -1);
        }
        String lowerDate = TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, dateLower);
        String upperDate = dateUpper != null ? TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, dateUpper) : "";
        getView().showUpperLowerBirthDate(lowerDate, upperDate);
    }

    private String formatPassengerHeader(String prefix, String postix) {
        return String.format(getView().getString(R.string.train_passenger_header_format),
                prefix,
                postix
        );
    }

    @Override
    public void onDestroyView() {
        if (compositeSubscription.hasSubscriptions()) compositeSubscription.unsubscribe();
        detachView();
    }
}
