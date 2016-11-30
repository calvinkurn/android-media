package com.tokopedia.core.rescenter.create.listener;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.rescenter.create.model.passdata.ActionParameterPassData;
import com.tokopedia.core.rescenter.create.model.responsedata.CreateResCenterFormData;

/**
 * Created on 6/16/16.
 */
public interface ChooseTroubleListener {

    void openSolutionFragment();

    void showLoading(boolean isShow);

    void showMainView(boolean isShow);

    void renderData(CreateResCenterFormData data);

    void setErrorFullView(String error, NetworkErrorHelper.RetryClickedListener clickedListener);

    void setTimeOutFullView(NetworkErrorHelper.RetryClickedListener clickedListener);

    void finishActivity();

    void showErrorMessage(String string);

    ActionParameterPassData collectInputData();

    void openProductDetailTroubleFragment();

    void showChooseTrouble(boolean isVisible);

    void showChooseProduct(boolean isVisible);
}
