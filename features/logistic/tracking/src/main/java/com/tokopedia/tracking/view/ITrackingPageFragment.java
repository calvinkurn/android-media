package com.tokopedia.tracking.view;

import com.tokopedia.tracking.viewmodel.TrackingViewModel;

/**
 * Created by kris on 5/14/18. Tokopedia
 */

public interface ITrackingPageFragment {

    void populateView(TrackingViewModel viewModel);

    void showMainLoadingPage();

    void closeMainLoadingPage();

    void showLoading();

    void hideLoading();

    void showError(String message);

}
