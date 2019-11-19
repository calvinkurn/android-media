package com.tokopedia.logisticaddaddress.features.addaddress;

import android.content.Context;

import com.tokopedia.logisticdata.data.entity.address.Destination;

import rx.subscriptions.CompositeSubscription;

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

        void showErrorSnackbar(String errorMessage);

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
