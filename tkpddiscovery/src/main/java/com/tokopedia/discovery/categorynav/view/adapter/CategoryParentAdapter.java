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
 * @author by alifa on 7/6/17.
 */

public class CategoryParentAdapter extends RecyclerView.Adapter<CategoryParentAdapter.ItemRowHolder> {

    private List<com.tokopedia.discovery.categorynav.domain.model.Category> categories;
    private OnItemClickListener clickListener;
    private String activeId;
    private int activePosition;

    public CategoryParentAdapter(OnItemClickListener itemListener, String activeId) {
        clickListener = itemListener;
        this.activeId = activeId;
        categories = new ArrayList<>();
    }

    public void setDataList(List<com.tokopedia.discovery.categorynav.domain.model.Category> dataList) {
        categories.addAll(dataList);
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_category_parent, parent, false
        );
        return new CategoryParentAdapter.ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        ItemRowHolder itemRowHolder = (ItemRowHolder) holder;
        itemRowHolder.bindData(position);
        View.OnClickListener onParentClicked = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClicked(categories.get(position),position);
            }
        };
        itemRowHolder.categoryContainer.setOnClickListener(onParentClicked);
        itemRowHolder.categoryIcon.setOnClickListener(onParentClicked);
        itemRowHolder.categoryName.setOnClickListener(onParentClicked);
        itemRowHolder.categoryContainer.setOnClickListener(onParentClicked);

    }

    @Override
    public int getItemCount() {
        if (categories != null)
            return categories.size();
        else
            return 0;
    }

    public String getActiveId() {
        return activeId;
    }

    public void setActiveId(String activeId) {
        this.activeId = activeId;
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {
        LinearLayout categoryContainer;
        ImageView categoryIcon;
        TextView categoryName;

        ItemRowHolder(View view) {
            super(view);
            this.categoryIcon = (ImageView) view.findViewById(R.id.category_parent_icon);
            this.categoryName = (TextView) view.findViewById(R.id.category_parent_text);
            this.categoryContainer = (LinearLayout) view.findViewById(R.id.category_parent_container);
        }

        public void bindData(int position) {
            Category category = categories.get(position);
            this.categoryName.setText(category.getName());
            ImageHandler.LoadImage(this.categoryIcon,category.getIconImageUrl());
            if (category.getId().equals(activeId)) {
                this.categoryContainer.setSelected(true);
                activePosition = position;
            } else {
                this.categoryContainer.setSelected(false);
            }
        }

    }

    public int getPositionById(String categoryId) {
        for (int i=0; i< getItemCount(); i++) {
            Category category = categories.get(i);
            if (category.getId().equals(categoryId)) {
                return i;
            }
        }
        return 0;

    }

    public int getActivePosition() {
        return activePosition;
    }

    public interface OnItemClickListener {
        void onItemClicked(com.tokopedia.discovery.categorynav.domain.model.Category category, int position);
    }

}