package com.tokopedia.shop.favourite.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.shop.R;
import com.tokopedia.shop.address.view.model.ShopAddressViewModel;
import com.tokopedia.shop.favourite.view.model.ShopFavouriteViewModel;

/**
 * @author by alvarisi on 12/12/17.
 */

public class ShopFavouriteViewHolder extends AbstractViewHolder<ShopFavouriteViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_favourite_user;


    private LabelView userLabelView;

    public ShopFavouriteViewHolder(View itemView) {
        super(itemView);
        findViews(itemView);
    }

    private void findViews(View view) {
        userLabelView = view.findViewById(R.id.label_user);
    }

    @Override
    public void bind(ShopFavouriteViewModel shopFavouriteViewModel) {
        userLabelView.setTitle(shopFavouriteViewModel.getName());
        ImageHandler.loadImageCircle2(userLabelView.getImageView().getContext(), userLabelView.getImageView(), shopFavouriteViewModel.getImageUrl());
    }
}