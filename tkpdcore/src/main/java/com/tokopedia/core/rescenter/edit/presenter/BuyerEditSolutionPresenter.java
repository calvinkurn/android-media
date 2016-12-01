package com.tokopedia.core.rescenter.edit.presenter;

import android.content.Context;

import com.drew.lang.annotations.NotNull;
import com.tokopedia.core.rescenter.edit.model.responsedata.ActionParameterPassData;

/**
 * Created on 8/26/16.
 */
public interface BuyerEditSolutionPresenter {

    void setOnSubmitClick(@NotNull Context context);

    void onFirstTimeLaunched(@NotNull Context context, ActionParameterPassData passData);

    void unsubscribe();
}
