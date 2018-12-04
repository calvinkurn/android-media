package com.tokopedia.tkpd.tkpdreputation.review.shop.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductContentViewHolder;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductModelContent;

/**
 * Created by zulfikarrahman on 1/19/18.
 */

public class ReviewShopViewHolder extends ReviewProductContentViewHolder {
    public static final int LAYOUT = R.layout.item_shop_review;
    public static final int RADIUS_IMAGE = 4;

    ShopReviewHolderListener shopReviewHolderListener;

    private TextView productName;
    private ImageView productImage;

    public ReviewShopViewHolder(View itemView, ListenerReviewHolder viewListener, ShopReviewHolderListener shopReviewHolderListener) {
        super(itemView, viewListener);
        productName = itemView.findViewById(R.id.product_name);
        productImage = itemView.findViewById(R.id.product_image);
        this.shopReviewHolderListener = shopReviewHolderListener;
    }

    @Override
    public void bind(ReviewProductModelContent element) {
        super.bind(element);
        if(element instanceof ReviewShopModelContent){
            final ReviewShopModelContent shopReviewModelContent = (ReviewShopModelContent) element;
            productName.setText(shopReviewModelContent.getProductName());

            ImageHandler.loadImageRounded2(itemView.getContext(), productImage, shopReviewModelContent.getProductImageUrl(), convertDpToPx(productImage.getContext(), RADIUS_IMAGE));
            productName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToProductDetail(shopReviewModelContent);
                }
            });
            productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToProductDetail(shopReviewModelContent);
                }
            });
        }
    }

    public float convertDpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    private void goToProductDetail(ReviewShopModelContent shopReviewModelContent) {
        shopReviewHolderListener.onGoToDetailProduct(shopReviewModelContent.getProductId(), getAdapterPosition());
    }

    public interface ShopReviewHolderListener{
        void onGoToDetailProduct(String productUrl, int adapterPosition);
    }
}
