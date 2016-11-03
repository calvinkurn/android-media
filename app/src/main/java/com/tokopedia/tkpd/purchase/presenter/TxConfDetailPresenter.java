package com.tokopedia.tkpd.purchase.presenter;

import android.content.Context;

import com.tokopedia.tkpd.purchase.model.response.txconfirmation.TxConfData;

/**
 * Created by Angga.Prasetiyo on 15/06/2016.
 */
public interface TxConfDetailPresenter {

    void processCancelTransaction(Context context, TxConfData txConfData);

    void processConfirmTransaction(Context context, TxConfData txConfData);

    void onDestroyView();

}
