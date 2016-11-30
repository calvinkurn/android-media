package com.tokopedia.core.rescenter.create.listener;

import com.tokopedia.core.rescenter.create.model.passdata.ActionParameterPassData;

/**
 * Created on 8/2/16.
 */
public interface ChooseProductTroubleListener {

    ActionParameterPassData collectInputData();

    void showErrorMessage(String message);

    void openSolutionFragment();

}
