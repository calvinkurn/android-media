package com.tokopedia.tokocash.balance.data.mapper;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.tokocash.CacheUtil;
import com.tokopedia.tokocash.TokoCashRouter;
import com.tokopedia.tokocash.balance.data.entity.AbTagEntity;
import com.tokopedia.tokocash.balance.data.entity.BalanceTokoCashEntity;
import com.tokopedia.tokocash.balance.view.ActionBalance;
import com.tokopedia.tokocash.balance.view.BalanceTokoCash;
import com.tokopedia.user.session.UserSession;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 1/4/18.
 */

public class BalanceTokoCashMapper implements Func1<BalanceTokoCashEntity, BalanceTokoCash> {
    private static final String OVO_TYPE = "OVO";
    private Context context;
    private UserSession userSession;

    @Inject
    public BalanceTokoCashMapper(@ApplicationContext Context context, UserSession userSession) {
        this.context = context;
        this.userSession = userSession;
    }

    @Override
    public BalanceTokoCash call(BalanceTokoCashEntity balanceTokoCashEntity) {
        if (balanceTokoCashEntity != null) {
            BalanceTokoCash balanceTokoCash = new BalanceTokoCash();

            //create an object if tokocash is not activated
            if (!balanceTokoCashEntity.getLinked()) {
                LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, CacheUtil.KEY_POPUP_INTRO_OVO_CACHE);
                boolean popupHasShown = true;
                if (userSession.isLoggedIn() && 
                    balanceTokoCashEntity.getWalletType() != null && 
                    balanceTokoCashEntity.getWalletType().equalsIgnoreCase(OVO_TYPE)){
                    popupHasShown = localCacheHandler.getBoolean(CacheUtil.FIRST_TIME_POPUP, false);
                    if (!popupHasShown) {
                        localCacheHandler.putBoolean(CacheUtil.FIRST_TIME_POPUP, true);
                        localCacheHandler.applyEditor();
                    }
                }
                balanceTokoCash.setShowAnnouncement(balanceTokoCashEntity.isShowAnnouncement() && !popupHasShown);

                String applinkActivation = ((TokoCashRouter) context).getStringRemoteConfig(RemoteConfigKey.MAINAPP_WALLET_APPLINK_REGISTER);
                if (applinkActivation.isEmpty()) {
                    applinkActivation = balanceTokoCashEntity.getAction().getApplinks();
                }
                ActionBalance action = new ActionBalance();
                action.setApplinks(applinkActivation);
                action.setVisibility(balanceTokoCashEntity.getAction().getVisibility());

                String labelActionName = ((TokoCashRouter) context).getStringRemoteConfig(RemoteConfigKey.MAINAPP_WALLET_LABEL_REGISTER);
                if (labelActionName.isEmpty()) {
                    labelActionName = balanceTokoCashEntity.getAction().getText();
                }
                action.setLabelAction(labelActionName);

                String labelName = ((TokoCashRouter) context).getStringRemoteConfig(RemoteConfigKey.MAINAPP_WALLET_LABEL_NAME);
                if (labelName.isEmpty()) {
                    labelName = balanceTokoCashEntity.getText();
                }
                balanceTokoCash.setTitleText(labelName);

                balanceTokoCash.setBalance(balanceTokoCashEntity.getBalance());
                balanceTokoCash.setApplinks(balanceTokoCashEntity.getApplinks());
                balanceTokoCash.setActionBalance(action);
                balanceTokoCash.setWalletType(balanceTokoCashEntity.getWalletType());
                balanceTokoCash.setHelpApplink(balanceTokoCashEntity.getHelpApplink());
                balanceTokoCash.setTncApplink(balanceTokoCashEntity.getTncApplink());
                return balanceTokoCash;
            }

            if (balanceTokoCashEntity.getAction() != null) {
                ActionBalance actionBalance = new ActionBalance();
                actionBalance.setApplinks(balanceTokoCashEntity.getAction().getApplinks());
                actionBalance.setLabelAction(balanceTokoCashEntity.getAction().getText());
                actionBalance.setRedirectUrl(balanceTokoCashEntity.getAction().getRedirectUrl());
                actionBalance.setVisibility(balanceTokoCashEntity.getAction().getVisibility());
                balanceTokoCash.setActionBalance(actionBalance);
            }

            String applinkBalance = ((TokoCashRouter) context).getStringRemoteConfig(RemoteConfigKey.MAINAPP_WALLET_APPLINK);
            if (applinkBalance.isEmpty()) {
                applinkBalance = balanceTokoCashEntity.getApplinks();
            }
            balanceTokoCash.setApplinks(applinkBalance);
            balanceTokoCash.setBalance(balanceTokoCashEntity.getBalance());
            balanceTokoCash.setHoldBalance(balanceTokoCashEntity.getHoldBalance());
            balanceTokoCash.setLink(balanceTokoCashEntity.getLinked());
            balanceTokoCash.setRawBalance(balanceTokoCashEntity.getRawBalance());
            balanceTokoCash.setRawHoldBalance(balanceTokoCashEntity.getRawHoldBalance());
            balanceTokoCash.setRawTotalBalance(balanceTokoCashEntity.getRawTotalBalance());
            balanceTokoCash.setRedirectUrl(balanceTokoCashEntity.getRedirectUrl());
            balanceTokoCash.setTotalBalance(balanceTokoCashEntity.getTotalBalance());

            String labelName = ((TokoCashRouter) context).getStringRemoteConfig(RemoteConfigKey.MAINAPP_WALLET_LABEL_NAME);
            if (labelName.isEmpty()) {
                labelName = balanceTokoCashEntity.getText();
            }
            balanceTokoCash.setTitleText(labelName);

            //set ab tags
            ArrayList<String> abTags = new ArrayList<>();
            if (balanceTokoCashEntity.getAbTags() != null) {
                int index = 0;
                for (AbTagEntity abtag : balanceTokoCashEntity.getAbTags()) {
                    abTags.add(abtag.getTag());
                    index++;
                }
            }
            balanceTokoCash.setAbTags(abTags);
            balanceTokoCash.setPointBalance(balanceTokoCashEntity.getPointBalance());
            balanceTokoCash.setRawPointBalance(balanceTokoCashEntity.getRawPointBalance());
            balanceTokoCash.setCashBalance(balanceTokoCashEntity.getCashBalance());
            balanceTokoCash.setRawCashBalance(balanceTokoCashEntity.getRawCashBalance());
            balanceTokoCash.setWalletType(balanceTokoCashEntity.getWalletType());

            return balanceTokoCash;
        }
        throw new RuntimeException("Error");
    }
}
