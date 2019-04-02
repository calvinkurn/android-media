package com.tokopedia.digital.nostylecategory.digitalcategory.presentation.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.common_digital.product.presentation.model.BaseWidgetItem;
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.digital.R;

import java.util.List;

/**
 * Created by Rizky on 05/09/18.
 */
public class WidgetItemAdapter extends RecyclerView.Adapter<WidgetItemAdapter.WidgetItemViewHolder> {

    private List<BaseWidgetItem> items;
    private String defaultId;

    private ActionListener actionListener;

    public interface ActionListener {

        void onItemSelected(Product product);

    }

    public WidgetItemAdapter(ActionListener actionListener, List<BaseWidgetItem> items, String defaultId) {
        this.actionListener = actionListener;
        this.items = items;
        this.defaultId = defaultId;
    }

    @NonNull
    @Override
    public WidgetItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_widget_digital, parent,
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

        private CardView cardView;
        private TextView textProductName;
        private TextView textProductPrice;

        private Product product;

        WidgetItemViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view);
            textProductName = itemView.findViewById(R.id.text_product_name);
            textProductPrice = itemView.findViewById(R.id.text_product_price);

            itemView.setOnClickListener(view -> actionListener.onItemSelected(product));
        }

        void bind(BaseWidgetItem f, String defaultId) {
            this.product = (Product) f;
            textProductName.setText(((Product) f).getDesc());
            textProductPrice.setText(((Product) f).getPrice());
            if (((Product) f).getProductId().equals(defaultId)) {
                cardView.setCardBackgroundColor(itemView.getResources().getColor(com.tokopedia.design.R.color.green_50));
            } else {
                cardView.setCardBackgroundColor(itemView.getResources().getColor(R.color.white));
            }
        }

    }

}
