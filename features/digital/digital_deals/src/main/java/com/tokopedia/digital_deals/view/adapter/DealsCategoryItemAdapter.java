package com.tokopedia.digital_deals.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.activity.CategoryDetailActivity;
import com.tokopedia.digital_deals.view.model.CategoriesModel;
import com.tokopedia.digital_deals.view.model.CategoryItem;
import com.tokopedia.digital_deals.view.utils.DealsAnalytics;
import com.tokopedia.digital_deals.view.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DealsCategoryItemAdapter extends RecyclerView.Adapter<DealsCategoryItemAdapter.ViewHolder> {

    private List<CategoryItem> categoryItems;
    private Context context;
    DealsAnalytics dealsAnalytics;
    CategorySelected categorySelected;

    public DealsCategoryItemAdapter(List<CategoryItem> categoryItems, CategorySelected categorySelected) {
        this.categoryItems = new ArrayList<>();
        this.categoryItems = categoryItems;
        this.categorySelected = categorySelected;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View itemView;
        private LinearLayout mainContent;
        private ImageView imageViewCatItem;
        private TextView textViewCatItem;
        private int index;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            mainContent = itemView.findViewById(R.id.container);
            imageViewCatItem = itemView.findViewById(R.id.iv_category);
            textViewCatItem = itemView.findViewById(R.id.tv_category);
        }

        public void bindData(final CategoryItem categoryItem) {
            textViewCatItem.setText(categoryItem.getTitle());
            itemView.setOnClickListener(this);
            ImageHandler.loadImage(context, imageViewCatItem, categoryItem.getMediaUrl(), R.color.grey_1100, R.color.grey_1100);
        }

        public void setIndex(int position) {
            this.index = position;
        }

        public int getIndex() {
            return this.index;
        }

        @Override
        public void onClick(View v) {
            dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_SELECT_VOUCHER_CATEGORY,
                    categoryItems.get(getIndex()).getTitle());

            CategoriesModel categoriesModel = new CategoriesModel();
            categoriesModel.setName(categoryItems.get(getIndex()).getName());
            categoriesModel.setTitle(categoryItems.get(getIndex()).getTitle());
            categoriesModel.setCategoryUrl(categoryItems.get(getIndex()).getCategoryUrl());
            categoriesModel.setCategoryId(categoryItems.get(getIndex()).getCategoryId());
            categoriesModel.setPosition(getIndex() + 1);
            categorySelected.openCategoryDetail(categoriesModel);
        }
    }

    public interface CategorySelected {
        void openCategoryDetail(CategoriesModel categoriesModel);
    }

    @Override
    public int getItemCount() {
        return (categoryItems == null) ? 0 : categoryItems.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        dealsAnalytics=new DealsAnalytics();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(categoryItems.get(position));
        holder.setIndex(position);
    }

}
