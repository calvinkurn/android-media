package com.tokopedia.logisticorder.presenter;

/**
 * Created by kris on 5/14/18. Tokopedia
 */

public interface ITrackingPagePresenter {

    void onGetTrackingData(String orderId);

    void onGetRetryAvailability(String orderId);

    void onRetryPickup(String orderId);

    void onDetach();

}
