package com.tokopedia.explore.view.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.LayoutRes;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.explore.R;
import com.tokopedia.explore.view.adapter.ExploreCategoryAdapter;
import com.tokopedia.explore.view.listener.ContentExploreContract;
import com.tokopedia.explore.view.uimodel.ExploreImageViewModel;
import com.tokopedia.kotlin.extensions.view.ViewExtKt;

/**
 * @author by milhamj on 24/07/18.
 */

public class ExploreImageViewHolder extends AbstractViewHolder<ExploreImageViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_explore_image;

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
        ImageHandler.LoadImage(image, element.getImageUrl());

        int badgeId = getBadgeId(element);
        if (badgeId != 0) {
            badge.setVisibility(View.VISIBLE);
            ImageHandler.loadImageWithId(badge, badgeId);
        } else {
            badge.setVisibility(View.GONE);
        }

        ViewExtKt.addOnImpressionListener(itemView, element.getImpressHolder(), () -> listener.onAffiliateTrack(element.getTrackingViewModelList(), false));

        image.setOnClickListener(v -> {
            listener.goToKolPostDetail(element.getPostId(), element.getUserName(), element.getTrackingViewModelList().get(0).getRecomId());
            listener.onAffiliateTrack(element.getTrackingViewModelList(), true);
        });

        if (element.getItemPos() == 0 && listener.getExploreCategory() == ExploreCategoryAdapter.CAT_ID_AFFILIATE) {
            listener.addExploreItemCoachmark(itemView);
        }
    }

    private int getBadgeId(ExploreImageViewModel element) {
        switch (element.getCardType()) {
            case Youtube:
            case Video: return R.drawable.ic_affiliate_video;
            case Multi: return R.drawable.ic_affiliate_multi;
            default : return 0;
        }
    }
}
