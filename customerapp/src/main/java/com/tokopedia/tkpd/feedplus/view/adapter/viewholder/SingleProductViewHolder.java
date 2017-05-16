package com.tokopedia.tkpd.feedplus.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.feedplus.FeedPlus;
import com.tokopedia.tkpd.feedplus.view.viewmodel.ProductCardViewModel;

import butterknife.BindView;

/**
 * @author by nisie on 5/16/17.
 */

public class SingleProductViewHolder extends ProductCardViewHolder<ProductCardViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.list_feed_product_single;

    @BindView(R.id.product_image)
    ImageView productImage;

    @BindView(R.id.product_name)
    TextView productName;

    @BindView(R.id.product_price)
    TextView productPrice;

    public SingleProductViewHolder(View itemView,
                                   FeedPlus.View viewListener) {
        super(itemView, viewListener);
    }

    @Override
    public void bind(ProductCardViewModel productCardViewModel) {
        super.bind(productCardViewModel);
        ImageHandler.LoadImage(productImage,
                productCardViewModel.getListProduct().get(0).getImageSource());

        productName.setText(MethodChecker.fromHtml(
                productCardViewModel.getListProduct().get(0).getName()));
        productPrice.setText(productCardViewModel.getListProduct().get(0).getPrice());
    }


}
