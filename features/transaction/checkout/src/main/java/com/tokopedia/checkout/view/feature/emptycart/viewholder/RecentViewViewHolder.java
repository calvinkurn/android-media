package com.tokopedia.checkout.view.feature.emptycart.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.feature.emptycart.adapter.RecentViewAdapter;
import com.tokopedia.checkout.view.feature.emptycart.viewmodel.RecentViewViewModel;

/**
 * Created by Irfan Khoirul on 21/09/18.
 */

public class RecentViewViewHolder extends RecyclerView.ViewHolder {

    public static final int LAYOUT_RECENT_VIEW = R.layout.item_recent_view;

    private ImageView imgProduct;
    private TextView tvProductName;
    private TextView tvProductPrice;

    private final RecentViewAdapter.ActionListener actionListener;
    private final int itemWidth;

    public RecentViewViewHolder(View itemView, RecentViewAdapter.ActionListener actionListener, int itemWidth) {
        super(itemView);
        this.actionListener = actionListener;
        this.itemWidth = itemWidth;

        imgProduct = itemView.findViewById(R.id.img_product);
        tvProductName = itemView.findViewById(R.id.tv_product_name);
        tvProductPrice = itemView.findViewById(R.id.tv_product_price);
    }

    public void bindData(RecentViewViewModel recentViewViewModel) {
        tvProductName.setText(recentViewViewModel.getRecentView().getProductName());
        tvProductPrice.setText(recentViewViewModel.getRecentView().getProductPrice());
        ImageHandler.loadImage(imgProduct.getContext(), imgProduct,
                recentViewViewModel.getRecentView().getProductImage(), R.drawable.loading_page
        );

        imgProduct.getLayoutParams().width = itemWidth;
        imgProduct.getLayoutParams().height = itemWidth;
        imgProduct.requestLayout();

        itemView.setOnClickListener(v -> actionListener.onItemRecentViewClicked(
                recentViewViewModel.getRecentView(), getAdapterPosition() + 1)
        );

    }

}
