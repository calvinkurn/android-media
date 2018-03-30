package com.tokopedia.core.util;

import com.tokopedia.anals.ConsumerDrawerData;
import com.tokopedia.core.drawer2.data.pojo.topcash.Action;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCashAction;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerWalletAction;
import com.tokopedia.core.drawer2.data.viewmodel.HomeHeaderWalletAction;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.var.TokoCashTypeDef;

import java.util.ArrayList;

/**
 * Created by nabillasabbaha on 11/13/17.
 */

public class TokoCashUtil {

    public static DrawerTokoCash convertToViewModel(TokoCashData tokoCashData) {
        DrawerTokoCash drawerTokoCash = new DrawerTokoCash();
        drawerTokoCash.setDrawerTokoCashAction(convertToActionViewModel(tokoCashData.getAction()));
        drawerTokoCash.setHomeHeaderWalletAction(convertToActionHomeHeader(tokoCashData));
        drawerTokoCash.setDrawerWalletAction(convertToActionDrawer(tokoCashData));
        return drawerTokoCash;
    }

    private static HomeHeaderWalletAction convertToActionHomeHeader(TokoCashData tokoCashData) {
        HomeHeaderWalletAction data = new HomeHeaderWalletAction();
        String appLinkBalance = tokoCashData.getmAppLinks();
        if (appLinkBalance != null) {
            if (!appLinkBalance.contains(Constants.AppLinkQueryParameter.WALLET_TOP_UP_VISIBILITY)) {
                appLinkBalance = tokoCashData.getAction().getmVisibility() != null
                        && tokoCashData.getAction().getmVisibility().equals("1")
                        ? appLinkBalance + "?"
                        + Constants.AppLinkQueryParameter.WALLET_TOP_UP_VISIBILITY + "=true"
                        : appLinkBalance + "?"
                        + Constants.AppLinkQueryParameter.WALLET_TOP_UP_VISIBILITY + "=false";
            }
        }
        data.setLabelTitle(tokoCashData.getText());

        data.setAppLinkBalance(appLinkBalance == null ? "" : appLinkBalance);
        data.setRedirectUrlBalance(tokoCashData.getRedirectUrl() == null ? "" : tokoCashData.getRedirectUrl());
        data.setBalance(tokoCashData.getBalance());
        data.setLabelActionButton(tokoCashData.getAction().getmText());
        data.setVisibleActionButton(tokoCashData.getAction().getmVisibility() != null
                && tokoCashData.getAction().getmVisibility().equals("1"));
        data.setTypeAction(tokoCashData.getLink() == TokoCashTypeDef.TOKOCASH_ACTIVE ? HomeHeaderWalletAction.TYPE_ACTION_TOP_UP
                : HomeHeaderWalletAction.TYPE_ACTION_ACTIVATION);
        data.setAppLinkActionButton(tokoCashData.getAction().getmAppLinks() == null ? ""
                : tokoCashData.getAction().getmAppLinks());
        data.setRedirectUrlActionButton(tokoCashData.getAction().getRedirectUrl() == null ? ""
                : tokoCashData.getAction().getRedirectUrl());
        data.setAbTags(tokoCashData.getAbTags() == null ? new ArrayList<String>()
                : tokoCashData.getAbTags());
        return data;
    }

    private static DrawerWalletAction convertToActionDrawer(TokoCashData tokoCashData) {
        String appLinkBalance = tokoCashData.getmAppLinks();
        if (appLinkBalance != null) {
            if (!appLinkBalance.contains(Constants.AppLinkQueryParameter.WALLET_TOP_UP_VISIBILITY)) {
                appLinkBalance = tokoCashData.getAction().getmVisibility() != null
                        && tokoCashData.getAction().getmVisibility().equals("1")
                        ? appLinkBalance + "?" +
                        Constants.AppLinkQueryParameter.WALLET_TOP_UP_VISIBILITY + "=true"
                        : appLinkBalance + "?" +
                        Constants.AppLinkQueryParameter.WALLET_TOP_UP_VISIBILITY + "=false";
            }
        }
        DrawerWalletAction data = new DrawerWalletAction();
        data.setLabelTitle(tokoCashData.getText());

        data.setAppLinkBalance(appLinkBalance == null ? "" : appLinkBalance);
        data.setRedirectUrlBalance(tokoCashData.getRedirectUrl() == null ? "" : tokoCashData.getRedirectUrl());
        data.setBalance(tokoCashData.getBalance());
        data.setLabelActionButton(tokoCashData.getAction().getmText());
        data.setVisibleActionButton(tokoCashData.getAction().getmVisibility() != null
                && tokoCashData.getAction().getmVisibility().equals("1"));
        data.setTypeAction(tokoCashData.getLink() == TokoCashTypeDef.TOKOCASH_ACTIVE ? DrawerWalletAction.TYPE_ACTION_BALANCE
                : DrawerWalletAction.TYPE_ACTION_ACTIVATION);
        data.setAppLinkActionButton(tokoCashData.getAction().getmAppLinks() == null ? ""
                : tokoCashData.getAction().getmAppLinks());
        data.setRedirectUrlActionButton(tokoCashData.getAction().getRedirectUrl() == null ? ""
                : tokoCashData.getAction().getRedirectUrl());
        return data;
    }

