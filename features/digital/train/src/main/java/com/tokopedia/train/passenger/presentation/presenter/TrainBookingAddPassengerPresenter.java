package com.tokopedia.train.passenger.presentation.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.passenger.presentation.contract.TrainBookingAddPassengerContract;
import com.tokopedia.train.passenger.data.TrainBookingPassenger;
import com.tokopedia.train.passenger.data.TrainPassengerTitle;
import com.tokopedia.train.passenger.presentation.viewmodel.TrainPassengerViewModel;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 26/06/18.
 */
public class TrainBookingAddPassengerPresenter extends BaseDaggerPresenter<TrainBookingAddPassengerContract.View>
        implements TrainBookingAddPassengerContract.Presenter {

    private static final int MAX_CONTACT_NAME = 60;
    private static final int MAX_IDENTITY_NUMBER = 20;
    private static final int MAX_PHONE_NUMBER = 15;

    @Inject
    public TrainBookingAddPassengerPresenter() {
    }

    @Override
    public void submitDataPassenger(TrainPassengerViewModel trainPassengerViewModel) {
        if (isAllDataValid()) {
            trainPassengerViewModel.setSalutationTitle(getView().getSalutationTitle());
            trainPassengerViewModel.setSalutationId(getSalutationId());
            trainPassengerViewModel.setName(getView().getContactName());
            if (getView().getPaxType() == TrainBookingPassenger.ADULT) {
                trainPassengerViewModel.setPhone(getView().getPhoneNumber());
                trainPassengerViewModel.setIdentityNumber(getView().getIdentityNumber());
            }
            getView().navigateToBookingPassenger(trainPassengerViewModel);
        }
    }

    private boolean isAllDataValid() {
        boolean allDataValid = true;
        if (TextUtils.isEmpty(getView().getSalutationTitle())) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.train_passenger_error_salutation);
        } else if (TextUtils.isEmpty(getView().getContactName())) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.train_passenger_error_contact_name);
        } else if (getView().getContactName().length() > MAX_CONTACT_NAME) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.train_passenger_contact_name_max);
        } else if (TextUtils.isEmpty(getView().getPhoneNumber()) && getView().getPaxType() == TrainBookingPassenger.ADULT) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.train_passenger_error_phone_number);
        } else if (TextUtils.isEmpty(getView().getIdentityNumber()) && getView().getPaxType() == TrainBookingPassenger.ADULT) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.train_passenger_error_identity_number);
        }  else if (getView().getPhoneNumber().length() > MAX_PHONE_NUMBER) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.train_passenger_contact_phone_max_length_error);
        } else if (getView().getIdentityNumber().length() > MAX_IDENTITY_NUMBER && getView().getPaxType() == TrainBookingPassenger.ADULT) {
            allDataValid = false;
            getView().showMessageErrorInSnackBar(R.string.train_passenger_error_identity_number_max);
        }

        return allDataValid;
    }

    private int getSalutationId() {
        switch (getView().getSpinnerPosition()) {
            case 0:
                return TrainPassengerTitle.TUAN;
            case 1:
                if (getView().getPaxType() == TrainBookingPassenger.INFANT) {
                    return TrainPassengerTitle.NONA;
                } else {
                    return TrainPassengerTitle.NYONYA;
                }
            case 2:
                return TrainPassengerTitle.NONA;
            default:
                return 0;
        }
    }
}
