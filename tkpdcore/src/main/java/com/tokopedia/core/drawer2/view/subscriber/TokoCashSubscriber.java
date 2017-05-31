package com.tokopedia.core.drawer2.view.subscriber;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.R;
import com.tokopedia.core.drawer2.data.pojo.topcash.Action;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashModel;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCashAction;
import com.tokopedia.core.drawer2.view.DrawerDataListener;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;

import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * Created by nisie on 5/5/17.
 */

public class TokoCashSubscriber extends Subscriber<TokoCashModel> {
    private final DrawerDataListener viewListener;

    public TokoCashSubscriber(DrawerDataListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorGetTokoCash(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(TokoCashModel tokoCashModel) {
        if(tokoCashModel.isSuccess())
            viewListener.onGetTokoCash(convertToViewModel(tokoCashModel.getTokoCashData()));
        else 
            viewListener.onErrorGetTokoCash(tokoCashModel.getErrorMessage());
    }

    private DrawerTokoCash convertToViewModel(TokoCashData tokoCashData) {
        DrawerTokoCash drawerTokoCash = new DrawerTokoCash();
        drawerTokoCash.setBalance(tokoCashData.getBalance());
        drawerTokoCash.setLink(tokoCashData.getLink());
        drawerTokoCash.setRedirectUrl(tokoCashData.getRedirectUrl());
        drawerTokoCash.setText(tokoCashData.getText());
        drawerTokoCash.setDrawerTokoCashAction(convertToActionViewModel(tokoCashData.getAction()));
        return drawerTokoCash;
    }

    private DrawerTokoCashAction convertToActionViewModel(Action action) {
        DrawerTokoCashAction drawerTokoCashAction = new DrawerTokoCashAction();
        drawerTokoCashAction.setText(action.getText());
        drawerTokoCashAction.setRedirectUrl(action.getRedirectUrl());
        return drawerTokoCashAction;
    }
}

