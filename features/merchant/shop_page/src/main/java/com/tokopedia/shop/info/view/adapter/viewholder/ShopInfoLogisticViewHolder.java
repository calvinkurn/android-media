package com.tokopedia.shop.info.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.shop.R;
import com.tokopedia.shop.info.view.model.ShopInfoLogisticViewModel;
import com.tokopedia.shop.note.view.model.ShopNoteViewModel;

/**
 * @author by alvarisi on 12/12/17.
 */

public class ShopInfoLogisticViewHolder extends AbstractViewHolder<ShopInfoLogisticViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_info_logistic;

    private LabelView shopNoteLabelView;

    public ShopInfoLogisticViewHolder(View itemView) {
        super(itemView);
        findViews(itemView);
    }

    private void findViews(View view) {
        shopNoteLabelView = view.findViewById(R.id.label_view);
    }

    @Override
    public void bind(ShopInfoLogisticViewModel element) {
        ImageHandler.loadImageRounded2(shopNoteLabelView.getImageView().getContext(), shopNoteLabelView.getImageView(), element.getShipmentImage());
        shopNoteLabelView.setTitle(element.getShipmentName());
        shopNoteLabelView.setContent(element.getShipmentPackage());
    }
}