package com.tokopedia.browse.homepage.presentation.adapter.viewholder;

import android.os.Build;
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

    private static final float DEFAULT_LETTER_SPACING = 0.1f;

    private AppCompatImageView ivProduct;
    private TextViewCompat tvProduct;
    private TextViewCompat tvNewLabel;
    private CategoryListener categoryListener;
    private LinearLayout containerItem;
    private DigitalBrowseServiceCategoryViewModel item;

    public DigitalBrowseServiceViewHolder(View itemView, CategoryListener categoryListener) {
        super(itemView);

        this.categoryListener = categoryListener;

        containerItem = itemView.findViewById(R.id.container_item);
        ivProduct = itemView.findViewById(R.id.iv_product);
        tvProduct = itemView.findViewById(R.id.tv_product);
        tvNewLabel = itemView.findViewById(R.id.tv_new_label);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tvNewLabel.setLetterSpacing(DEFAULT_LETTER_SPACING);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DigitalBrowseServiceViewHolder.this.item.isTitle()) {
                    DigitalBrowseServiceViewHolder.this.categoryListener.onCategoryItemClicked(item, getAdapterPosition());
                }
            }
        });
    }

    @Override
    public void bind(DigitalBrowseServiceCategoryViewModel element) {
        this.item = element;
        if (element.isTitle()) {
            ivProduct.setVisibility(View.GONE);
            tvProduct.setTextAppearance(ivProduct.getContext(), R.style.TextView_Title_Bold);
            containerItem.setGravity(Gravity.LEFT);
            tvNewLabel.setVisibility(View.GONE);
            itemView.setClickable(false);
            itemView.setPadding(0, itemView.getResources().getDimensionPixelSize(R.dimen.dp_16), 0, 0);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            setItemView();

            itemView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            categoryListener.sendImpressionAnalytics(element, getAdapterPosition());
        }

        tvProduct.setText(element.getName());
    }

    public void bindLastItem(DigitalBrowseServiceCategoryViewModel element) {
        this.item = element;

        setItemView();

        itemView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        tvProduct.setText(element.getName());

        categoryListener.sendImpressionAnalytics(element, getAdapterPosition());
    }

    private void setItemView() {
        ivProduct.setVisibility(View.VISIBLE);
        ImageHandler.loadImageWithoutPlaceholder(ivProduct, DigitalBrowseServiceViewHolder.this.item.getImageUrl(),
                R.drawable.status_no_result);
        tvProduct.setTextAppearance(ivProduct.getContext(), R.style.TextView_Micro);
        containerItem.setGravity(Gravity.CENTER_HORIZONTAL);
        itemView.setClickable(true);
        itemView.setPadding(0, itemView.getResources().getDimensionPixelSize(R.dimen.dp_8), 0, itemView.getResources().getDimensionPixelSize(R.dimen.dp_8));

        if (DigitalBrowseServiceViewHolder.this.item.getCategoryLabel().equals("1")) {
            tvNewLabel.setVisibility(View.VISIBLE);
        } else {
            tvNewLabel.setVisibility(View.GONE);
        }
    }

    public interface CategoryListener {
        void onCategoryItemClicked(DigitalBrowseServiceCategoryViewModel viewModel, int itemPosition);

        void sendImpressionAnalytics(DigitalBrowseServiceCategoryViewModel viewModel, int itemPosition);
    }
}
