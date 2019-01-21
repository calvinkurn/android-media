package com.tokopedia.common.travel.presentation.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.usecase.RequestParams;

import java.util.Date;

/**
 * Created by nabillasabbaha on 25/06/18.
 */
public interface TravelPassengerUpdateContract {

    interface View extends CustomerView {

        int getPaxType();

        String getSalutationTitle();

        String getFirstName();

        String getLastName();

        String getIdentityNumber();

        String getBirthdate();

        void showMessageErrorInSnackBar(int resId);

        void showMessageErrorInSnackBar(Throwable throwable);

        void navigateToPassengerList();

        int getSpinnerPosition();

        RequestParams getRequestParamAddPassenger();

        RequestParams getRequestParamEditPassenger();

        int getTravelPlatformType();

        void showBirthdateChange(Date dateSelected);

        Date getUpperBirthDate();

        Date getLowerBirthDate();

    }

    interface Presenter extends CustomerPresenter<View> {

        void submitAddPassengerData();

        void submitEditPassengerData(String idPassenger);

        void onChangeBirthdate(int year, int month, int dayOfMonth);

        void onDestroyView();
    }
}
