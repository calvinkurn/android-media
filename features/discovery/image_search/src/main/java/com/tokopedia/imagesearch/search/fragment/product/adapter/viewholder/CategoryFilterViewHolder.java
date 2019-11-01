package com.tokopedia.imagesearch.search.fragment.product.adapter.viewholder;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.filter.newdynamicfilter.adapter.viewholder.decoration.LinearHorizontalSpacingDecoration;
import com.tokopedia.imagesearch.R;
import com.tokopedia.imagesearch.domain.viewmodel.CategoryFilterModel;
import com.tokopedia.imagesearch.search.fragment.product.adapter.listener.CategoryFilterListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryFilterViewHolder extends AbstractViewHolder<CategoryFilterModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.image_search_category_filter_layout;

    CategoryFilterListener categoryFilterListener;
    CategoryFilterAdapter categoryFilterAdapter;
    RecyclerView categoryFilterListView;
    Context context;

    public CategoryFilterViewHolder(View itemView, CategoryFilterListener categoryFilterListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.categoryFilterListener = categoryFilterListener;
        categoryFilterListView = itemView.findViewById(R.id.categoryFilterListView);
        initCategoryFilterRecyclerView();
    }

    @Override
    public void bind(CategoryFilterModel categoryFilterModel) {
        bindCategoryFilterView(categoryFilterModel);
    }

    private void initCategoryFilterRecyclerView() {
        categoryFilterAdapter = new CategoryFilterAdapter(categoryFilterListener);
        categoryFilterListView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        categoryFilterListView.setAdapter(categoryFilterAdapter);
        categoryFilterListView.addItemDecoration(new LinearHorizontalSpacingDecoration(
                context.getResources().getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_8),
                context.getResources().getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_16)
        ));
    }

    private void bindCategoryFilterView(CategoryFilterModel element) {
        categoryFilterAdapter.setItemList(element.getItemList());
    }

    private static class CategoryFilterAdapter extends RecyclerView.Adapter<CategoryFilterItemViewHolder> {

        private List<CategoryFilterModel.Item> itemList = new ArrayList<>();
        private CategoryFilterListener categoryFilterListener;

        CategoryFilterAdapter(CategoryFilterListener categoryFilterListener) {
            this.categoryFilterListener = categoryFilterListener;
        }

        void setItemList(List<CategoryFilterModel.Item> itemList) {
            this.itemList.clear();
            this.itemList.addAll(itemList);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public CategoryFilterItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_search_category_filter_item, parent, false);
            return new CategoryFilterItemViewHolder(view, categoryFilterListener);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryFilterItemViewHolder holder, int position) {
                holder.bind(itemList.get(position));
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }
    }

    private static class CategoryFilterItemViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryFilterText;
        private View itemContainer;
        private final CategoryFilterListener categoryFilterListener;

        CategoryFilterItemViewHolder(View itemView, CategoryFilterListener categoryFilterListener) {
            super(itemView);
            categoryFilterText = itemView.findViewById(R.id.category_filter_text);
            itemContainer = itemView.findViewById(R.id.filter_item_container);
            this.categoryFilterListener = categoryFilterListener;
        }

        public void bind(final CategoryFilterModel.Item item) {

            categoryFilterText.setText(item.getName());

            setBackgroundResource(item);

            categoryFilterText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (categoryFilterListener != null) {
                        categoryFilterListener.onCategoryFilterSelected(item);
                    }
                }
            });
        }

        private void setBackgroundResource(CategoryFilterModel.Item item) {
            if (categoryFilterListener != null && categoryFilterListener.isCategoryFilterSelected(item.getCategoryId())) {
                itemContainer.setBackgroundResource(R.drawable.image_search_category_filter_item_background_selected);
            } else {
                itemContainer.setBackgroundResource(R.drawable.image_search_category_filter_item_background_neutral);
            }
        }
    }
}
