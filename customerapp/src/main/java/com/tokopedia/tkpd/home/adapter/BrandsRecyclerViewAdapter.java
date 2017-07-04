package com.tokopedia.tkpd.home.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.network.entity.home.Brand;
import com.tokopedia.core.network.entity.home.Brands;
import com.tokopedia.tkpd.R;

/**
 * Created by Herdi_WORK on 31.01.17.
 */

public class BrandsRecyclerViewAdapter extends RecyclerView.Adapter<BrandsRecyclerViewAdapter.ItemRowHolder> {

    private Brands brands;
    private OnItemClickListener clickListener;
    private final int homeMenuWidth;

    public BrandsRecyclerViewAdapter(OnItemClickListener itemListener, int homeWidth) {
        clickListener = itemListener;
        brands = new Brands();
        homeMenuWidth = homeWidth;
    }

    public void setDataList(Brands dataList) {
        brands = dataList;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_brands_category, null
        );
        v.setMinimumWidth(homeMenuWidth);
        return new BrandsRecyclerViewAdapter.ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, int position) {
        if (position < brands.getData().size()) {
            final Brand singleBrand = brands.getData().get(position);

            if (singleBrand.getIsNew() != 1) {
                holder.labelNew.setVisibility(View.GONE);
            } else {
                holder.labelNew.setVisibility(View.VISIBLE);
            }

            if ((position + 1) % 3 == 0) {
                holder.separator.setVisibility(View.GONE);
            } else {
                holder.separator.setVisibility(View.VISIBLE);
            }

            ImageHandler.LoadImage(holder.ivBrands, singleBrand.getLogoUrl());
            holder.ivBrands.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClicked(singleBrand.getShopName(), singleBrand, holder.getAdapterPosition());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (brands.getData() != null)
            return brands.getData().size();
        else
            return 0;
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {
        ImageView ivBrands;
        View separator;
        TextView labelNew;

        ItemRowHolder(View view) {
            super(view);
            this.ivBrands = (ImageView) view.findViewById(R.id.iv_brands);
            this.separator = view.findViewById(R.id.separator);
            this.labelNew = (TextView) view.findViewById(R.id.text_new);
        }

    }

    public interface OnItemClickListener {
        void onItemClicked(String name, Brand brand, int position);
    }

}
