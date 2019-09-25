package com.tokopedia.filter.newdynamicfilter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.design.item.DeletableItemView;
import com.tokopedia.filter.R;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.filter.newdynamicfilter.helper.RatingHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 8/11/17.
 */

public class PricePillsAdapter extends
        RecyclerView.Adapter<PricePillsAdapter.ViewHolder> {

    private List<Option> pricePills = new ArrayList<>();
    private Callback callback;

    public PricePillsAdapter(Callback callback) {
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.filter_price_pill_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(pricePills.get(position), position);
    }

    @Override
    public int getItemCount() {
        return pricePills.size();
    }

    public void setPricePills(List<Option> pricePills) {
        this.pricePills = pricePills;
        notifyDataSetChanged();
    }

    public void refreshData() {
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView pricePillItem;
        Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            pricePillItem = itemView.findViewById(R.id.price_pill_item);
        }

        public void bind(final Option pricePillOption, final int position) {
            if (isValueRangeMatch(pricePillOption)) {
                pricePillItem.setTextColor(context.getResources().getColor(com.tokopedia.design.R.color.unify_G500));
                pricePillItem.setBackground(context.getResources().getDrawable(R.drawable.filter_price_pill_item_background_selected));
            } else {
                pricePillItem.setTextColor(context.getResources().getColor(com.tokopedia.design.R.color.white));
                pricePillItem.setBackground(context.getResources().getDrawable(R.drawable.filter_price_pill_item_background_neutral));
            }
            pricePillItem.setText(pricePillOption.getName());
            pricePillItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onPriceRangeClicked(
                                Integer.parseInt(pricePillOption.getValMin()),
                                Integer.parseInt(pricePillOption.getValMax())
                        );
                    }
                }
            });
        }

        private boolean isValueRangeMatch(Option pricePillOption) {
            return Integer.parseInt(pricePillOption.getValMin()) == callback.getCurrentPriceMin()
                    && Integer.parseInt(pricePillOption.getValMax()) == callback.getCurrentPriceMax();
        }
    }

    public interface Callback {
        void onPriceRangeClicked(int minValue, int maxValue);
        int getCurrentPriceMin();
        int getCurrentPriceMax();
    }
}
