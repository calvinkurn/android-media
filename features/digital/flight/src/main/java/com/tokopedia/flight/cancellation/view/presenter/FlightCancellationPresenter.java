package com.tokopedia.flight.cancellation.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.domain.FlightCancellationGetCancelablePassengerUseCase;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationContract;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationJourney;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationPassengerViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.flight.common.util.FlightPassengerTitleType.NONA;
import static com.tokopedia.flight.common.util.FlightPassengerTitleType.NYONYA;
import static com.tokopedia.flight.common.util.FlightPassengerTitleType.TUAN;

/**
 * @author by furqan on 21/03/18.
 */

public class FlightCancellationPresenter extends BaseDaggerPresenter<FlightCancellationContract.View>
        implements FlightCancellationContract.Presenter {

    FlightCancellationGetCancelablePassengerUseCase flightCancellationGetCancelablePassengerUseCase;

    @Inject
    public FlightCancellationPresenter(FlightCancellationGetCancelablePassengerUseCase flightCancellationGetCancelablePassengerUseCase) {
        this.flightCancellationGetCancelablePassengerUseCase = flightCancellationGetCancelablePassengerUseCase;
    }

    @Override
    public void onViewCreated() {
        getCancelablePassenger();
    }

    @Override
    public void onNextButtonClicked() {
        boolean canGoToNext = false;
        boolean isRefundable = false;

        for (FlightCancellationViewModel item : getView().getSelectedCancellationViewModel()) {
            if (item.getPassengerViewModelList().size() > 0) {
                canGoToNext = true;
            }

            if (item.getFlightCancellationJourney().isRefundable()) {
                isRefundable = true;
            }
        }

        if (canGoToNext && isRefundable) {
            getView().navigateToReasonAndProofPage();
        } else if (canGoToNext && !isRefundable) {
            getView().navigateToReasonAndProofPage();
        } else {
            getView().showShouldChooseAtLeastOnePassengerError();
        }
    }

    @Override
    public void checkPassenger(FlightCancellationPassengerViewModel passengerViewModel, int position) {
        FlightCancellationViewModel flightCancellationViewModel = getView().getSelectedCancellationViewModel().get(position);
        if (!flightCancellationViewModel.getPassengerViewModelList().contains(passengerViewModel)) {
            flightCancellationViewModel.getPassengerViewModelList().add(passengerViewModel);
            if (passengerViewModel.getRelations().size() > 0) {
                checkAllRelations(passengerViewModel);

                if (getView().isFirstRelationCheck()) {
                    getView().showAutoCheckDialog();
                }
            }
        }

        init();
    }

    @Override
    public void uncheckPassenger(FlightCancellationPassengerViewModel passengerViewModel, int position) {
        FlightCancellationViewModel flightCancellationViewModel = getView().getSelectedCancellationViewModel().get(position);
        flightCancellationViewModel.getPassengerViewModelList().remove(passengerViewModel);

        if (passengerViewModel.getRelations().size() > 0) {
            uncheckAllRelations(passengerViewModel);
        }

        init();
    }

    @Override
    public boolean isPassengerChecked(FlightCancellationPassengerViewModel passengerViewModel) {
        for (FlightCancellationViewModel item : getView().getSelectedCancellationViewModel()) {
            if (item.getPassengerViewModelList().contains(passengerViewModel)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canGoNext() {
        boolean canGoToNext = false;

        if (getView().getSelectedCancellationViewModel() != null) {
            for (FlightCancellationViewModel item : getView().getSelectedCancellationViewModel()) {
                if (item.getPassengerViewModelList().size() > 0) {
                    canGoToNext = true;
                }
            }
        }

        return canGoToNext;
    }

    @Override
    public void init() {
        if (canGoNext()) {
            getView().enableNextButton();
        } else {
            getView().disableNextButton();
        }
    }

    private void getCancelablePassenger() {
        getView().showFullLoading();
        this.flightCancellationGetCancelablePassengerUseCase.execute(
                flightCancellationGetCancelablePassengerUseCase.generateRequestParams(
                        getView().getInvoiceId()
                ),
                new Subscriber<List<FlightCancellationViewModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        if (isViewAttached()) {
                            getView().showGetListError(throwable);
                        }
                    }

                    @Override
                    public void onNext(List<FlightCancellationViewModel> flightCancellationViewModels) {
                        transformJourneyToCancellationViewModel(flightCancellationViewModels);
                    }
                }
        );
    }

    private void transformJourneyToCancellationViewModel(List<FlightCancellationViewModel> flightCancellationViewModelList) {
        List<FlightCancellationViewModel> selectedViewModel = new ArrayList<>();
        List<FlightCancellationViewModel> cancellationModelList = new ArrayList<>();
        Map<String, FlightCancellationPassengerViewModel> passengerRelations = new HashMap<>();

        for (FlightCancellationViewModel item : flightCancellationViewModelList) {
            for (FlightCancellationJourney journeyItem : getView().getFlightCancellationJourney()) {
                if (item.getFlightCancellationJourney().getJourneyId().equals(journeyItem.getJourneyId())) {
                    FlightCancellationViewModel flightCancellationViewModel = new FlightCancellationViewModel();
                    flightCancellationViewModel.setFlightCancellationJourney(journeyItem);
                    flightCancellationViewModel.setPassengerViewModelList(transformPassengerList(item.getPassengerViewModelList()));
                    cancellationModelList.add(flightCancellationViewModel);

                    FlightCancellationViewModel cancellationForSelectedViewModelList = new FlightCancellationViewModel();
                    cancellationForSelectedViewModelList.setFlightCancellationJourney(journeyItem);
                    cancellationForSelectedViewModelList.setPassengerViewModelList(new ArrayList<FlightCancellationPassengerViewModel>());
                    selectedViewModel.add(cancellationForSelectedViewModelList);

                    passengerRelations.putAll(buildPassengerRelationsMap(flightCancellationViewModel.getPassengerViewModelList()));
                }
            }
        }

        getView().getPassengerRelations().putAll(passengerRelations);
        getView().setSelectedCancellationViewModel(selectedViewModel);
        getView().setFlightCancellationViewModel(cancellationModelList);
        getView().renderCancelableList();
    }

    private Map<String, FlightCancellationPassengerViewModel> buildPassengerRelationsMap(List<FlightCancellationPassengerViewModel> passengerList) {
        Map<String, FlightCancellationPassengerViewModel> passengerRelations = new HashMap<>();

        for (FlightCancellationPassengerViewModel item : passengerList) {
            passengerRelations.put(item.getRelationId(), item);
        }

        return passengerRelations;
    }

    private List<FlightCancellationPassengerViewModel> transformPassengerList(List<FlightCancellationPassengerViewModel> passengerList) {
        for (FlightCancellationPassengerViewModel item : passengerList) {
            item.setTitleString(getTitleString(item.getTitle()));
        }

        return passengerList;
    }

    private String getTitleString(int typeId) {
        switch (typeId) {
            case TUAN:
                return getView().getString(R.string.mister);
            case NYONYA:
                return getView().getString(R.string.misiz);
            case NONA:
                return getView().getString(R.string.miss);
            default:
                return getView().getString(R.string.mister);
        }
    }

    private void checkAllRelations(FlightCancellationPassengerViewModel passengerViewModel) {

        for (String relationId : passengerViewModel.getRelations()) {
            FlightCancellationPassengerViewModel relationPassenger = getView().getPassengerRelations().get(relationId);

            for (FlightCancellationViewModel item : getView().getSelectedCancellationViewModel()) {
                if (relationId.contains(item.getFlightCancellationJourney().getJourneyId()) &&
                        !item.getPassengerViewModelList().contains(relationPassenger)) {
                    item.getPassengerViewModelList().add(relationPassenger);
                }
            }
        }
    }

    private void uncheckAllRelations(FlightCancellationPassengerViewModel passengerViewModel) {
        for (String relationId : passengerViewModel.getRelations()) {
            FlightCancellationPassengerViewModel relationPassenger = getView().getPassengerRelations().get(relationId);
            for (FlightCancellationViewModel item : getView().getSelectedCancellationViewModel()) {
                item.getPassengerViewModelList().remove(relationPassenger);
            }
        }
    }
}
