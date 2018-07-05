package com.tokopedia.tokopoints.view.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.model.CatalogCategory;
import com.tokopedia.tokopoints.view.presenter.CatalogListingPresenter;

import java.util.List;

public class CatalogChipAdapter extends RecyclerView.Adapter<CatalogChipAdapter.ViewHolder> {
    private List<CatalogCategory> mItems;
    private CatalogListingPresenter mPresenter;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView label;

        public ViewHolder(View view) {
            super(view);
            label = view.findViewById(R.id.text_label);
        }
    }

    public CatalogChipAdapter(CatalogListingPresenter presenter, List<CatalogCategory> items) {
        this.mPresenter = presenter;
        this.mItems = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tp_layout_catalog_chips, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CatalogCategory item = mItems.get(position);
        holder.label.setText(item.getName());

        if (mPresenter.getSelectedCategoryId() == item.getId()) {
            holder.label.setBackground(ContextCompat.getDrawable(holder.label.getContext(), R.drawable.bg_tp_category_bubble_selected));
        } else {
            holder.label.setBackground(ContextCompat.getDrawable(holder.label.getContext(), R.drawable.bg_tp_category_bubble_default));
        }

        holder.label.setOnClickListener(v -> {
            mPresenter.getView().updateSelectedCategoryId(item.getId());
            mPresenter.getView().refreshTab(item.getId());
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
