package com.tokopedia.topads.sdk.view.adapter.viewholder.discovery;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.domain.model.Badge;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.ImpressionListener;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.utils.ImageLoader;
import com.tokopedia.topads.sdk.utils.ImpresionTask;
import com.tokopedia.topads.sdk.view.ImpressedImageView;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ProductCarouselListViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ProductGridViewModel;

import java.util.List;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class ProductCarouselListViewHolder extends AbstractViewHolder<ProductCarouselListViewModel> implements
        View.OnClickListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ads_carousel_item;
    private static final String TAG = ProductCarouselListViewHolder.class.getSimpleName();

    private LocalAdsClickListener itemClickListener;
    private TopAdsItemImpressionListener impressionListener;
    private Data data;
    private Context context;
    public LinearLayout badgeContainer;
    public TextView productName;
    public TextView productPrice;
    public TextView shopLocation;
    public ImpressedImageView productImage;
    private ImageLoader imageLoader;
    private int clickPosition;


    public ProductCarouselListViewHolder(View itemView, ImageLoader imageLoader,
                                         LocalAdsClickListener itemClickListener, int clickPosition,
                                         TopAdsItemImpressionListener impressionListener) {
        super(itemView);
        itemView.findViewById(R.id.container).setOnClickListener(this);
        this.itemClickListener = itemClickListener;
        this.imageLoader = imageLoader;
        this.clickPosition = clickPosition;
        this.impressionListener = impressionListener;
        context = itemView.getContext();
        badgeContainer = (LinearLayout) itemView.findViewById(R.id.badges_container);
        productImage = (ImpressedImageView) itemView.findViewById(R.id.product_image);
        productName = (TextView) itemView.findViewById(R.id.title);
        productPrice = (TextView) itemView.findViewById(R.id.price);
    }

    @Override
    public void bind(ProductCarouselListViewModel element) {
        data = element.getData();
        if (data.getProduct() != null) {
            bindProduct(data.getProduct());
        }
    }

    private void bindProduct(final Product product) {
        productImage.setImage(product.getImage());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            productName.setText(Html.fromHtml(product.getName(),
                    Html.FROM_HTML_MODE_LEGACY));
        } else {
            productName.setText(Html.fromHtml(product.getName()));
        }
        productPrice.setText(product.getPriceFormat());
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null) {
            if(v.getId() == R.id.container) {
                itemClickListener.onProductItemClicked((clickPosition < 0 ? getAdapterPosition() : clickPosition), data);
            }
            if(v.getId() == R.id.wishlist_button_container){
                itemClickListener.onAddWishLish((clickPosition < 0 ? getAdapterPosition() : clickPosition), data);
                data.getProduct().setWishlist(!data.getProduct().isWishlist());
            }
        }
    }

}