    private static DrawerTokoCashAction convertToActionViewModel(Action action) {
        DrawerTokoCashAction drawerTokoCashAction = new DrawerTokoCashAction();
        drawerTokoCashAction.setText(action.getmText());
        drawerTokoCashAction.setRedirectUrl(action.getRedirectUrl());
        return drawerTokoCashAction;
    }

    public static DrawerTokoCash convertToViewModel(ConsumerDrawerData.Data.Wallet tokoCashData) {
        DrawerTokoCash drawerTokoCash = new DrawerTokoCash();
        drawerTokoCash.setDrawerTokoCashAction(convertToActionViewModel(tokoCashData.action()));
        drawerTokoCash.setHomeHeaderWalletAction(convertToActionHomeHeader(tokoCashData));
        drawerTokoCash.setDrawerWalletAction(convertToActionDrawer(tokoCashData));
        return drawerTokoCash;
    }

    private static HomeHeaderWalletAction convertToActionHomeHeader(ConsumerDrawerData.Data.Wallet tokoCashData) {
        HomeHeaderWalletAction data = new HomeHeaderWalletAction();
        String appLinkBalance = tokoCashData.applinks();
        if (appLinkBalance != null) {
            if (!appLinkBalance.contains(Constants.AppLinkQueryParameter.WALLET_TOP_UP_VISIBILITY)) {
                appLinkBalance = tokoCashData.action().visibility() != null
                        && tokoCashData.action().visibility().equals("1")
                        ? appLinkBalance + "?"
                        + Constants.AppLinkQueryParameter.WALLET_TOP_UP_VISIBILITY + "=true"
                        : appLinkBalance + "?"
                        + Constants.AppLinkQueryParameter.WALLET_TOP_UP_VISIBILITY + "=false";
            }
        }
        data.setLabelTitle(tokoCashData.text());

        data.setAppLinkBalance(appLinkBalance == null ? "" : appLinkBalance);
        data.setRedirectUrlBalance(tokoCashData.redirect_url() == null ? "" : tokoCashData.redirect_url());
        data.setBalance(tokoCashData.balance());
        data.setLabelActionButton(tokoCashData.action().text());
        data.setVisibleActionButton(tokoCashData.action().visibility() != null
                && tokoCashData.action().visibility().equals("1"));
        data.setTypeAction(tokoCashData.linked() ? HomeHeaderWalletAction.TYPE_ACTION_TOP_UP
                : HomeHeaderWalletAction.TYPE_ACTION_ACTIVATION);
        data.setAppLinkActionButton(tokoCashData.action().applinks() == null ? ""
                : tokoCashData.action().applinks());
        data.setRedirectUrlActionButton(tokoCashData.action().redirect_url() == null ? ""
                : tokoCashData.action().redirect_url());


        ArrayList<String> abTags = new ArrayList<>();
        if (tokoCashData.ab_tags() != null) {
            int index = 0;
            for (ConsumerDrawerData.Data.Ab_tag abtag : tokoCashData.ab_tags()) {
                abTags.add(abtag.toString());
                index++;
            }
        }
        data.setAbTags(abTags);
        return data;
    }

    private static DrawerWalletAction convertToActionDrawer(ConsumerDrawerData.Data.Wallet tokoCashData) {
        String appLinkBalance = tokoCashData.applinks();
        if (appLinkBalance != null) {
            if (!appLinkBalance.contains(Constants.AppLinkQueryParameter.WALLET_TOP_UP_VISIBILITY)) {
                appLinkBalance = tokoCashData.action().visibility() != null
                        && tokoCashData.action().visibility().equals("1")
                        ? appLinkBalance + "?" +
                        Constants.AppLinkQueryParameter.WALLET_TOP_UP_VISIBILITY + "=true"
                        : appLinkBalance + "?" +
                        Constants.AppLinkQueryParameter.WALLET_TOP_UP_VISIBILITY + "=false";
            }
        }
        DrawerWalletAction data = new DrawerWalletAction();
        data.setLabelTitle(tokoCashData.text());

        data.setAppLinkBalance(appLinkBalance == null ? "" : appLinkBalance);
        data.setRedirectUrlBalance(tokoCashData.redirect_url() == null ? "" : tokoCashData.redirect_url());
        data.setBalance(tokoCashData.balance());
        data.setLabelActionButton(tokoCashData.action().text());
        data.setVisibleActionButton(tokoCashData.action().visibility() != null
                && tokoCashData.action().visibility().equals("1"));
        data.setTypeAction(tokoCashData.linked() ? DrawerWalletAction.TYPE_ACTION_BALANCE
                : DrawerWalletAction.TYPE_ACTION_ACTIVATION);
        data.setAppLinkActionButton(tokoCashData.action().applinks() == null ? ""
                : tokoCashData.action().applinks());
        data.setRedirectUrlActionButton(tokoCashData.action().redirect_url() == null ? ""
                : tokoCashData.action().redirect_url());
        return data;
    }

    private static DrawerTokoCashAction convertToActionViewModel(ConsumerDrawerData.Data.Action action) {
        if (action == null) {
            return null;
        }

        DrawerTokoCashAction drawerTokoCashAction = new DrawerTokoCashAction();
        drawerTokoCashAction.setText(action.text());
        drawerTokoCashAction.setRedirectUrl(action.redirect_url());
        return drawerTokoCashAction;
    }
}
