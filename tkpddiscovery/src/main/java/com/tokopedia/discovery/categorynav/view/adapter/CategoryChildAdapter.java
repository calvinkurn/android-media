package com.tokopedia.discovery.categorynav.view.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.categorynav.domain.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alifa on 7/10/17.
 */

public class CategoryChildAdapter  extends RecyclerView.Adapter<CategoryChildAdapter.ItemRowHolder> {

    private List<Category> categories;
    private CategoryParentAdapter.OnItemClickListener clickListener;
    private int activePosition = 0;

    public CategoryChildAdapter(CategoryParentAdapter.OnItemClickListener itemListener) {
        clickListener = itemListener;
        categories = new ArrayList<>();
    }

    public void setDataList(List<com.tokopedia.discovery.categorynav.domain.model.Category> dataList) {
        categories = dataList;
    }

    @Override
    public CategoryChildAdapter.ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_category_child, null
        );
        return new CategoryChildAdapter.ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final CategoryChildAdapter.ItemRowHolder holder, int position) {
        activePosition = position;
        CategoryChildAdapter.ItemRowHolder itemRowHolder = (CategoryChildAdapter.ItemRowHolder) holder;
        itemRowHolder.bindData(position);

    }

    @Override
    public int getItemCount() {
        if (categories != null)
            return categories.size();
        else
            return 0;
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {
        LinearLayout categoryContainer;
        ImageView categoryIcon;
        TextView categoryName;

        ItemRowHolder(View view) {
            super(view);
//            this.categoryIcon = (ImageView) view.findViewById(R.id.category_parent_icon);
//            this.categoryName = (TextView) view.findViewById(R.id.category_parent_text);
//            this.categoryContainer = (LinearLayout) view.findViewById(R.id.category_parent_container);
        }

        public void bindData(int position) {
//            Category category = categories.get(position);
//            this.categoryName.setText(category.getName());
//            ImageHandler.LoadImage(this.categoryIcon,category.getIconImageUrl());
//            if (position == activePosition) {
//                this.categoryContainer.setSelected(true);
//            } else {
//                this.categoryContainer.setSelected(false);
//            }
        }

    }

    public interface OnItemClickListener {
        void onItemClicked(com.tokopedia.discovery.categorynav.domain.model.Category category);
    }

}