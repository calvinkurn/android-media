package com.tokopedia.tracking.presenter;

/**
 * Created by kris on 5/14/18. Tokopedia
 */

public interface ITrackingPagePresenter {

    void onGetTrackingData(String orderId);

    void onDetach();

}
