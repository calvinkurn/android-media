package com.tokopedia.digital.tokocash.listener;

import android.app.Application;
import android.content.Intent;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.tokocash.model.tokocashitem.TokoCashBalanceData;

/**
 * Created by nabillasabbaha on 8/21/17.
 */

public interface TopUpTokoCashListener {

    void showProgressLoading();

    void hideProgressLoading();

    void showToastMessage(String message);

    void renderBalanceTokoCash(TokoCashBalanceData tokoCashBalanceData);

    void showEmptyPage();

    void renderTopUpDataTokoCash(CategoryData categoryData);

    TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams
    );

    Application getMainApplication();

    void navigateToActivityRequest(Intent intent, int requestCode);

    String getVersionInfoApplication();

    String getUserLoginId();
}
