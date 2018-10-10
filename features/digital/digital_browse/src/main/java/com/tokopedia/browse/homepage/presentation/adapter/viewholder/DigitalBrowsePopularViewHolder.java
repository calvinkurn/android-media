package com.tokopedia.browse.homepage.presentation.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.browse.R;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowsePopularBrandsViewModel;

/**
 * @author by furqan on 05/09/18.
 */

public class DigitalBrowsePopularViewHolder extends AbstractViewHolder<DigitalBrowsePopularBrandsViewModel> {

    @LayoutRes
    public static int LAYOUT = R.layout.item_digital_browse_image;

    private AppCompatImageView ivPopularBrand;
    private PopularBrandListener popularBrandListener;

    public DigitalBrowsePopularViewHolder(View itemView, PopularBrandListener popularBrandListener) {
        super(itemView);

        this.popularBrandListener = popularBrandListener;

        ivPopularBrand = itemView.findViewById(R.id.iv_popular_brand);
    }

    @Override
    public void bind(DigitalBrowsePopularBrandsViewModel element) {
        ImageHandler.loadImageWithoutPlaceholder(ivPopularBrand, element.getLogoUrl(), R.drawable.status_no_result);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popularBrandListener.onPopularItemClicked(element, getAdapterPosition());
            }
        });
    }

    public interface PopularBrandListener {
        void onPopularItemClicked(DigitalBrowsePopularBrandsViewModel viewModel, int position);
    }
}
