package com.tokopedia.tkpd.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.network.entity.home.Brands;
import com.tokopedia.tkpd.R;

/**
 * Created by Herdi_WORK on 31.01.17.
 */

public class BrandsRecyclerViewAdapter extends RecyclerView.Adapter<BrandsRecyclerViewAdapter.ItemRowHolder>{

    private Context context;
    private Brands brands;
    private int homeWidth;
    private BrandsItemRecyclerViewAdapter.OnItemClickListener clickListener;

    public BrandsRecyclerViewAdapter(Context ctx, BrandsItemRecyclerViewAdapter.OnItemClickListener itemListener){
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
        @SuppressLint("InflateParams") View v = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_brands_category, null
        );
        return new BrandsRecyclerViewAdapter.ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, int position) {

        holder.itemTitle.setText("Official Store");
        BrandsItemRecyclerViewAdapter itemAdapter = new BrandsItemRecyclerViewAdapter(brands,
                homeWidth, clickListener);
        holder.recyclerViewList.setHasFixedSize(true);
        holder.recyclerViewList.setLayoutManager(
                new LinearLayoutManager(context,
                        LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerViewList.setAdapter(itemAdapter);

    }

    @Override
    public int getItemCount() {
        if(brands.getData()!=null)
            return brands.getData().size();
        else
            return 0;
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {

        TextView itemTitle;
        RecyclerView recyclerViewList;

        ItemRowHolder(View view) {
            super(view);
            this.itemTitle = (TextView) view.findViewById(R.id.tv_title);
            this.recyclerViewList = (RecyclerView) view.findViewById(R.id.rv_view_list);
        }

    }

}
