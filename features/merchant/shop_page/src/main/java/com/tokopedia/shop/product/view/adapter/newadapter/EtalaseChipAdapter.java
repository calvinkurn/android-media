package com.tokopedia.shop.product.view.adapter.newadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.shop.R;
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;

import java.util.ArrayList;
import java.util.List;

public class EtalaseChipAdapter extends RecyclerView.Adapter<EtalaseChipAdapter.ShopProductEtalaseChipViewHolder>{

    private List<ShopEtalaseViewModel> etalaseViewModelList;
    private RecyclerView recyclerView;

    public  EtalaseChipAdapter(List<ShopEtalaseViewModel> shopEtalaseViewModelList) {
        setEtalaseViewModelList(shopEtalaseViewModelList);
    }

    public void setEtalaseViewModelList(List<ShopEtalaseViewModel> etalaseViewModelList) {
        if (etalaseViewModelList == null) {
            this.etalaseViewModelList = new ArrayList<>();
        } else {
            this.etalaseViewModelList = etalaseViewModelList;
        }
    }

    class ShopProductEtalaseChipViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        public ShopProductEtalaseChipViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
        }
    }


    @Override
    public ShopProductEtalaseChipViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_shop_product_etalase_chip, parent, false);
        return new ShopProductEtalaseChipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShopProductEtalaseChipViewHolder holder, int position) {
        ShopEtalaseViewModel shopEtalaseViewModel = etalaseViewModelList.get(position);
        holder.textView.setText(shopEtalaseViewModel.getEtalaseName());
    }

    @Override
    public int getItemCount() {
        return etalaseViewModelList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }

}
