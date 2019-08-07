package com.tokopedia.tkpd.home.wishlist.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.adapter.OnWishlistActionButtonClicked;
import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.WishlistEmptyViewModel;

/**
 * Author errysuprayogi on 25,July,2019
 */
public class WishlistEmptyViewHolder extends AbstractViewHolder<WishlistEmptyViewModel> {

    @LayoutRes
    public static int LAYOUT = R.layout.layout_wishlist_empty_state;
    private OnWishlistActionButtonClicked actionButtonClicked;
    private Button button;

    public WishlistEmptyViewHolder(View itemView, OnWishlistActionButtonClicked actionButtonClicked) {
        super(itemView);
        this.actionButtonClicked = actionButtonClicked;
        button = itemView.findViewById(R.id.action_btn);
    }

    @Override
    public void bind(WishlistEmptyViewModel element) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionButtonClicked.findProduct();
            }
        });
    }
}
