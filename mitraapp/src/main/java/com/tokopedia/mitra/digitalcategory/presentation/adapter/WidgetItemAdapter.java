package com.tokopedia.mitra.digitalcategory.presentation.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.mitra.R;

import java.util.List;

/**
 * Created by Rizky on 05/09/18.
 */
public class WidgetItemAdapter<T> extends RecyclerView.Adapter<WidgetItemAdapter.WidgetItemViewHolder> {

    private List<T> items;
    private String defaultId;

    public WidgetItemAdapter(List<T> items, String defaultId) {
        this.items = items;
        this.defaultId = defaultId;
    }

    @NonNull
    @Override
    public WidgetItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_widget, parent,
                false);
        return new WidgetItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WidgetItemViewHolder holder, int position) {
        holder.bind(items.get(position), defaultId);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class WidgetItemViewHolder<F> extends RecyclerView.ViewHolder {

        private TextView textProductName;
        private TextView textProductPrice;

        WidgetItemViewHolder(View itemView) {
            super(itemView);

            textProductName = itemView.findViewById(R.id.text_product_name);
            textProductPrice = itemView.findViewById(R.id.text_product_price);
        }

        void bind(F f, String defaultId) {
            textProductName.setText(((Product) f).getDesc());
            textProductPrice.setText(((Product) f).getPrice());
            if (((Product) f).getProductId().equals(defaultId)) {
                itemView.setBackgroundColor(itemView.getResources().getColor(R.color.green_50));
            } else {
                itemView.setBackgroundColor(itemView.getResources().getColor(R.color.white));
            }
        }

    }

}
