package com.tokopedia.tkpd.rescenter.edit.listener;

import android.support.v7.widget.RecyclerView;

import com.tokopedia.tkpd.rescenter.edit.model.responsedata.ActionParameterPassData;

/**
 * Created on 8/26/16.
 */
public interface BuyerEditProductListener {

    ActionParameterPassData getPassData();

    void showErrorMessage(String message);

    RecyclerView getProductRecyclerView();

    RecyclerView.Adapter getAdapter();

    void openSolutionFragment();
}
