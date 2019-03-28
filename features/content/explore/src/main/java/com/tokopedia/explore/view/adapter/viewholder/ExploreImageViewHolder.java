package com.tokopedia.explore.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.explore.R;
import com.tokopedia.explore.view.listener.ContentExploreContract;
import com.tokopedia.explore.view.viewmodel.ExploreImageViewModel;

import static com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel.TYPE_MULTI;
import static com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel.TYPE_VIDEO;
import static com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel.TYPE_YOUTUBE;

/**
 * @author by milhamj on 24/07/18.
 */

public class ExploreImageViewHolder extends AbstractViewHolder<ExploreImageViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_explore_image;

    private final ContentExploreContract.View listener;
    private ImageView image, badge;

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

        image.setOnClickListener(v -> {
            listener.goToKolPostDetail(element.getKolPostViewModel());
        });
    }

    private int getBadgeId(ExploreImageViewModel element) {
        switch (element.getKolPostViewModel().getCardType()) {
            case TYPE_YOUTUBE:
            case TYPE_VIDEO: return R.drawable.ic_affiliate_video;
            case TYPE_MULTI: return R.drawable.ic_affiliate_multi;
            default : return 0;
        }
    }
}
