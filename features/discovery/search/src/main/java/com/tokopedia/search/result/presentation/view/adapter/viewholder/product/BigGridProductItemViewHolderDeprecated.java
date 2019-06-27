package com.tokopedia.search.result.presentation.view.adapter.viewholder.product;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.search.R;
import com.tokopedia.search.result.presentation.model.ProductItemViewModel;
import com.tokopedia.search.result.presentation.view.listener.ProductListener;

@Deprecated
public class BigGridProductItemViewHolderDeprecated extends GridProductItemViewHolder {
    @LayoutRes
    public static final int LAYOUT = R.layout.search_srp_item_big_grid;

    public BigGridProductItemViewHolderDeprecated(View itemView, ProductListener itemClickListener) {
        super(itemView, itemClickListener);
    }

    @Override
    public void setImageProduct(ProductItemViewModel productItem) {
        ImageHandler.loadImageThumbs(context, productImage, productItem.getImageUrl700());
    }
}
