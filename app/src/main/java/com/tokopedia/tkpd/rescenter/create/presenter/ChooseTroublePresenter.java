package com.tokopedia.tkpd.rescenter.create.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.tkpd.rescenter.create.model.passdata.ActionParameterPassData;

/**
 * Created on 6/16/16.
 */
public interface ChooseTroublePresenter {

    void setOnChooseSolutionClick(@NonNull Context context);

    void setOnFirstTimeLaunched(@NonNull Context context, ActionParameterPassData passData);

    void onsubscribe();
}
