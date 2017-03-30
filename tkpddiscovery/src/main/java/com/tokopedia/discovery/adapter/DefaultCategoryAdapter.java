package com.tokopedia.discovery.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;

import butterknife.ButterKnife;

import com.tokopedia.core.network.entity.categoriesHades.Child;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alifa on 2/28/2017.
 */

public class DefaultCategoryAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private int categoryWidth;
    private List<Child> categories;
    private DefaultCategoryAdapter.CategoryListener categoryListener;

    public DefaultCategoryAdapter(int categoryWidth, List<Child> categories, CategoryListener categoryListener) {
        this.categoryWidth = categoryWidth;
        this.categories = categories;
        this.categoryListener = categoryListener;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.item_default_category, null
        );
        v.setMinimumWidth(categoryWidth);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final  int position) {
        ItemRowHolder itemRowHolder = (ItemRowHolder) holder;
        itemRowHolder.container.getLayoutParams().width = categoryWidth;
        itemRowHolder.categoryTitle.setText(categories.get(position).getName());
        itemRowHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryListener.onCategoryClick(categories.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.categoryTitle)
        TextView categoryTitle;

        @BindView(R2.id.linWrapper)
        LinearLayout container;

        ItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    public interface CategoryListener {
        void onCategoryClick(Child child);
    }

    public void addDataChild(List<Child> childs) {
        categories.addAll(childs);
        notifyDataSetChanged();
    }

    public void hideExpandable() {
        categories.subList(6,categories.size()).clear();
        notifyDataSetChanged();
    }
}
