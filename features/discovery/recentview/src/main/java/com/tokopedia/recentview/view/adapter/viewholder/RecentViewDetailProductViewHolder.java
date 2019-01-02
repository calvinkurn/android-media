package com.tokopedia.recentview.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.recentview.R;
import com.tokopedia.recentview.view.adapter.LabelsAdapter;
import com.tokopedia.recentview.view.listener.RecentView;
import com.tokopedia.recentview.view.viewmodel.RecentViewDetailProductViewModel;

/**
 * @author by nisie on 7/4/17.
 */

public class RecentViewDetailProductViewHolder extends AbstractViewHolder<RecentViewDetailProductViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_recent_view_product_detail;

    private TextView productName;
    private TextView productPrice;
    private ImageView productImage;
    private ImageView wishlist;
    private RatingBar productRating;
    private RecyclerView labels;
    public TextView shopName;
    private TextView shopLocation;
    private ImageView iconLocation;

    private ImageView freeReturn;
    private ImageView goldMerchant;
    private ImageView officialStore;

    private View mainView;

    private final RecentView.View viewListener;
    private LabelsAdapter labelsAdapter;

    public RecentViewDetailProductViewHolder(View itemView, RecentView.View viewListener) {
        super(itemView);
        productName = itemView.findViewById(R.id.product_name);
        productPrice = itemView.findViewById(R.id.product_price);
        productImage = itemView.findViewById(R.id.product_image);
        wishlist = itemView.findViewById(R.id.wishlist);
        productRating = itemView.findViewById(R.id.product_rating);
        labels = itemView.findViewById(R.id.labels);
        freeReturn = itemView.findViewById(R.id.free_return);
        mainView = itemView.findViewById(R.id.main_view);
        goldMerchant = itemView.findViewById(R.id.gold_merchant);
        officialStore = itemView.findViewById(R.id.official_store);
        shopName = itemView.findViewById(R.id.shop_name);
        shopLocation = itemView.findViewById(R.id.shop_location);
        iconLocation = itemView.findViewById(R.id.ic_location);
        this.viewListener = viewListener;

        LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext
                (), LinearLayoutManager.HORIZONTAL, false);
        labels.setLayoutManager(layoutManager);
        labelsAdapter = new LabelsAdapter();
        labels.setAdapter(labelsAdapter);

    }

    @Override
    public void bind(final RecentViewDetailProductViewModel element) {

        ImageHandler.LoadImage(productImage, element.getImageSource());

//        if (element.isWishlist()) {
//            ImageHandler.loadImageWithId(wishlist, R.drawable.wishlist_faved);
//        } else {
//            ImageHandler.loadImageWithId(wishlist, R.drawable.wishlist);
//        }

        productName.setText(MethodChecker.fromHtml(element.getName()));
        productPrice.setText(element.getPrice());

        if (element.getRating() > 0) {
            productRating.setRating(element.getRating());
            productRating.setVisibility(View.VISIBLE);
        } else {
            productRating.setVisibility(View.GONE);
        }

        if (!element.getLabels().isEmpty()) {
            labels.setVisibility(View.VISIBLE);
            labelsAdapter.setList(element.getLabels());
        } else {
            labels.setVisibility(View.GONE);
        }

        if (element.isFreeReturn())
            freeReturn.setVisibility(View.VISIBLE);
        else
            freeReturn.setVisibility(View.GONE);
//
//        wishlist.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewListener.onWishlistClicked(
//                        getAdapterPosition(),
//                        element.getContentId(),
//                        element.isWishlist());
//            }
//        });

        if (element.isOfficial()) {
            officialStore.setVisibility(View.VISIBLE);
            goldMerchant.setVisibility(View.GONE);
        } else if (element.isGold()) {
            officialStore.setVisibility(View.GONE);
            goldMerchant.setVisibility(View.VISIBLE);
        } else {
            officialStore.setVisibility(View.GONE);
            goldMerchant.setVisibility(View.GONE);
        }

        shopName.setText(MethodChecker.fromHtml(element.getShopName()));

        if (element.isOfficial()) {
            iconLocation.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_badge_authorize));
            shopLocation.setText(itemView.getContext().getString(R.string.title_badge_authorized));
        } else {
            shopLocation.setText(element.getShopLocation());
            iconLocation.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_icon_location_grey));
        }

        mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToProductDetail(
                        String.valueOf(element.getProductId()),
                        element.getName(),
                        element.getPrice(),
                        element.getImageSource()
                );
                viewListener.sendRecentViewClickTracking(element);
            }
        });
    }

}
