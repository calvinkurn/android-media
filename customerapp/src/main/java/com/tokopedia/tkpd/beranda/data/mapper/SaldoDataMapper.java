package com.tokopedia.tkpd.beranda.data.mapper;

import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashModel;
import com.tokopedia.core.drawer2.data.pojo.toppoints.TopPointsData;
import com.tokopedia.core.drawer2.data.pojo.toppoints.TopPointsModel;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.var.TokoCashTypeDef;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.SaldoViewModel;

import rx.functions.Func2;

/**
 * Created by errysuprayogi on 12/5/17.
 */
public class SaldoDataMapper implements Func2<TokoCashModel, TopPointsModel, SaldoViewModel> {

    @Override
    public SaldoViewModel call(TokoCashModel tokoCashModel, TopPointsModel topPointsModel) {
        SaldoViewModel cashViewModel = new SaldoViewModel();
        if (tokoCashModel.isSuccess() && tokoCashModel.getTokoCashData() != null) {
            TokoCashData tokoCashData = tokoCashModel.getTokoCashData();
            SaldoViewModel.ItemModel tokoCash = new SaldoViewModel.ItemModel();
            tokoCash.setIcon(R.drawable.ic_tokocash_icon);
            tokoCash.setTitle(tokoCashData.getText());
            if (tokoCashData.getLink() == TokoCashTypeDef.TOKOCASH_ACTIVE) {
                tokoCash.setSubtitle(tokoCashData.getBalance());
                tokoCash.setApplinks(getAppLinkBalance(tokoCashData));
                tokoCash.setRedirectUrl(tokoCashData.getRedirectUrl());
            } else {
                tokoCash.setSubtitle(tokoCashData.getAction().getText());
                tokoCash.setApplinks(tokoCashData.getAction().getmAppLinks());
                tokoCash.setRedirectUrl(tokoCashData.getAction().getRedirectUrl());
            }
            cashViewModel.addItem(tokoCash);
        }
        //TODO replace with hachiko
        if (topPointsModel.isSuccess() && topPointsModel.getTopPointsData() != null) {
            TopPointsData pointsData = topPointsModel.getTopPointsData();
            SaldoViewModel.ItemModel topPoint = new SaldoViewModel.ItemModel();
            topPoint.setIcon(R.drawable.ic_logo_toppoint);
            topPoint.setTitle("TopPoint");
            topPoint.setSubtitle(pointsData.getLoyaltyPoint().getAmount());
            cashViewModel.addItem(topPoint);
        }
        return cashViewModel;
    }

    private String getAppLinkBalance(TokoCashData tokoCashData){
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
        return appLinkBalance;
    }
}
