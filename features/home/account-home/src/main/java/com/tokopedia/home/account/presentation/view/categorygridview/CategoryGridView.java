package com.tokopedia.home.account.presentation.view.categorygridview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.view.categorygridview.model.CategoryGrid;
import com.tokopedia.home.account.presentation.view.categorygridview.model.CategoryItem;

/**
 * @author okasurya on 7/19/18.
 */
public class CategoryGridView extends BaseCustomView {
    private static final int SPAN_COUNT = 4;

    private TextView textTitle;
    private TextView textLink;
    private RecyclerView recyclerCategory;
    private CategoryGridAdapter adapter;

    public CategoryGridView(@NonNull Context context) {
        super(context);
        init();
    }

    public CategoryGridView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CategoryGridView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.view_category_grid, this);
        textTitle = view.findViewById(R.id.text_title);
        textLink = view.findViewById(R.id.text_link);
        recyclerCategory = view.findViewById(R.id.recycler_category);
        recyclerCategory.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT, GridLayoutManager.VERTICAL, false));
    }

    private void setData(CategoryGrid categoryGrid, @NonNull OnClickListener listener) {
        if(categoryGrid.getItems() != null && categoryGrid.getItems().size() > 0) {
            if(!TextUtils.isEmpty(categoryGrid.getTitle())) {
                textTitle.setText(categoryGrid.getTitle());
            }

            if(!TextUtils.isEmpty(categoryGrid.getLinkText())) {
                textLink.setText(categoryGrid.getLinkText());
                textLink.setOnClickListener(v -> listener.onLinkClicked(categoryGrid.getApplinkUrl()));
            }

            adapter = new CategoryGridAdapter(listener);
            recyclerCategory.setAdapter(adapter);
            adapter.setNewData(categoryGrid.getItems());
        }
    }

    interface OnClickListener {
        void onCategoryItemClicked(CategoryItem categoryItem);

        void onLinkClicked(String url);
    }

}
