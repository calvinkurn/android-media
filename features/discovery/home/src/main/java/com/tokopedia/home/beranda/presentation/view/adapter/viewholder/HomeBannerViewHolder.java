package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.domain.gql.feed.Badge;
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedContract;
import com.tokopedia.home.beranda.presentation.view.viewmodel.BannerFeedViewModel;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedViewModel;
import com.tokopedia.productcard.ProductCardView;
import com.tokopedia.topads.sdk.view.ImpressedImageView;

import java.util.ArrayList;
import java.util.List;

public class HomeBannerViewHolder extends AbstractViewHolder<BannerFeedViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.home_feed_banner;

    private Context context;

    private ImpressedImageView bannerImageView;
    private final HomeFeedContract.View homeFeedview;

    public HomeBannerViewHolder(View itemView, HomeFeedContract.View homeFeedview) {
        super(itemView);
        this.homeFeedview = homeFeedview;
        this.context = itemView.getContext();
        bannerImageView = itemView.findViewById(R.id.bannerImageView);
    }

    @Override
    public void bind(BannerFeedViewModel element) {
        ImageHandler.loadImageFitCenter(context, bannerImageView, element.getImageUrl());

        bannerImageView.setViewHintListener(element, new ImpressedImageView.ViewHintListener() {
            @Override
            public void onViewHint() {
//                homeFeedview.onProductImpression(element, getAdapterPosition());
            }
        });
    }
}
