package com.tokopedia.logisticorder.view;

import com.tokopedia.logisticorder.viewmodel.TrackingUiModel;

/**
 * Created by kris on 5/14/18. Tokopedia
 */

public interface ITrackingPageFragment {

    void populateView(TrackingUiModel uiModel);

    void showLoading();

    void hideLoading();

    void showError(Throwable error);

    void showSoftError(Throwable error);

    void setRetryButton(boolean active, long deadline);

    void startSuccessCountdown();

}
