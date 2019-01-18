package com.tokopedia.home.account.presentation.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.view.InfoCardView;
import com.tokopedia.home.account.presentation.viewmodel.InfoCardViewModel;

/**
 * @author okasurya on 7/24/18.
 */
public class InfoCardViewHolder extends AbstractViewHolder<InfoCardViewModel> {
    @LayoutRes
    public static int LAYOUT = R.layout.item_announcement_card;

    private InfoCardView infoCardView;
    private AccountItemListener listener;

    public InfoCardViewHolder(View itemView, AccountItemListener listener) {
        super(itemView);
        this.infoCardView = itemView.findViewById(R.id.view_info_card);
        this.listener = listener;
    }

    @Override
    public void bind(InfoCardViewModel element) {
        infoCardView.setImage(element.getIconRes());
        infoCardView.setMainText(element.getMainText());
        infoCardView.setSecondaryText(element.getSecondaryText());
        infoCardView.setOnClickListener(v -> listener.onInfoCardClicked(element, getAdapterPosition()));
        infoCardView.setTextNewVisiblity(element.isNewTxtVisiblle());
    }
}
