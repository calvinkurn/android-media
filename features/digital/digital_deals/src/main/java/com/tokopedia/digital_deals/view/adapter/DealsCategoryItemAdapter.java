package com.tokopedia.digital_deals.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.activity.DealsCategoryDetailActivity;
import com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter;
import com.tokopedia.digital_deals.view.viewmodel.CategoriesModel;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class DealsCategoryItemAdapter extends RecyclerView.Adapter<DealsCategoryItemAdapter.ViewHolder> {

    private List<CategoryViewModel> categoryItems;
    private Context context;

    public DealsCategoryItemAdapter(Context context, List<CategoryViewModel> categoryItems) {
        this.context = context;
        this.categoryItems = new ArrayList<>();
        this.categoryItems = categoryItems;

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View itemView;
        private ImageView imageViewCatItem;
        private TextView textViewCatItem;

        private int index;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            imageViewCatItem = itemView.findViewById(R.id.imageViewCatItem);
            textViewCatItem = itemView.findViewById(R.id.textViewCatItem);
        }

        public void bindData(final CategoryViewModel categoryViewModel) {
//            discount.setText(categoryItemsViewModel.getd);
            textViewCatItem.setText(categoryViewModel.getName());
            itemView.setOnClickListener(this);
        }

        public void setIndex(int position) {
            this.index = position;
        }

        public int getIndex() {
            return this.index;
        }

        @Override
        public void onClick(View v) {
            CategoriesModel categoriesModel=new CategoriesModel();
            categoriesModel.setName(categoryItems.get(getIndex()).getName());
            categoriesModel.setTitle(categoryItems.get(getIndex()).getTitle());
            categoriesModel.setUrl(categoryItems.get(getIndex()).getUrl());
            Intent detailsIntent = new Intent(context, DealsCategoryDetailActivity.class);
            detailsIntent.putExtra(DealDetailsPresenter.HOME_DATA, categoriesModel);
            context.startActivity(detailsIntent);
        }
    }

    @Override
    public int getItemCount() {
        if (categoryItems != null) {
            return categoryItems.size();
        }
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
