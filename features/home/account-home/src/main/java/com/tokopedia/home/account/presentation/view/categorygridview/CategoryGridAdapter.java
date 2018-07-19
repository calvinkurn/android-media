package com.tokopedia.home.account.presentation.view.categorygridview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.home.account.presentation.view.categorygridview.model.CategoryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author okasurya on 7/19/18.
 */
class CategoryGridAdapter extends RecyclerView.Adapter<CategoryGridViewHolder> {
    private List<CategoryItem> categories;
    private CategoryGridView.OnClickListener listener;

    public CategoryGridAdapter() {
        this.categories = new ArrayList<>();
    }

    @NonNull
    @Override
    public CategoryGridViewHolder onCreateViewHolder(@NonNull ViewGroup view, int viewType) {
        // todo context from view(?)
        return new CategoryGridViewHolder(view.getContext(), view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryGridViewHolder holder, int position) {
        holder.bind(categories.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setListener(CategoryGridView.OnClickListener listener) {
        this.listener = listener;
    }

    public void setNewData(List<CategoryItem> items) {
        categories.clear();
        categories.addAll(items);
        notifyDataSetChanged();
    }
}
