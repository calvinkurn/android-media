package com.tokopedia.tkpd.tkpdreputation.inbox.view.listener;

import android.content.Context;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * @author by nisie on 9/13/17.
 */

public interface InboxReputationReport {

    interface View extends CustomerView {

        void showLoadingProgress();

        void onErrorReportReview(String errorMessage);

        void onSuccessReportReview();

        void removeLoadingProgress();

        Context getContext();
    }

    interface Presenter extends CustomerPresenter<View> {

        void reportReview(String reviewId, String shopId, int checkedRadioButtonId, String otherReason);
    }
}
