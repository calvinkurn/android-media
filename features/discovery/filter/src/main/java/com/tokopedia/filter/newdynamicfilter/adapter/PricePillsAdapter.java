package com.tokopedia.filter.newdynamicfilter.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.filter.R;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.filter.common.helper.NumberParseHelper;

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
            boolean isPricePillSelected = isValueRangeMatch(pricePillOption);
            if (isPricePillSelected) {
                pricePillItem.setTextColor(context.getResources().getColor(com.tokopedia.design.R.color.unify_G500));
                pricePillItem.setBackground(context.getResources().getDrawable(R.drawable.filter_price_pill_item_background_selected));
            } else {
                pricePillItem.setTextColor(context.getResources().getColor(R.color.price_pills_text_color_normal));
                pricePillItem.setBackground(context.getResources().getDrawable(R.drawable.filter_price_pill_item_background_neutral));
            }
            pricePillItem.setText(pricePillOption.getName());
            pricePillItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        if (!isPricePillSelected) {
                            callback.onPriceRangeSelected(
                                    NumberParseHelper.safeParseInt(pricePillOption.getValMin()),
                                    NumberParseHelper.safeParseInt(pricePillOption.getValMax())
                            );
                        } else {
                            callback.onPriceRangeRemoved();
                        }
                    }
                }
            });
        }

        private boolean isValueRangeMatch(Option pricePillOption) {
            String valMin = pricePillOption.getValMin();
            String valMax = pricePillOption.getValMax();

            if (TextUtils.isEmpty(valMin) || TextUtils.isEmpty(valMax)) {
                return false;
            }

            return Integer.parseInt(valMin) == callback.getCurrentPriceMin()
                    && Integer.parseInt(valMax) == callback.getCurrentPriceMax();
        }
    }

    public interface Callback {
        void onPriceRangeSelected(int minValue, int maxValue);
        void onPriceRangeRemoved();
        int getCurrentPriceMin();
        int getCurrentPriceMax();
    }
}
