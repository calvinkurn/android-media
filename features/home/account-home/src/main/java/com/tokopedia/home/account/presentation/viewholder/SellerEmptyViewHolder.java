package com.tokopedia.home.account.presentation.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.gm.resource.GMConstant;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.view.InfoCardView;
import com.tokopedia.home.account.presentation.viewmodel.SellerEmptyViewModel;

/**
 * @author by alvinatin on 14/08/18.
 */

public class SellerEmptyViewHolder extends AbstractViewHolder<SellerEmptyViewModel> {
    @LayoutRes
    public static int LAYOUT = R.layout.fragment_seller_account_empty;

    private InfoCardView topadsInfo;
    private InfoCardView gmInfo;
    private InfoCardView sellerCenterInfo;
    private TextView btnLearnMore;
    private Button btnOpenShop;
    private AccountItemListener listener;

    public SellerEmptyViewHolder(View itemView, AccountItemListener listener) {
        super(itemView);
        this.listener = listener;
        topadsInfo = itemView.findViewById(R.id.topads_info);
        gmInfo = itemView.findViewById(R.id.gm_info);
        sellerCenterInfo = itemView.findViewById(R.id.seller_center_info);
        btnLearnMore = itemView.findViewById(R.id.btn_learn_more);
        btnOpenShop = itemView.findViewById(R.id.btn_open_shop);
    }

    @Override
    public void bind(SellerEmptyViewModel element) {
        topadsInfo.setImage(R.drawable.ic_topads);
        topadsInfo.setMainText(R.string.title_menu_topads);
        topadsInfo.setSecondaryText(R.string.topads_desc);

        gmInfo.setImage(GMConstant.getGMDrawableResource(itemView.getContext()));
        gmInfo.setMainText(GMConstant.getGMTitleResource(itemView.getContext()));
        gmInfo.setSecondaryText(R.string.gold_merchant_desc);

        sellerCenterInfo.setImage(R.drawable.ic_seller_center);
        sellerCenterInfo.setMainText(R.string.seller_center);
        sellerCenterInfo.setSecondaryText(R.string.seller_center_desc);

        topadsInfo.setOnClickListener(v -> listener.onTopadsInfoClicked());

        gmInfo.setOnClickListener(v -> listener.onGMInfoClicked());

        sellerCenterInfo.setOnClickListener(v -> listener.onSellerCenterInfoClicked());

        btnOpenShop.setOnClickListener(v -> listener.onOpenShopClicked());

        btnLearnMore.setOnClickListener(v -> listener.onLearnMoreSellerClicked());

    }
}
