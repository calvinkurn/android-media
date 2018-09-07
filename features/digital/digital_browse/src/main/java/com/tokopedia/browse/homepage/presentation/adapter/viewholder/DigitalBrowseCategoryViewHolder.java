package com.tokopedia.browse.homepage.presentation.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.browse.R;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseRowViewModel;
import com.tokopedia.design.component.TextViewCompat;

/**
 * @author by furqan on 05/09/18.
 */

public class DigitalBrowseCategoryViewHolder extends AbstractViewHolder<DigitalBrowseRowViewModel> {

    @LayoutRes
    public static int LAYOUT = R.layout.item_digital_browse_image_with_title;

    private AppCompatImageView ivProduct;
    private TextViewCompat tvProduct;
    private CategoryListener categoryListener;

    public DigitalBrowseCategoryViewHolder(View itemView, CategoryListener categoryListener) {
        super(itemView);

        this.categoryListener = categoryListener;

        ivProduct = itemView.findViewById(R.id.iv_product);
        tvProduct = itemView.findViewById(R.id.tv_product);
    }

    @Override
    public void bind(DigitalBrowseRowViewModel element) {
        ImageHandler.loadImageWithoutPlaceholder(ivProduct, element.getImageUrl(),
                R.drawable.status_no_result);
        tvProduct.setText(element.getName());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryListener.onCategoryItemClicked(element);
            }
        });
    }

    public interface CategoryListener {
        void onCategoryItemClicked(DigitalBrowseRowViewModel viewModel);
    }
}
