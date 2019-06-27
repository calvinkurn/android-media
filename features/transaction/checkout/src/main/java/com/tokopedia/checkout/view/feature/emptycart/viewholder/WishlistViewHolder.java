package com.tokopedia.checkout.view.feature.emptycart.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.feature.emptycart.adapter.WishlistAdapter;
import com.tokopedia.checkout.view.feature.emptycart.viewmodel.WishlistViewModel;

/**
 * Created by Irfan Khoirul on 21/09/18.
 */

public class WishlistViewHolder extends RecyclerView.ViewHolder {

    public static final int LAYOUT_WISHLIST = R.layout.item_wishlist;

    private ImageView imgProduct;
    private TextView tvProductName;
    private TextView tvProductPrice;

    private final WishlistAdapter.ActionListener actionListener;
    private final int itemWidth;

    public WishlistViewHolder(View itemView, WishlistAdapter.ActionListener actionListener, int itemWidth) {
        super(itemView);
        this.actionListener = actionListener;
        this.itemWidth = itemWidth;

        imgProduct = itemView.findViewById(R.id.img_product);
        tvProductName = itemView.findViewById(R.id.tv_product_name);
        tvProductPrice = itemView.findViewById(R.id.tv_product_price);

    }

    public void bindData(WishlistViewModel wishlistViewModel) {
        tvProductName.setText(wishlistViewModel.getWishlist().getName());
        tvProductPrice.setText(wishlistViewModel.getWishlist().getPriceFmt());
        ImageHandler.loadImage(imgProduct.getContext(), imgProduct,
                wishlistViewModel.getWishlist().getImageUrl(), R.drawable.loading_page
        );

        imgProduct.getLayoutParams().width = itemWidth;
        imgProduct.getLayoutParams().height = itemWidth;
        imgProduct.requestLayout();

        itemView.setOnClickListener(v -> actionListener.onItemWishListClicked(
                wishlistViewModel.getWishlist(), getAdapterPosition() + 1)
        );
    }
}
