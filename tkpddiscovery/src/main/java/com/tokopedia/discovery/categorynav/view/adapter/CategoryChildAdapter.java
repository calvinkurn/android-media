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
import com.tokopedia.core.discovery.dynamicfilter.adapter.MultiLevelExpIndListAdapter;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.categorynav.domain.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alifa on 7/10/17.
 */

public class CategoryChildAdapter  extends MultiLevelExpIndListAdapter {

    private CategoryParentAdapter.OnItemClickListener clickListener;
    private int activePosition = 0;

    public CategoryChildAdapter(CategoryParentAdapter.OnItemClickListener itemListener) {
        clickListener = itemListener;
    }

    @Override
    public CategoryChildAdapter.ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_category_child, null
        );
        return new CategoryChildAdapter.ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        activePosition = position;
        Category category = (Category) getItemAt(position);
        CategoryChildAdapter.ItemRowHolder itemRowHolder = (CategoryChildAdapter.ItemRowHolder) holder;
        itemRowHolder.bindData(category);
    }


    class ItemRowHolder extends RecyclerView.ViewHolder {
        LinearLayout categoryContainer;
        TextView categoryName;

        ItemRowHolder(View view) {
            super(view);
            this.categoryName = (TextView) view.findViewById(R.id.category_child_text);
            this.categoryContainer = (LinearLayout) view.findViewById(R.id.category_child_container);
        }

        public void bindData(Category category) {
            this.categoryName.setText(category.getName());
        }

    }

    public interface OnItemClickListener {
        void onItemClicked(com.tokopedia.discovery.categorynav.domain.model.Category category);
    }

}