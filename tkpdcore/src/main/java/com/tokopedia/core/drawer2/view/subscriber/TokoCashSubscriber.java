package com.tokopedia.core.drawer2.view.subscriber;

import com.tokopedia.core.R;
import com.tokopedia.core.drawer2.data.pojo.topcash.Action;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashModel;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCashAction;
import com.tokopedia.core.drawer2.data.viewmodel.HomeHeaderWalletAction;
import com.tokopedia.core.drawer2.view.DrawerDataListener;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;

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
        if (tokoCashModel.isSuccess())
            viewListener.onGetTokoCash(
                    convertToViewModel(
                            tokoCashModel.getTokoCashData()));
        else
            viewListener.onErrorGetTokoCash(
                    viewListener.getString(R.string.default_request_error_unknown));
    }

    private DrawerTokoCash convertToViewModel(TokoCashData tokoCashData) {
        DrawerTokoCash drawerTokoCash = new DrawerTokoCash();
        drawerTokoCash.setBalance(tokoCashData.getBalance());
        drawerTokoCash.setLink(tokoCashData.getLink());
        drawerTokoCash.setRedirectUrl(tokoCashData.getRedirectUrl());
        drawerTokoCash.setText(tokoCashData.getText());
        drawerTokoCash.setDrawerTokoCashAction(convertToActionViewModel(tokoCashData.getAction()));
        drawerTokoCash.setHomeHeaderWalletAction(convertToActionHomeHeader(tokoCashData));
        return drawerTokoCash;
    }

    private HomeHeaderWalletAction convertToActionHomeHeader(TokoCashData tokoCashData) {
        HomeHeaderWalletAction data = new HomeHeaderWalletAction();
        data.setLabelTitle(tokoCashData.getText());
        data.setAppLinkBalance(tokoCashData.getmAppLinks() == null ? "" : tokoCashData.getmAppLinks());
        data.setRedirectUrlBalance(tokoCashData.getRedirectUrl() == null ? "" : tokoCashData.getRedirectUrl());
        data.setBalance(tokoCashData.getBalance());
        data.setLabelActionButton(tokoCashData.getAction().getmText());
        data.setVisibleActionButton(tokoCashData.getAction().getmVisibility() != null
                && tokoCashData.getAction().getmVisibility().equals("1"));
        data.setTypeAction(tokoCashData.getLink() == 1 ? HomeHeaderWalletAction.TYPE_ACTION_TOP_UP
                : HomeHeaderWalletAction.TYPE_ACTION_ACTIVATION);
        data.setAppLinkActionButton(tokoCashData.getAction().getmAppLinks() == null ? ""
                : tokoCashData.getAction().getmAppLinks());
        data.setRedirectUrlActionButton(tokoCashData.getAction().getRedirectUrl() == null ? ""
                : tokoCashData.getAction().getRedirectUrl());
        return data;
    }

    private DrawerTokoCashAction convertToActionViewModel(Action action) {
        DrawerTokoCashAction drawerTokoCashAction = new DrawerTokoCashAction();
        drawerTokoCashAction.setText(action.getText());
        drawerTokoCashAction.setRedirectUrl(action.getRedirectUrl());
        return drawerTokoCashAction;
    }
}

