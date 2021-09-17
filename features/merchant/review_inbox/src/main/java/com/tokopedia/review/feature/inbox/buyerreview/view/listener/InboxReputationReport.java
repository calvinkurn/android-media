package com.tokopedia.review.feature.inbox.buyerreview.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;


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
