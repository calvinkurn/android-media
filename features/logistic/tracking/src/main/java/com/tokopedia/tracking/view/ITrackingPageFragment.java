package com.tokopedia.tracking.view;

import com.tokopedia.tracking.viewmodel.TrackingViewModel;

/**
 * Created by kris on 5/14/18. Tokopedia
 */

public interface ITrackingPageFragment {

    void populateView(TrackingViewModel viewModel);

    void showLoading();

    void hideLoading();

    void showError(Throwable error);

    void showSoftError(Throwable error);

    void setRetryButton(boolean active, long deadline);

    void startSuccessCountdown();

}
