package com.tokopedia.tokocash.balance.data.mapper;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.core.drawer2.data.pojo.AbTag;
import com.tokopedia.core.drawer2.data.pojo.Wallet;
import com.tokopedia.remote_config.FirebaseRemoteConfigImpl;
import com.tokopedia.remote_config.RemoteConfig;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.remote_config.RemoteConfigKey;
import com.tokopedia.tokocash.balance.view.ActionBalance;
import com.tokopedia.tokocash.balance.view.BalanceTokoCash;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 1/4/18.
 */

public class BalanceTokoCashMapper implements Func1<Wallet, BalanceTokoCash> {

    private RemoteConfig remoteConfig;

    @Inject
    public BalanceTokoCashMapper(@ApplicationContext Context context) {
        remoteConfig = new FirebaseRemoteConfigImpl(context);
    }

    @Override
    public BalanceTokoCash call(Wallet wallet) {
        if (wallet != null) {
            BalanceTokoCash balanceTokoCash = new BalanceTokoCash();

            //create an object if tokocash is not activated
            if (!wallet.getLinked()) {
                String applinkActivation = remoteConfig.getString(RemoteConfigKey.MAINAPP_WALLET_APPLINK_REGISTER);
                if (applinkActivation.isEmpty()) {
                    applinkActivation = wallet.getAction().getApplinks();
                }
                ActionBalance action = new ActionBalance();
                action.setApplinks(applinkActivation);
                action.setVisibility(wallet.getAction().getVisibility());

                String labelActionName = remoteConfig.getString(RemoteConfigKey.MAINAPP_WALLET_LABEL_REGISTER);
                if (labelActionName.isEmpty()) {
                    labelActionName = wallet.getAction().getText();
                }
                action.setLabelAction(labelActionName);

                String labelName = remoteConfig.getString(RemoteConfigKey.MAINAPP_WALLET_LABEL_NAME);
                if (labelName.isEmpty()) {
                    labelName = wallet.getText();
                }
                balanceTokoCash.setTitleText(labelName);

                balanceTokoCash.setBalance(wallet.getBalance());
                balanceTokoCash.setApplinks(wallet.getApplinks());
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

            String applinkBalance = remoteConfig.getString(RemoteConfigKey.MAINAPP_WALLET_APPLINK);
            if (applinkBalance.isEmpty()) {
                applinkBalance = wallet.getApplinks();
            }
            balanceTokoCash.setApplinks(applinkBalance);
            balanceTokoCash.setBalance(wallet.getBalance());
            balanceTokoCash.setHoldBalance(wallet.getHoldBalance());
            balanceTokoCash.setLink(wallet.getLinked());
            balanceTokoCash.setRawBalance(wallet.getRawBalance());
            balanceTokoCash.setRawHoldBalance(wallet.getRawHoldBalance());
            balanceTokoCash.setRawTotalBalance(wallet.getRawTotalBalance());
            balanceTokoCash.setRedirectUrl(wallet.getRedirectUrl());
            balanceTokoCash.setTotalBalance(wallet.getTotalBalance());

            String labelName = remoteConfig.getString(RemoteConfigKey.MAINAPP_WALLET_LABEL_NAME);
            if (labelName.isEmpty()) {
                labelName = wallet.getText();
            }
            balanceTokoCash.setTitleText(labelName);

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