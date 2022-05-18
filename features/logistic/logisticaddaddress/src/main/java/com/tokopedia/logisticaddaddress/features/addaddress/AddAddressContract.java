package com.tokopedia.logisticaddaddress.features.addaddress;

import android.content.Context;

import com.tokopedia.logisticCommon.data.entity.address.Destination;
import com.tokopedia.logisticaddaddress.data.entity.OldEditAddressResponseData;

/**
 * Created by fajarnuha on 03/12/18.
 */
public interface AddAddressContract {

    interface View {

        boolean isEdit();

        boolean isDistrictRecommendation();

        void showLoading();

        void finishLoading();

        void finishActivity();

        void finishActivityAfterEdit(OldEditAddressResponseData data);

        void showErrorToaster(String errorMessage);

        boolean isValidAddress();

        Destination getAddress();

        void setAddress(Destination address);

        void errorSaveAddress();

        void successSaveAddress();

        void setPinpointAddress(String address);

        void stopPerformaceMonitoring();

    }

    interface Presenter {

        void attachView(View view);

        void detachView();

        void saveAddress();

        void requestReverseGeoCode(Context context, Destination destination);

    }
}
