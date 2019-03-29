package com.tokopedia.tkpd.tokocash;

import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction;
import com.tokopedia.tokocash.balance.view.BalanceTokoCash;

import java.util.ArrayList;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 21/09/18.
 */
public class TokoCashHomeBalanceMapper implements Func1<BalanceTokoCash, HomeHeaderWalletAction> {

    @Override
    public HomeHeaderWalletAction call(BalanceTokoCash balanceTokoCash) {
        HomeHeaderWalletAction data = new HomeHeaderWalletAction();
        data.setLinked(balanceTokoCash.getLink());
        data.setBalance(balanceTokoCash.getBalance());
        data.setLabelTitle(balanceTokoCash.getTitleText());
        data.setAppLinkBalance(balanceTokoCash.getApplinks());
        data.setLabelActionButton(balanceTokoCash.getActionBalance().getLabelAction());
        data.setVisibleActionButton(balanceTokoCash.getActionBalance().getVisibility() != null
                && balanceTokoCash.getActionBalance().getVisibility().equals("1"));
        data.setAppLinkActionButton(balanceTokoCash.getActionBalance().getApplinks());
        data.setAbTags(balanceTokoCash.getAbTags() == null ? new ArrayList<String>()
                : balanceTokoCash.getAbTags());
        data.setPointBalance(balanceTokoCash.getPointBalance());
        data.setRawPointBalance(balanceTokoCash.getRawPointBalance());
        data.setCashBalance(balanceTokoCash.getCashBalance());
        data.setRawCashBalance(balanceTokoCash.getRawCashBalance());
        data.setWalletType(balanceTokoCash.getWalletType());
        data.setShowAnnouncement(balanceTokoCash.isShowAnnouncement());
        return data;
    }
}
