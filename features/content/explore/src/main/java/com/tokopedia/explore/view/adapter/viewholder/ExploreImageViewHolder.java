package com.tokopedia.explore.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.explore.R;
import com.tokopedia.explore.view.listener.ContentExploreContract;
import com.tokopedia.explore.view.viewmodel.ExploreImageViewModel;

/**
 * @author by milhamj on 24/07/18.
 */

public class ExploreImageViewHolder extends AbstractViewHolder<ExploreImageViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_explore_image;

    private final ContentExploreContract.View listener;
    private ImageView image;

    public ExploreImageViewHolder(View itemView, ContentExploreContract.View listener) {
        super(itemView);
        this.listener = listener;
        image = itemView.findViewById(R.id.image);
    }

    @Override
    public void bind(ExploreImageViewModel element) {
        ImageHandler.LoadImage(image, element.getImageUrl());
        image.setOnClickListener(v -> {
            listener.goToKolPostDetail(element.getKolPostViewModel());
        });
    }
}
