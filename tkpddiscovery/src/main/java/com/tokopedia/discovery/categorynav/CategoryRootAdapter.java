package com.tokopedia.discovery.categorynav;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.discovery.R;
import com.tokopedia.core.network.entity.categories.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alifa on 7/6/17.
 */

public class CategoryRootAdapter extends RecyclerView.Adapter<CategoryRootAdapter.ItemRowHolder> {

    private List<Category> categories;
    private OnItemClickListener clickListener;

    public CategoryRootAdapter(OnItemClickListener itemListener) {
        clickListener = itemListener;
        categories = new ArrayList<>();
    }

    public void setDataList(List<Category> dataList) {
        categories = dataList;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_category_root, null
        );
        return new CategoryRootAdapter.ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        if (categories != null)
            return categories.size();
        else
            return 0;
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {
        ImageView iconCategory;
        TextView CategotyName;

        ItemRowHolder(View view) {
            super(view);
           // this.iconCategory = (ImageView) view.findViewById(R.id.iv_brands);
            this.CategotyName = (TextView) view.findViewById(R.id.dynamic_filter_list_text);
        }

    }

    public interface OnItemClickListener {
        void onItemClicked(Category category);
    }

}