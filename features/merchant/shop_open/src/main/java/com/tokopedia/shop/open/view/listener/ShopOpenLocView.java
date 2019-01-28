package com.tokopedia.shop.open.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.district_recommendation.domain.model.Token;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;

/**
 * Created by normansyahputa on 1/2/18.
 */

public interface ShopOpenLocView extends CustomerView {
    void updateStepperModel();

    void goToNextPage(Object object);

    void navigateToGoogleMap(String generatedMap, LocationPass locationPass);

    void navigateToDistrictRecommendation(Token token);

    void onErrorGetReserveDomain(Throwable e);

    void onFailedSaveInfoShop(Throwable t);

    void showProgressDialog();

    void dismissProgressDialog();
}
