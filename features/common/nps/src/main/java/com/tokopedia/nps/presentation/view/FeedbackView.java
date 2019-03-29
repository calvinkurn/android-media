package com.tokopedia.nps.presentation.view;

import android.content.Context;

/**
 * Created by meta on 28/06/18.
 */
public interface FeedbackView {

    void showLoading();

    void hideLoading();

    void showRetry();

    void hideRetry();

    void showError(String message);

    Context context();

    void successPostFeedback();
}
