package com.tokopedia.tkpd.selling.presenter;

import com.tokopedia.tkpd.customadapter.RetryDataBinder;

/**
 * Created by Alifa on 10/12/2016.
 */

public interface TrackingFragmentPresenter {

    void loadTrackingData(String orderId);

    void onDestroyView();
}
