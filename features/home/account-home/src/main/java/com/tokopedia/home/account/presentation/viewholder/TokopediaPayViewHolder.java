package com.tokopedia.home.account.presentation.viewholder;

import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.account.AccountHomeUrl;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.view.TokopediaPayCardView;
import com.tokopedia.home.account.presentation.viewmodel.TokopediaPayViewModel;

/**
 * @author okasurya on 7/19/18.
 */
public class TokopediaPayViewHolder extends AbstractViewHolder<TokopediaPayViewModel> {
    @LayoutRes
    public static int LAYOUT = R.layout.item_tokopedia_pay;

    private TokopediaPayCardView tokopediaPayCardView;
    private AccountItemListener listener;

    public TokopediaPayViewHolder(View itemView, @NonNull AccountItemListener listener) {
        super(itemView);
        tokopediaPayCardView = itemView.findViewById(R.id.view_tokopedia_pay);
        this.listener = listener;
    }

    @Override
    public void bind(TokopediaPayViewModel element) {
        String imageUrl;
        imageUrl = listener.getRemoteConfig().getString(AccountHomeUrl.ImageUrl.KEY_IMAGE_HOST, AccountHomeUrl.CDN_URL);
        imageUrl = imageUrl + AccountHomeUrl.CDN_IMAGE_PATH;
        if(AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
            tokopediaPayCardView.setBackgroundImage(imageUrl);
        }
        tokopediaPayCardView.setTextAmountLeft(element.getAmountLeft());
        if (element.isLinked()) {
            tokopediaPayCardView.setAmountColorLeft(com.tokopedia.unifyprinciples.R.color.Unify_N700_96);
        } else {
            tokopediaPayCardView.setAmountColorLeft(com.tokopedia.unifyprinciples.R.color.Unify_G400);
        }

        if(element.getBsDataCentre() == null) {
            tokopediaPayCardView.setCenterLayoutVisibility(View.GONE);
        } else {
            tokopediaPayCardView.setCenterLayoutVisibility(View.VISIBLE);
        }

        tokopediaPayCardView.setTextDescLeft(element.getLabelLeft());

        tokopediaPayCardView.setTextAmountCentre(element.getAmountCentre());
        tokopediaPayCardView.setTextDesctCentre(element.getLabelCentre());

        tokopediaPayCardView.setTextAmountRight(element.getAmountRight(), element.isRightImportant());
        tokopediaPayCardView.setTextDesctRight(element.getLabelRight());

        tokopediaPayCardView.setIconLeft(element.getIconUrlLeft());
        tokopediaPayCardView.setIconCentre(element.getIconUrlCentre());
        tokopediaPayCardView.setIconRight(element.getIconUrlRight());

        tokopediaPayCardView.setActionTextClickListener(v -> listener.onTokopediaPayLinkClicked());
        tokopediaPayCardView.setLeftItemClickListener(v -> listener.onTokopediaPayLeftItemClicked(
                element.getLabelLeft(),
                element.getApplinkLeft(),
                element.getBsDataLeft(),
                element.isLinked(),
                element.getWalletType()));
        tokopediaPayCardView.setCentreItemClickListener(v -> listener.onTokopediaPayCentreItemClicked(
                element.getLabelCentre(),
                element.getApplinkCentre(),
                element.getBsDataCentre(),
                element.isLinked(),
                element.getWalletType()));
        tokopediaPayCardView.setRightItemClickListener(v -> listener.onTokopediaPayRightItemClicked(
                element.isRightSaldo(),
                element.getLabelRight(),
                "",
                element.getApplinkRight(),
                element.getBsDataRight()));
    }
}
