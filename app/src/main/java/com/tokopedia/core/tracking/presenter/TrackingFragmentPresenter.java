package com.tokopedia.core.tracking.presenter;

/**
 * Created by Alifa on 10/12/2016.
 */

public interface TrackingFragmentPresenter {

    void loadTrackingData(String orderId);

    void onDestroyView();
}
