package com.tokopedia.browse.homepage.presentation.adapter.viewholder;

import android.os.Build;
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

    private static final float DEFAULT_LETTER_SPACING = 0.1f;

    private AppCompatImageView ivProduct;
    private TextViewCompat tvProduct;
    private TextViewCompat tvNewLabel;
    private CategoryListener categoryListener;

    public DigitalBrowseCategoryViewHolder(View itemView, CategoryListener categoryListener) {
        super(itemView);

        this.categoryListener = categoryListener;

        ivProduct = itemView.findViewById(R.id.iv_product);
        tvProduct = itemView.findViewById(R.id.tv_product);
        tvNewLabel = itemView.findViewById(R.id.tv_new_label);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tvNewLabel.setLetterSpacing(DEFAULT_LETTER_SPACING);
        }
    }

    @Override
    public void bind(DigitalBrowseRowViewModel element) {
        ImageHandler.loadImageWithoutPlaceholder(ivProduct, element.getImageUrl(),
                R.drawable.status_no_result);
        tvProduct.setText(element.getName());

        if (element.getCategoryLabel().equals("1")) {
            tvNewLabel.setVisibility(View.VISIBLE);
        } else {
            tvNewLabel.setVisibility(View.GONE);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryListener.onCategoryItemClicked(element, getAdapterPosition());
            }
        });

        categoryListener.sendImpressionAnalytics(element.getName(), getAdapterPosition()+1);
    }

    public interface CategoryListener {
        void onCategoryItemClicked(DigitalBrowseRowViewModel viewModel, int itemPosition);

        void sendImpressionAnalytics(String iconName, int iconPosition);
    }
}
