package com.tokopedia.discovery.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;

import com.tokopedia.core.network.entity.categoriesHades.Child;

import java.util.List;

/**
 * Created by Alifa on 3/6/2017.
 */

public class RevampCategoryAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private int categoryWidth;
    private List<Child> categories;
    private RevampCategoryAdapter.CategoryListener categoryListener;

    public RevampCategoryAdapter(int categoryWidth, List<Child> categories, RevampCategoryAdapter.CategoryListener categoryListener) {
        this.categoryWidth = categoryWidth;
        this.categories = categories;
        this.categoryListener = categoryListener;
    }

    @Override
    public RevampCategoryAdapter.ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.item_revamp_category, null
        );
        v.setMinimumWidth(categoryWidth);
        return new RevampCategoryAdapter.ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final  int position) {
        RevampCategoryAdapter.ItemRowHolder itemRowHolder = (RevampCategoryAdapter.ItemRowHolder) holder;
        itemRowHolder.container.getLayoutParams().width = categoryWidth;
        itemRowHolder.categoryTitle.setText(categories.get(position).getName().toUpperCase());
        ImageHandler.LoadImage(itemRowHolder.thumbnail,categories.get(position).getThumbnailImage());
        itemRowHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryListener.onCategoryIntermediaryClick(categories.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {

        TextView categoryTitle;
        LinearLayout container;
        ImageView thumbnail;

        ItemRowHolder(View view) {
            super(view);
            this.categoryTitle = (TextView) view.findViewById(R.id.categoryTitle);
            this.container = (LinearLayout) view.findViewById(R.id.linWrapper);
            this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }

    public interface CategoryListener {
        void onCategoryIntermediaryClick(Child child);
    }

    public void addDataChild(List<Child> childs) {
        categories.addAll(childs);
        notifyDataSetChanged();
    }
}
