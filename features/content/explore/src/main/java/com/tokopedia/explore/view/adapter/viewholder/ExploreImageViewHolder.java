package com.tokopedia.explore.view.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.LayoutRes;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.explore.R;
import com.tokopedia.explore.view.adapter.ExploreCategoryAdapter;
import com.tokopedia.explore.view.listener.ContentExploreContract;
import com.tokopedia.explore.view.uimodel.ExploreImageViewModel;
import com.tokopedia.kotlin.extensions.view.ViewExtKt;
import com.tokopedia.media.loader.JvmMediaLoader;

/**
 * @author by milhamj on 24/07/18.
 */

public class ExploreImageViewHolder extends AbstractViewHolder<ExploreImageViewModel> {

    @LayoutRes
    public static final int LAYOUT = com.tokopedia.explore.R.layout.item_explore_image;

    private final ContentExploreContract.View listener;
    private final ImageView image;
    private final ImageView badge;

    public ExploreImageViewHolder(View itemView, ContentExploreContract.View listener) {
        super(itemView);
        this.listener = listener;
        image = itemView.findViewById(R.id.image);
        badge = itemView.findViewById(R.id.badge);
    }

    @Override
    public void bind(ExploreImageViewModel element) {
        JvmMediaLoader.loadImage(image, element.getImageUrl());

        int badgeId = getBadgeId(element);
        if (badgeId != 0) {
            badge.setVisibility(View.VISIBLE);
            JvmMediaLoader.loadImage(badge, badgeId);
        } else {
            badge.setVisibility(View.GONE);
        }

        ViewExtKt.addOnImpressionListener(itemView, element.getImpressHolder(), () -> listener.onAffiliateTrack(element.getTrackingModelList(), false));

        image.setOnClickListener(v -> {
            listener.goToKolPostDetail(element.getPostId(), element.getUserName(), element.getTrackingModelList().get(0).getRecomId());
            listener.onAffiliateTrack(element.getTrackingModelList(), true);
        });

        if (element.getItemPos() == 0 && listener.getExploreCategory() == ExploreCategoryAdapter.CAT_ID_AFFILIATE) {
            listener.addExploreItemCoachmark(itemView);
        }
    }

    private int getBadgeId(ExploreImageViewModel element) {
        switch (element.getCardType()) {
            case Youtube:
            case Video: return com.tokopedia.feedcomponent.R.drawable.ic_affiliate_video;
            case Multi: return com.tokopedia.feedcomponent.R.drawable.ic_affiliate_multi;
            default : return 0;
        }
    }
}
