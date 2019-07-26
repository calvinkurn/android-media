package com.tokopedia.tkpd.home.wishlist.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.component.TextViewCompat;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.WishlistRecomTitleViewModel;

/**
 * Author errysuprayogi on 25,July,2019
 */
public class WishlistRecomTitleViewHolder extends AbstractViewHolder<WishlistRecomTitleViewModel> {

    @LayoutRes
    public static int LAYOUT = R.layout.wishlist_recom_title_item;
    private TextViewCompat textView;

    public WishlistRecomTitleViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.title);
    }

    @Override
    public void bind(WishlistRecomTitleViewModel element) {
        textView.setText(element.getTitle());
    }
}
