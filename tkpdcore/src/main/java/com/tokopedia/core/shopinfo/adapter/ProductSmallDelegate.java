package com.tokopedia.core.shopinfo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core2.R;
import com.tokopedia.core.customwidget.SquareImageView;
import com.tokopedia.core.shopinfo.models.productmodel.List;

/**
 * Created by Tkpd_Eka on 10/9/2015.
 */
public class ProductSmallDelegate {

    private class VHolder extends RecyclerView.ViewHolder {

        public SquareImageView img;
        public View mainView;

        public VHolder(View itemView) {
            super(itemView);
            img = (SquareImageView) itemView.findViewById(R.id.img);
            mainView = itemView;
        }
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_small, parent, false);
        return new VHolder(view);
    }

    public void onBindViewHolder(List item, RecyclerView.ViewHolder holder) {
        VHolder vholder = (VHolder) holder;
        ImageHandler.LoadImage(vholder.img, item.productImage300);
    }

    public void onItemClickListener(View.OnClickListener onClick, RecyclerView.ViewHolder holder){
        ((VHolder)holder).mainView.setOnClickListener(onClick);
    }

}
