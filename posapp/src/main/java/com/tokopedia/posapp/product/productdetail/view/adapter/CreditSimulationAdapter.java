package com.tokopedia.posapp.product.productdetail.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.product.model.productdetail.ProductInstallment;
import com.tokopedia.posapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by okasurya on 8/14/17.
 */

public class CreditSimulationAdapter
        extends RecyclerView.Adapter<CreditSimulationAdapter.ViewHolder> {

    private Context context;
    List<ProductInstallment> productInstallments;

    public CreditSimulationAdapter(Context context) {
        this.context = context;
        productInstallments = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_credit_simulation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position % 2 == 0) {
            holder.rootView.setBackgroundColor(ContextCompat.getColor(context, R.color.grey_200));
        } else {
            holder.rootView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }
        ImageHandler.loadImageAndCache(holder.icon, productInstallments.get(position).getIcon());
        holder.title.setText(productInstallments.get(position).getName());

        holder.month3.setText(
                productInstallments.get(position).getTerms().getRule3Months() != null ? "v" : "-");
        holder.month6.setText(
                productInstallments.get(position).getTerms().getRule6Months() != null ? "v" : "-");
        holder.month12.setText(
                productInstallments.get(position).getTerms().getRule12Months() != null ? "v" : "-");
        holder.month18.setText(
                productInstallments.get(position).getTerms().getRule18Months() != null ? "v" : "-");
        holder.month24.setText(
                productInstallments.get(position).getTerms().getRule24Months() != null ? "v" : "-");
    }

    @Override
    public int getItemCount() {
        return productInstallments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout rootView;
        ImageView icon;
        TextView title;
        TextView month3;
        TextView month6;
        TextView month12;
        TextView month18;
        TextView month24;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.rootview);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            month3 = itemView.findViewById(R.id.month3);
            month6 = itemView.findViewById(R.id.month6);
            month12 = itemView.findViewById(R.id.month12);
            month18 = itemView.findViewById(R.id.month18);
            month24 = itemView.findViewById(R.id.month24);
        }
    }

    public void addData(List<ProductInstallment> productInstallments) {
        this.productInstallments.clear();
        this.productInstallments.addAll(productInstallments);
        notifyDataSetChanged();
    }
}
