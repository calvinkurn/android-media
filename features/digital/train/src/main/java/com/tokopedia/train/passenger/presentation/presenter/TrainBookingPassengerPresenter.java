package com.tokopedia.train.passenger.presentation.presenter;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.design.component.CardWithAction;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.passenger.data.TrainBookingPassenger;
import com.tokopedia.train.passenger.domain.TrainSoftBookingUseCase;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.train.passenger.domain.requestmodel.TrainScheduleRequest;
import com.tokopedia.train.passenger.presentation.contract.TrainBookingPassengerContract;
import com.tokopedia.train.passenger.presentation.viewmodel.ProfileBuyerInfo;
import com.tokopedia.train.passenger.presentation.viewmodel.TrainPassengerViewModel;
import com.tokopedia.train.search.domain.GetDetailScheduleUseCase;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nabillasabbaha on 25/06/18.
 */
public class TrainBookingPassengerPresenter extends BaseDaggerPresenter<TrainBookingPassengerContract.View>
        implements TrainBookingPassengerContract.Presenter {

    private static final int MAX_CONTACT_NAME = 60;

    private CompositeSubscription compositeSubscription;
    private GetDetailScheduleUseCase getDetailScheduleUseCase;
    private TrainSoftBookingUseCase trainSoftBookingUseCase;

    @Inject
    public TrainBookingPassengerPresenter(GetDetailScheduleUseCase getDetailScheduleUseCase, TrainSoftBookingUseCase trainSoftBookingUseCase) {
        this.getDetailScheduleUseCase = getDetailScheduleUseCase;
        this.trainSoftBookingUseCase = trainSoftBookingUseCase;
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void getProfilBuyer() {
        compositeSubscription.add(getView().getObservableProfileBuyerInfo()
                .onBackpressureDrop()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ProfileBuyerInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (isViewAttached()) {

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
    public void getDetailSchedule(String idSchedule, CardWithAction cardWithAction) {
        getDetailScheduleUseCase.setIdSchedule(idSchedule);
        getDetailScheduleUseCase.execute(RequestParams.EMPTY, new Subscriber<TrainScheduleViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                getView().hideDetailSchedule();
            }

            @Override
            public void onNext(TrainScheduleViewModel viewModel) {
                if (viewModel != null) {
                    if (viewModel.isReturnTrip()) {
                        getView().setCityRouteTripInfo(cardWithAction,
                                getView().getDestinationCity(), getView().getOriginCity());
                        getView().showReturnTripInfo();
                        getView().setReturnTripRequest(convertTripToRequestParam(viewModel));
                    } else {
                        getView().showDepartureTripInfo();
                        getView().setCityRouteTripInfo(cardWithAction,
                                getView().getOriginCity(), getView().getDestinationCity());
                        getView().hideReturnTripInfo();
                        getView().setDepartureTripRequest(convertTripToRequestParam(viewModel));
                    }
                    getView().loadDetailSchedule(viewModel, cardWithAction);
                }
            }
        });
    }

    private TrainScheduleRequest convertTripToRequestParam(TrainScheduleViewModel trainScheduleViewModel) {
        TrainScheduleRequest trainScheduleRequest = new TrainScheduleRequest();
        trainScheduleRequest.setDepartureTimestamp(trainScheduleViewModel.getDepartureTimestamp());
        trainScheduleRequest.setDestination(trainScheduleViewModel.getDestination());
        trainScheduleRequest.setOrigin(trainScheduleViewModel.getOrigin());
        trainScheduleRequest.setSubClass(trainScheduleViewModel.getSubclass());
        trainScheduleRequest.setTrainClass(trainScheduleViewModel.getClassTrain());
        trainScheduleRequest.setTrainName(trainScheduleViewModel.getTrainName());
        trainScheduleRequest.setTrainNo(trainScheduleViewModel.getTrainNumber());
        return trainScheduleRequest;
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
            trainSoftBookingUseCase.execute(trainSoftBookingUseCase.create(
                    getView().getTrainSoftBookingRequestParam()), new Subscriber<TrainSoftbook>() {
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
                    getView().navigateToReview(trainSoftbook);
                }
            });
        }
    }

    @Override
    public void onChooseSeatButtonClicked() {
        if (isAllDataValid()) {
            getView().hidePage();
            getView().showLoading();
            trainSoftBookingUseCase.execute(trainSoftBookingUseCase.create(
                    getView().getTrainSoftBookingRequestParam()), new Subscriber<TrainSoftbook>() {
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
            });
        }
    }

    private boolean isAllDataValid() {
        boolean isValid = true;
        if (TextUtils.isEmpty(getView().getContactNameEt())) {
            isValid = false;
            getView().showMessageErrorInSnackBar(R.string.train_passenger_contact_name_empty_error);
        } else if (!isAlphabetAndSpaceOnly(getView().getContactNameEt())) {
            isValid = false;
            getView().showMessageErrorInSnackBar(R.string.train_passenger_contact_name_alpha_space_error);
        } else if (getView().getContactNameEt().length() > MAX_CONTACT_NAME) {
            isValid = false;
            getView().showMessageErrorInSnackBar(R.string.train_passenger_contact_name_max);
        } else if (TextUtils.isEmpty(getView().getPhoneNumberEt())) {
            isValid = false;
            getView().showMessageErrorInSnackBar(R.string.train_passenger_contact_phone_empty_error);
        } else if (!isNumericOnly(getView().getPhoneNumberEt())) {
            isValid = false;
            getView().showMessageErrorInSnackBar(R.string.train_passenger_contact_phone_invalid_error);
        } else if (!isNumericOnly(getView().getPhoneNumberEt())) {
            isValid = false;
            getView().showMessageErrorInSnackBar(R.string.train_passenger_contact_phone_invalid_error);
        } else if (!isMinPhoneNumberValid(getView().getPhoneNumberEt())) {
            isValid = false;
            getView().showMessageErrorInSnackBar(R.string.train_passenger_contact_phone_min_length_error);
        } else if (!isMaxPhoneNumberValid(getView().getPhoneNumberEt())) {
            isValid = false;
            getView().showMessageErrorInSnackBar(R.string.train_passenger_contact_phone_max_length_error);
        } else if (TextUtils.isEmpty(getView().getEmailEt())) {
            isValid = false;
            getView().showMessageErrorInSnackBar(R.string.train_passenger_contact_email_empty_error);
        } else if (!isEmailWithoutProhibitSymbol(getView().getEmailEt()) || !isValidEmail(getView().getEmailEt())) {
            isValid = false;
            getView().showMessageErrorInSnackBar(R.string.train_passenger_contact_email_invalid_error);
        } else if (!isAllPassengerFilled(getView().getCurrentPassengerList())) {
            isValid = false;
            getView().showMessageErrorInSnackBar(R.string.train_passenger_passenger_not_fullfilled_error);
        }
        return isValid;
    }

    private boolean isEmailWithoutProhibitSymbol(String contactEmail) {
        return !contactEmail.contains("+");
    }

    private boolean isAllPassengerFilled(List<TrainPassengerViewModel> trainPassengerViewModels) {
        boolean isvalid = true;
        for (TrainPassengerViewModel trainPassengerViewModel : trainPassengerViewModels) {
            if (trainPassengerViewModel.getName() == null) {
                isvalid = false;
                break;
            }
        }
        return isvalid;
    }

    private boolean isMinPhoneNumberValid(String phoneNumber) {
        return phoneNumber.length() >= 9;
    }

    private boolean isMaxPhoneNumberValid(String phoneNumber) {
        return phoneNumber.length() <= 15;
    }

    private boolean isNumericOnly(String expression) {
        Pattern pattern = Pattern.compile(new String("^[0-9\\s]*$"));
        Matcher matcher = pattern.matcher(expression);
        return matcher.matches();
    }

    private boolean isAlphabetAndSpaceOnly(String expression) {
        Pattern pattern = Pattern.compile(new String("^[a-zA-Z\\s]*$"));
        Matcher matcher = pattern.matcher(expression);
        return matcher.matches();
    }

    private boolean isValidEmail(String contactEmail) {
        return Patterns.EMAIL_ADDRESS.matcher(contactEmail).matches() && !contactEmail.contains(".@") && !contactEmail.contains("@.");
    }


    @Override
    public void wrapPassengerSameAsBuyer() {
        TrainPassengerViewModel trainPassengerViewModel = new TrainPassengerViewModel();
        trainPassengerViewModel.setPassengerId(1);
        trainPassengerViewModel.setName(getView().getContactNameEt());
        trainPassengerViewModel.setPhone(getView().getPhoneNumberEt());
        trainPassengerViewModel.setPaxType(TrainBookingPassenger.ADULT);
        trainPassengerViewModel.setHeaderTitle(
                formatPassengerHeader(getView().getString(R.string.train_passenger_header_title),
                        1, getView().getString(R.string.train_select_passenger_adult_title)));
        getView().loadPassengerSameAsBuyer(trainPassengerViewModel);
    }

    @Override
    public void removePassengerSameAsBuyer() {
        TrainPassengerViewModel trainPassengerViewModel = new TrainPassengerViewModel();
        trainPassengerViewModel.setPassengerId(1);
        trainPassengerViewModel.setPaxType(TrainBookingPassenger.ADULT);
        trainPassengerViewModel.setHeaderTitle(
                formatPassengerHeader(getView().getString(R.string.train_passenger_header_title),
                        1, getView().getString(R.string.train_select_passenger_adult_title)));
        updateDataPassengers(trainPassengerViewModel);
    }

    private List<TrainPassengerViewModel> initPassenger(int adultPassengers, int infantPassengers) {
        List<TrainPassengerViewModel> trainPassengerViewModelList = new ArrayList<>();
        int passengerId = 1;
        for (int i = 1; i <= adultPassengers; i++) {
            TrainPassengerViewModel trainPassengerViewModel = new TrainPassengerViewModel();
            trainPassengerViewModel.setPassengerId(passengerId);
            trainPassengerViewModel.setPaxType(TrainBookingPassenger.ADULT);
            trainPassengerViewModel.setHeaderTitle(
                    formatPassengerHeader(getView().getString(R.string.train_passenger_header_title),
                            passengerId, getView().getString(R.string.train_select_passenger_adult_title)));
            trainPassengerViewModelList.add(trainPassengerViewModel);
            passengerId++;
        }

        for (int i = 1; i <= infantPassengers; i++) {
            TrainPassengerViewModel trainPassengerViewModel = new TrainPassengerViewModel();
            trainPassengerViewModel.setPassengerId(passengerId);
            trainPassengerViewModel.setPaxType(TrainBookingPassenger.INFANT);
            trainPassengerViewModel.setHeaderTitle(
                    formatPassengerHeader(getView().getString(R.string.train_passenger_header_title),
                            passengerId, getView().getString(R.string.train_select_passenger_infant_title)));
            trainPassengerViewModelList.add(trainPassengerViewModel);
            passengerId++;
        }

        return trainPassengerViewModelList;
    }

    private String formatPassengerHeader(String prefix, int number, String postix) {
        return String.format(getView().getString(R.string.train_passenger_header_format),
                prefix,
                number,
                postix
        );
    }

    @Override
    public void detachView() {
        getDetailScheduleUseCase.unsubscribe();
        trainSoftBookingUseCase.unsubscribe();
        super.detachView();
    }
}
