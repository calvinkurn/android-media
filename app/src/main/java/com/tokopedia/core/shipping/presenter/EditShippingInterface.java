package com.tokopedia.core.shipping.presenter;

import android.os.Parcelable;

import com.tokopedia.core.network.NetworkHandler;
import com.tokopedia.core.shipping.model.EditShippingModel;

import java.util.ArrayList;

/**
 * Created by Kris on 11/6/2015.
 */
public interface EditShippingInterface {

    String JNE_TAG = "JNE";
    String TIKI_TAG = "TIKI";
    String POS_TAG = "POS";


    void fetchData();

    void prepareCourierSelections();

    void sendData(ArrayList<Boolean> checkBoxStatus);

    void requestCourierParameters(ArrayList<Boolean> checkBoxStatus, NetworkHandler network);

    void updateShopParameters(Parcelable shop);

    EditShippingModel.ParamEditShop getUpdatedShopParameters();

    void districtSpinnerChanged(int provinceSpinnerPosition, int citySpinnerPosition, int districtSpinnerPosition);

    void citySpinnerChanged(int citySpinnerPosition, int provinceSpinnerPosition);

    void provinceSpinnerChanged(int position);

    void setOkeActivatedProperties(boolean checkBoxState);

    void getOkeActivatedProperties(boolean isJNEOkeChecked);

    void setMapPosition(String longitude, String latitude);

    void additionalOptionChanged();

    String getMapLatitude();

    String getMapLongitude();

    String getWhiteListStatus();
}