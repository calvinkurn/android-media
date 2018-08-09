package com.tokopedia.navigation.presentation.view;

import android.content.Context;

/**
 * Created by meta on 25/07/18.
 */
public interface LoadDataView {

    void onStartLoading();

    void onError(String message);

    void onHideLoading();

    Context getContext();
}
