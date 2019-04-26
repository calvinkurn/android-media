package com.tokopedia.home.account.presentation.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
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

    private static String URL_EMPTY = "https://ecs7.tokopedia.net/img/android/seller_dashboard/seller_dashboard.png";

    private TextView btnLearnMore;
    private Button btnOpenShop;
    private ImageView ivEmptyImage;
    private AccountItemListener listener;

    public SellerEmptyViewHolder(View itemView, AccountItemListener listener) {
        super(itemView);
        this.listener = listener;
        btnLearnMore = itemView.findViewById(R.id.btn_learn_more);
        btnOpenShop = itemView.findViewById(R.id.btn_open_shop);
        ivEmptyImage = itemView.findViewById(R.id.iv_empty_image);
    }

    @Override
    public void bind(SellerEmptyViewModel element) {
        ImageHandler.LoadImage(ivEmptyImage, URL_EMPTY);
        btnOpenShop.setOnClickListener(v -> listener.onOpenShopClicked());
        btnLearnMore.setOnClickListener(v -> listener.onLearnMoreSellerClicked());
    }
}
