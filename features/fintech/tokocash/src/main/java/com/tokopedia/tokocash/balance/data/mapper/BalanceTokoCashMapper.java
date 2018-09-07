package com.tokopedia.tokocash.balance.data.mapper;

import com.tokopedia.core.drawer2.data.pojo.AbTag;
import com.tokopedia.core.drawer2.data.pojo.Wallet;
import com.tokopedia.tokocash.balance.view.ActionBalance;
import com.tokopedia.tokocash.balance.view.BalanceTokoCash;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 1/4/18.
 */

public class BalanceTokoCashMapper implements Func1<Wallet, BalanceTokoCash> {

    @Inject
    public BalanceTokoCashMapper() {
    }

    @Override
    public BalanceTokoCash call(Wallet wallet) {
        if (wallet != null) {
            BalanceTokoCash balanceTokoCash = new BalanceTokoCash();

            //create an object if tokocash is not activated
            if (!wallet.getLinked()) {
                ActionBalance action = new ActionBalance();
                action.setLabelAction("Aktivasi TokoCash");
                action.setApplinks("tokopedia://wallet/activation");
                balanceTokoCash.setBalance("");
                balanceTokoCash.setTitleText("TokoCash");
                balanceTokoCash.setActionBalance(action);
                return balanceTokoCash;
            }

            if (wallet.getAction() != null) {
                ActionBalance actionBalance = new ActionBalance();
                actionBalance.setApplinks(wallet.getAction().getApplinks());
                actionBalance.setLabelAction(wallet.getAction().getText());
                actionBalance.setRedirectUrl(wallet.getAction().getRedirectUrl());
                actionBalance.setVisibility(wallet.getAction().getVisibility());
                balanceTokoCash.setActionBalance(actionBalance);
            }

            balanceTokoCash.setApplinks(wallet.getApplinks());
            balanceTokoCash.setBalance(wallet.getBalance());
            balanceTokoCash.setHoldBalance(wallet.getHoldBalance());
            balanceTokoCash.setLink(wallet.getLinked() ? 1 : 0);
            balanceTokoCash.setRawBalance(wallet.getRawBalance());
            balanceTokoCash.setRawHoldBalance(wallet.getRawHoldBalance());
            balanceTokoCash.setRawTotalBalance(wallet.getRawTotalBalance());
            balanceTokoCash.setRedirectUrl(wallet.getRedirectUrl());
            balanceTokoCash.setTitleText(wallet.getText());
            balanceTokoCash.setTotalBalance(wallet.getTotalBalance());

            //set ab tags
            ArrayList<String> abTags = new ArrayList<>();
            if (wallet.getAbTags() != null) {
                int index = 0;
                for (AbTag abtag : wallet.getAbTags()) {
                    abTags.add(abtag.getTag());
                    index++;
                }
            }
            balanceTokoCash.setAbTags(abTags);


            return balanceTokoCash;
        }
        throw new RuntimeException("Error");
    }
}