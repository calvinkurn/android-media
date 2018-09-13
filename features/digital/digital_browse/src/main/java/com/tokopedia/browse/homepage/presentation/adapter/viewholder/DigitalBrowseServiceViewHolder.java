package com.tokopedia.browse.homepage.presentation.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatImageView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.browse.R;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseServiceCategoryViewModel;
import com.tokopedia.design.component.TextViewCompat;

/**
 * @author by furqan on 07/09/18.
 */

public class DigitalBrowseServiceViewHolder extends AbstractViewHolder<DigitalBrowseServiceCategoryViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_digital_browse_image_with_title;

    private AppCompatImageView ivProduct;
    private TextViewCompat tvProduct;
    private CategoryListener categoryListener;
    private DigitalBrowseServiceCategoryViewModel item;

    public DigitalBrowseServiceViewHolder(View itemView, CategoryListener categoryListener) {
        super(itemView);

        this.categoryListener = categoryListener;

        ivProduct = itemView.findViewById(R.id.iv_product);
        tvProduct = itemView.findViewById(R.id.tv_product);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DigitalBrowseServiceViewHolder.this.item.isTitle()) {
                    DigitalBrowseServiceViewHolder.this.categoryListener.onCategoryItemClicked(item);
                }
            }
        });
    }

    @Override
    public void bind(DigitalBrowseServiceCategoryViewModel element) {
        this.item  = element;
        if (element.isTitle()) {
            ivProduct.setVisibility(View.GONE);
            tvProduct.setTextAppearance(ivProduct.getContext(), R.style.TextView_Title_Bold);
            tvProduct.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            ((LinearLayout) itemView).setGravity(Gravity.LEFT);
            itemView.setClickable(false);
        } else {
            setItemView();

            itemView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        tvProduct.setText(element.getName());
    }

    public void bindLastItem(DigitalBrowseServiceCategoryViewModel element) {
        this.item  = element;

        setItemView();

        itemView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        tvProduct.setText(element.getName());
    }

    private void setItemView() {
        ivProduct.setVisibility(View.VISIBLE);
        ImageHandler.loadImageWithoutPlaceholder(ivProduct, DigitalBrowseServiceViewHolder.this.item.getImageUrl(),
                R.drawable.status_no_result);
        tvProduct.setTextAppearance(ivProduct.getContext(), R.style.TextView_Micro);
        ((LinearLayout) itemView).setGravity(Gravity.CENTER_HORIZONTAL);
        itemView.setClickable(true);
    }

    public interface CategoryListener {
        void onCategoryItemClicked(DigitalBrowseServiceCategoryViewModel viewModel);
    }
}
