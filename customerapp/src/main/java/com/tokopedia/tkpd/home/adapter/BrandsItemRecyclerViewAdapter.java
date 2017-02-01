package com.tokopedia.tkpd.home.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.network.entity.home.Brand;
import com.tokopedia.core.network.entity.home.Brands;
import com.tokopedia.tkpd.R;

/**
 * Created by Herdi_WORK on 31.01.17.
 */

public class BrandsItemRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Brands brands;
    private int homeWidth;
    private OnItemClickListener brandsOnItemClickListener;

    BrandsItemRecyclerViewAdapter(Brands brandsData, int homeWidthData, OnItemClickListener itemClickListener){
        brands = brandsData;
        homeWidth = homeWidthData;
        brandsOnItemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_brands, null
        );
        v.setMinimumWidth(homeWidth);
        return new BrandsItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BrandsItemViewHolder brandsItemViewHolder = (BrandsItemViewHolder) holder;
        if(position<brands.getData().size()){
            final Brand singleItem = brands.getData().get(position);
            ImageHandler.LoadImage(brandsItemViewHolder.itemImage,singleItem.getLogoUrl());
            brandsItemViewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils.dumper("brands clicked");
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (null != brands ? (brands.getData().size()+1) : 0);
    }

    public interface OnItemClickListener {
        void onItemClicked(String name, Brand brand, int position);
    }

    class BrandsItemViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImage;
        protected View view;
        BrandsItemViewHolder(View view) {
            super(view);
            this.view = view;
            this.itemImage = (ImageView) view.findViewById(R.id.iv_brands);
        }
    }

}
