package com.tokopedia.core.util;

import android.content.Context;

import com.tokopedia.core.R;
import com.tokopedia.core.drawer2.data.pojo.UserDrawerData;
import com.tokopedia.core.drawer2.data.pojo.Wallet;
import com.tokopedia.core.drawer2.data.pojo.topcash.Action;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCashAction;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerWalletAction;
import com.tokopedia.core.drawer2.data.viewmodel.HomeHeaderWalletAction;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.var.TokoCashTypeDef;

import java.util.ArrayList;

import static com.tokopedia.core.gcm.Constants.Applinks.WALLET_ACTIVATION;

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

    public static DrawerTokoCash convertToViewModel(Wallet tokoCashData, Context context) {
        DrawerTokoCash drawerTokoCash = new DrawerTokoCash();
        drawerTokoCash.setDrawerTokoCashAction(convertToActionViewModel(tokoCashData.getAction()));
        drawerTokoCash.setHomeHeaderWalletAction(convertToActionHomeHeader(tokoCashData, context));
        drawerTokoCash.setDrawerWalletAction(convertToActionDrawer(tokoCashData, context));
        return drawerTokoCash;
    }

    private static HomeHeaderWalletAction convertToActionHomeHeader(Wallet tokoCashData, Context context) {
        HomeHeaderWalletAction data = new HomeHeaderWalletAction();
        String appLinkBalance = tokoCashData.getApplinks();
        if (appLinkBalance != null) {
            if (!appLinkBalance.contains(Constants.AppLinkQueryParameter.WALLET_TOP_UP_VISIBILITY)) {
                appLinkBalance = tokoCashData.getAction().getVisibility() != null
                        && tokoCashData.getAction().getVisibility().equals("1")
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
        data.setLabelActionButton(tokoCashData.getAction().getText());
        data.setVisibleActionButton(tokoCashData.getAction().getVisibility() != null
                && tokoCashData.getAction().getVisibility().equals("1"));
        data.setTypeAction(tokoCashData.getLinked() ? HomeHeaderWalletAction.TYPE_ACTION_TOP_UP
                : HomeHeaderWalletAction.TYPE_ACTION_ACTIVATION);

        if(tokoCashData.getLinked()) {
            data.setAppLinkActionButton(tokoCashData.getAction().getApplinks() == null ? ""
                    : tokoCashData.getAction().getApplinks());
        }else{
            data.setAppLinkActionButton(WALLET_ACTIVATION);
            data.setLabelActionButton(context.getString(R.string.title_activation));
            data.setLabelTitle(context.getString(R.string.label_tokocash));
        }

        data.setRedirectUrlActionButton(tokoCashData.getAction().getRedirectUrl() == null ? ""
                : tokoCashData.getAction().getRedirectUrl());


        ArrayList<String> abTags = new ArrayList<>();
        if (tokoCashData.getAbTags() != null) {
            int index = 0;
            for (Object abtag : tokoCashData.getAbTags()) {
                abTags.add(abtag.toString());
                index++;
            }
        }
        data.setAbTags(abTags);
        return data;
    }

    private static DrawerWalletAction convertToActionDrawer(Wallet tokoCashData, Context context) {
        String appLinkBalance = tokoCashData.getApplinks();
        if (appLinkBalance != null) {
            if (!appLinkBalance.contains(Constants.AppLinkQueryParameter.WALLET_TOP_UP_VISIBILITY)) {
                appLinkBalance = tokoCashData.getAction().getVisibility() != null
                        && tokoCashData.getAction().getVisibility().equals("1")
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
        data.setLabelActionButton(tokoCashData.getAction().getText());
        data.setVisibleActionButton(tokoCashData.getAction().getVisibility() != null
                && tokoCashData.getAction().getVisibility().equals("1"));
        data.setTypeAction(tokoCashData.getLinked() ? DrawerWalletAction.TYPE_ACTION_BALANCE
                : DrawerWalletAction.TYPE_ACTION_ACTIVATION);
        data.setAppLinkActionButton(tokoCashData.getAction().getApplinks() == null ? ""
                : tokoCashData.getAction().getApplinks());
        data.setRedirectUrlActionButton(tokoCashData.getAction().getRedirectUrl() == null ? ""
                : tokoCashData.getAction().getRedirectUrl());

        if(tokoCashData.getLinked()) {
            data.setAppLinkActionButton(tokoCashData.getAction().getApplinks() == null ? ""
                    : tokoCashData.getAction().getApplinks());
        }else{
            data.setAppLinkActionButton(WALLET_ACTIVATION);
            data.setLabelActionButton(context.getString(R.string.title_activation));
            data.setLabelTitle(context.getString(R.string.label_tokocash));
        }

        return data;
    }

    private static DrawerTokoCashAction convertToActionViewModel(com.tokopedia.core.drawer2.data.pojo.Action action) {
        if (action == null) {
            return null;
        }

        DrawerTokoCashAction drawerTokoCashAction = new DrawerTokoCashAction();
        drawerTokoCashAction.setText(action.getText());
        drawerTokoCashAction.setRedirectUrl(action.getRedirectUrl());
        return drawerTokoCashAction;
    }
}
