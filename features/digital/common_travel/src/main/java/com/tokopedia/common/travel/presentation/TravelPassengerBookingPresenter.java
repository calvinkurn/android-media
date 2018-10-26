package com.tokopedia.common.travel.presentation;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.domain.GetTravelPassengersUseCase;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;
import com.tokopedia.common.travel.utils.typedef.TravelBookingPassenger;
import com.tokopedia.common.travel.utils.typedef.TravelPassengerTitle;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 26/06/18.
 */
public class TravelPassengerBookingPresenter extends BaseDaggerPresenter<TravelPassengerBookingContract.View>
        implements TravelPassengerBookingContract.Presenter {
    private static final String PASSENGER_NAME_REGEX = "^[a-zA-Z\\s]*$";
    private static final String PASSENGER_ID_NUMBER_REGEX = "^[A-Za-z0-9]+$";
    private static final int MAX_CONTACT_NAME = 60;
    private static final int MAX_IDENTITY_NUMBER = 20;
    private static final int MIN_IDENTITY_NUMBER = 5;
    private static final int MAX_PHONE_NUMBER = 15;

    private GetTravelPassengersUseCase getTravelPassengersUseCase;

    @Inject
    public TravelPassengerBookingPresenter(GetTravelPassengersUseCase getTravelPassengersUseCase) {
        this.getTravelPassengersUseCase = getTravelPassengersUseCase;
    }

    @Override
    public void submitDataPassenger(TravelPassenger trainPassengerViewModel) {
        if (isAllDataValid()) {
            trainPassengerViewModel.setSalutationTitle(getView().getSalutationTitle());
            trainPassengerViewModel.setTitle(getSalutationId());
            trainPassengerViewModel.setName(getView().getContactName());
            if (getView().getPaxType() == TravelBookingPassenger.ADULT) {
                trainPassengerViewModel.setIdNumber(getView().getIdentityNumber());
            }
            getView().navigateToBookingPassenger(trainPassengerViewModel);
        }
    }

    private boolean isAllDataValid() {
        boolean allDataValid = true;
        if (TextUtils.isEmpty(getView().getSalutationTitle())) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_error_salutation);
        } else if (TextUtils.isEmpty(getView().getContactName())) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_error_contact_name);
        } else if (getView().getContactName().length() > MAX_CONTACT_NAME) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_contact_name_max);
        } else if (!getView().getContactName().matches(PASSENGER_NAME_REGEX)) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_contact_name_containt_alphabet);
        } else if (TextUtils.isEmpty(getView().getPhoneNumber()) && getView().getPaxType() == TravelBookingPassenger.ADULT) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_error_phone_number);
        } else if (TextUtils.isEmpty(getView().getIdentityNumber()) && getView().getPaxType() == TravelBookingPassenger.ADULT) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_error_identity_number);
        } else if (getView().getPhoneNumber().length() > MAX_PHONE_NUMBER) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_contact_phone_max_length_error);
        } else if (getView().getIdentityNumber().length() < MIN_IDENTITY_NUMBER && getView().getPaxType() == TravelBookingPassenger.ADULT) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_error_identity_number_min);
        } else if (getView().getIdentityNumber().length() > MAX_IDENTITY_NUMBER && getView().getPaxType() == TravelBookingPassenger.ADULT) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_error_identity_number_max);
        } else if (!getView().getIdentityNumber().matches(PASSENGER_ID_NUMBER_REGEX) && getView().getPaxType() == TravelBookingPassenger.ADULT) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.travel_passenger_error_identity_alphanumeric);
        }

        return allDataValid;
    }

    private int getSalutationId() {
        switch (getView().getSpinnerPosition()) {
            case 0:
                return TravelPassengerTitle.TUAN;
            case 1:
                if (getView().getPaxType() == TravelBookingPassenger.INFANT) {
                    return TravelPassengerTitle.NONA;
                } else {
                    return TravelPassengerTitle.NYONYA;
                }
            case 2:
                return TravelPassengerTitle.NONA;
            default:
                return 0;
        }
    }

    @Override
    public void onDestroyView() {
        detachView();
        getTravelPassengersUseCase.unsubscribe();
    }
}
