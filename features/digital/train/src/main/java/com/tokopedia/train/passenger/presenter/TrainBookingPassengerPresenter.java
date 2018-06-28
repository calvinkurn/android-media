package com.tokopedia.train.passenger.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.design.component.CardWithAction;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.util.TrainDateUtil;
import com.tokopedia.train.passenger.contract.TrainBookingPassengerContract;
import com.tokopedia.train.passenger.data.TrainBookingPassenger;
import com.tokopedia.train.passenger.viewmodel.ProfileBuyerInfo;
import com.tokopedia.train.passenger.viewmodel.TrainPassengerViewModel;
import com.tokopedia.train.search.domain.GetDetailScheduleUseCase;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

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

    private CompositeSubscription compositeSubscription;
    private GetDetailScheduleUseCase getDetailScheduleUseCase;

    @Inject
    public TrainBookingPassengerPresenter(GetDetailScheduleUseCase getDetailScheduleUseCase) {
        this.getDetailScheduleUseCase = getDetailScheduleUseCase;
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

                        getView().setBirthdate(
                                TrainDateUtil.dateToString(
                                        TrainDateUtil.stringToDate(profileBuyerInfo.getBday()),
                                        TrainDateUtil.DEFAULT_FORMAT));
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
                    } else {
                        getView().showDepartureTripInfo();
                        getView().setCityRouteTripInfo(cardWithAction,
                                getView().getOriginCity(), getView().getDestinationCity());
                        getView().hideReturnTripInfo();
                    }
                    getView().loadDetailSchedule(viewModel, cardWithAction);
                }
            }
        });
    }

    @Override
    public void processInitPassengers(int adultPassengers, int infantPassengers) {
        List<TrainPassengerViewModel> passengerViewModelList = initPassenger(adultPassengers, infantPassengers);
        getView().setCurrentListPassenger(passengerViewModelList);
        getView().renderPassenger(passengerViewModelList);
    }

    @Override
    public void updateDataPassengers(TrainPassengerViewModel trainPassengerViewModel) {
        List<TrainPassengerViewModel> passengerViewModels = getView().getCurrentPassengerList();
        int indexPassenger = passengerViewModels.indexOf(trainPassengerViewModel);
        if (indexPassenger != -1) {
            passengerViewModels.set(indexPassenger, trainPassengerViewModel);
        }
        getView().renderPassenger(passengerViewModels);
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
                            i, getView().getString(R.string.train_select_passenger_adult_title)));
            trainPassengerViewModelList.add(trainPassengerViewModel);
            passengerId++;
        }

        for (int i = 1; i <= infantPassengers; i++) {
            TrainPassengerViewModel trainPassengerViewModel = new TrainPassengerViewModel();
            trainPassengerViewModel.setPassengerId(passengerId);
            trainPassengerViewModel.setPaxType(TrainBookingPassenger.INFANT);
            trainPassengerViewModel.setHeaderTitle(
                    formatPassengerHeader(getView().getString(R.string.train_passenger_header_title),
                            i, getView().getString(R.string.train_select_passenger_infant_title)));
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
}
