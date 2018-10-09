package com.tokopedia.mitra.homepage.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.mitra.R;
import com.tokopedia.mitra.homepage.adapter.HomepageCategoryClickListener;
import com.tokopedia.mitra.homepage.domain.model.CategoryRow;

public class HomepageCategoryViewHolder extends AbstractViewHolder<CategoryRow> {
    @LayoutRes
    public static final int LAYOUT = R.layout.view_mitra_layout_category_item;

    private HomepageCategoryClickListener categoryClickListener;
    private AppCompatTextView titleTextView;
    private AppCompatImageView iconImageView;
    private CardView container;
    private CategoryRow item;

    public HomepageCategoryViewHolder(View itemView, HomepageCategoryClickListener categoryClickListener) {
        super(itemView);
        this.categoryClickListener = categoryClickListener;
        titleTextView = itemView.findViewById(R.id.tv_title);
        iconImageView = itemView.findViewById(R.id.iv_icon);
        container = itemView.findViewById(R.id.container);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomepageCategoryViewHolder.this.categoryClickListener != null) {
                    HomepageCategoryViewHolder.this.categoryClickListener.actionClick(item);
                }
            }
        });
    }

    @Override
    public void bind(CategoryRow element) {
        item = element;
        titleTextView.setText(element.getName());
        ImageHandler.loadImageThumbs(itemView.getContext(), iconImageView, element.getImageUrl());
    }
}
