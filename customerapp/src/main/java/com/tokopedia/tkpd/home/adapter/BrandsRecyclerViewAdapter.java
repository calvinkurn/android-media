package com.tokopedia.tkpd.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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

public class BrandsRecyclerViewAdapter extends RecyclerView.Adapter<BrandsRecyclerViewAdapter.ItemRowHolder>{

    private Context context;
    private Brands brands;
    private int homeWidth;
    private OnItemClickListener clickListener;

    public BrandsRecyclerViewAdapter(Context ctx, OnItemClickListener itemListener){
        context = ctx;
        clickListener = itemListener;
        brands = new Brands();
    }

    public void setHomeMenuWidth(int homeMenuWidth) {
        homeWidth = homeMenuWidth;
    }

    public void setDataList(Brands dataList) {
        brands = dataList;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CommonUtils.dumper("mohito on create viewholder brandsrecviewadapter");

        @SuppressLint("InflateParams") View v = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_brands_category, null
        );
        return new BrandsRecyclerViewAdapter.ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, int position) {

        CommonUtils.dumper("mohito on bind viewholder brandsrecviewadapter "+position);

        if(position<brands.getData().size()){
            final Brand singleBrand = brands.getData().get(position);
            ImageHandler.LoadImage(holder.ivBrands,singleBrand.getLogoUrl());
            holder.ivBrands.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClicked("",singleBrand, holder.getAdapterPosition());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if(brands.getData()!=null)
            return brands.getData().size();
        else
            return 0;
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {
        ImageView ivBrands;

        ItemRowHolder(View view) {
            super(view);
            this.ivBrands = (ImageView) view.findViewById(R.id.iv_brands);
        }

    }

    public interface OnItemClickListener {
        void onItemClicked(String name, Brand brand, int position);
    }

}
