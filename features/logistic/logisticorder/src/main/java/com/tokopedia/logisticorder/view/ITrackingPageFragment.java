package com.tokopedia.logisticorder.view;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.logisticorder.uimodel.TrackingUiModel;

/**
 * Created by kris on 5/14/18. Tokopedia
 */

public interface ITrackingPageFragment extends CustomerView {

    void populateView(TrackingUiModel uiModel);

    void showLoading();

    void hideLoading();

    void showError(Throwable error);

    void showSoftError(Throwable error);

    void setRetryButton(boolean active, long deadline);

    void startSuccessCountdown();

}
