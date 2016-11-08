package com.tokopedia.core.rescenter.create.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.core.rescenter.create.model.passdata.ActionParameterPassData;

/**
 * Created on 6/16/16.
 */
public interface ChooseSolutionPresenter {

    void setOnSubmitClick(@NonNull Context context);

    void onFirstTimeLaunched(@NonNull Context context, ActionParameterPassData passData);

    void processResultCreateResCenter(int resultCode, Bundle resultData);

    void unSubscribe();

    void onAbortCreateResolution();
}
