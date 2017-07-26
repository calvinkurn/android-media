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

    private CategoryChildAdapter.OnItemClickListener clickListener;
    private int activePosition=0;

    public CategoryChildAdapter(CategoryChildAdapter.OnItemClickListener itemListener) {
        super();
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Category category = (Category) getItemAt(position);
        CategoryChildAdapter.ItemRowHolder itemRowHolder = (CategoryChildAdapter.ItemRowHolder) holder;
        itemRowHolder.bindData(category);
        itemRowHolder.categoryContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Category categoryClicked = (Category)getItemAt(position);
                if (categoryClicked.getChildren()!=null && categoryClicked.getChildren().size()>0) {
                    toggleSelectedChild();
                    notifyDataSetChanged();
                } else {
                    activePosition = position;
                    clickListener.onChildClicked(categoryClicked);
                }
            }
        });
    }



    class ItemRowHolder extends RecyclerView.ViewHolder {
        LinearLayout categoryContainer;
        TextView categoryName;
        ImageView dropdown;

        ItemRowHolder(View view) {
            super(view);
            this.categoryName = (TextView) view.findViewById(R.id.category_child_text);
            this.categoryContainer = (LinearLayout) view.findViewById(R.id.category_child_container);
            this.dropdown = (ImageView) view.findViewById(R.id.category_child_dropdown);
        }

        public void bindData(Category category) {
            this.categoryName.setText(category.getName());
            if (!category.getHasChild()) dropdown.setVisibility(View.GONE);
            int pad = getPaddingPixels(10);
            if (category.getIndentation()>2) {
                categoryContainer.setPadding(getPaddingPixels(25), pad, pad, pad);
            } else {
                categoryContainer.setPadding(pad, pad, pad, pad);
            }
        }

        public int getPaddingPixels(int dpValue) {
            final float scale = categoryContainer.getContext().getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }

    }

    public void toggleSelectedChild() {
        toggleGroup(activePosition);
    }

    public interface OnItemClickListener {
        void onChildClicked(com.tokopedia.discovery.categorynav.domain.model.Category category);
    }

}