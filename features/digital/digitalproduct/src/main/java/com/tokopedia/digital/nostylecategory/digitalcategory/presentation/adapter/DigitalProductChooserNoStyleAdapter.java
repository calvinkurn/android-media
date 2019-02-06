package com.tokopedia.digital.nostylecategory.digitalcategory.presentation.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.digital.R;

import java.util.List;

/**
 * Created by Rizky on 10/09/18.
 */
public class DigitalProductChooserNoStyleAdapter extends RecyclerView.Adapter<DigitalProductChooserNoStyleAdapter.ViewHolder> {

    private List<Product> products;

    public DigitalProductChooserNoStyleAdapter(List<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mitra_digital_product,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(Product product) {

        }

    }

}
