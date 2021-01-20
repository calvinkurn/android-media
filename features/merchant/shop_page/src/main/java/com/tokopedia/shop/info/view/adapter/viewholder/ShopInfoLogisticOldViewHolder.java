package com.tokopedia.shop.info.view.adapter.viewholder;

import androidx.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.shop.R;
import com.tokopedia.shop.info.view.model.ShopInfoLogisticUiModel;

/**
 * @author by alvarisi on 12/12/17.
 */
@Deprecated
public class ShopInfoLogisticOldViewHolder extends AbstractViewHolder<ShopInfoLogisticUiModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_info_logistic;

    private LabelView shopNoteLabelView;

    public ShopInfoLogisticOldViewHolder(View itemView) {
        super(itemView);
        findViews(itemView);
    }

    private void findViews(View view) {
        shopNoteLabelView = view.findViewById(R.id.label_view);
    }

    @Override
    public void bind(ShopInfoLogisticUiModel element) {
        ImageHandler.loadImageRounded2(shopNoteLabelView.getImageView().getContext(), shopNoteLabelView.getImageView(), element.getShipmentImage());
        shopNoteLabelView.setTitle(element.getShipmentName());
        shopNoteLabelView.setContent(element.getShipmentPackage());
    }
}