package com.tokopedia.home.account.presentation.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.home.account.AccountHomeUrl;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.viewmodel.SellerEmptyViewModel;

import static com.tokopedia.home.account.analytics.AccountAnalytics.ClickKnowMore;
import static com.tokopedia.home.account.analytics.AccountAnalytics.ClickOpenShopFree;


/**
 * @author by alvinatin on 14/08/18.
 */

public class SellerEmptyViewHolder extends AbstractViewHolder<SellerEmptyViewModel> {
    @LayoutRes
    public static int LAYOUT = R.layout.fragment_seller_account_empty;

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
        ImageHandler.LoadImage(ivEmptyImage, AccountHomeUrl.CDN_URL + AccountHomeUrl.ImageUrl.EMPTY_SELLER_IMG);
        btnOpenShop.setOnClickListener(v -> {
            ClickOpenShopFree();
            listener.onOpenShopClicked();
        });
        btnLearnMore.setOnClickListener(v -> {
            ClickKnowMore();
            listener.onLearnMoreSellerClicked();
        });
    }
}
