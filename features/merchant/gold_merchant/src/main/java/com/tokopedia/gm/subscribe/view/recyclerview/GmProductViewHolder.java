package com.tokopedia.gm.subscribe.view.recyclerview;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.gm.R;
import com.tokopedia.gm.subscribe.view.viewmodel.GmProductViewModel;

/**
 * Created by sebastianuskh on 11/23/16.
 */

public class GmProductViewHolder extends RecyclerView.ViewHolder {

    private final Context mContext;

    TextView titlePackage;

    TextView promoPackage;

    TextView descriptionPackage;

    TextView descriptionFreeDays;

    TextView pricePackage;

    TextView lastPricePakcage;

    ImageView iconCheck;

    RelativeLayout layoutView;

    public GmProductViewHolder(View itemView) {
        super(itemView);
        titlePackage = (TextView) itemView.findViewById(R.id.title_package);
        promoPackage = (TextView) itemView.findViewById(R.id.promo_package);
        descriptionPackage = (TextView) itemView.findViewById(R.id.description_package);
        descriptionFreeDays = (TextView) itemView.findViewById(R.id.description_free_days);
        pricePackage = (TextView)itemView.findViewById(R.id.price_package);
        lastPricePakcage = (TextView) itemView.findViewById(R.id.last_price_package);
        iconCheck = (ImageView) itemView.findViewById(R.id.icon_check);
        layoutView = (RelativeLayout) itemView.findViewById(R.id.layout_view);
        mContext = itemView.getContext();
    }

    public void renderData(GmProductViewModel gmProductViewModel, boolean isSelected) {
        titlePackage.setText(gmProductViewModel.getName());
        descriptionPackage.setText(gmProductViewModel.getNotes());
        promoPackage.setVisibility(
                gmProductViewModel.isBestDeal() ? View.VISIBLE : View.GONE);
        pricePackage.setText(gmProductViewModel.getPrice());
        if (!gmProductViewModel.getFreeDays().isEmpty()) {
            descriptionFreeDays.setVisibility(View.VISIBLE);
            descriptionFreeDays.setText(gmProductViewModel.getFreeDays());
        }
        if (!gmProductViewModel.getLastPrice().isEmpty()) {
            setViewForLastPrice(gmProductViewModel.getLastPrice());
        }
        if (isSelected) {
            setSelected();
        } else {
            setUnselected();
        }
    }

    private void setViewForLastPrice(String lastPrice) {
        pricePackage.setPaintFlags(pricePackage.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        pricePackage.setTextColor(mContext.getResources().getColor(R.color.primary_text_default_material_light));
        pricePackage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        lastPricePakcage.setText(lastPrice);
        lastPricePakcage.setVisibility(View.VISIBLE);
    }

    public void setSelected() {
        iconCheck.setVisibility(View.VISIBLE);
        try {
            layoutView.setBackgroundResource(R.drawable.background_gmsubscribe_product_item_selected);
        } catch (NoSuchMethodError e) {
            layoutView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.background_gmsubscribe_product_item_selected));
        }
    }

    public void setUnselected() {
        iconCheck.setVisibility(View.GONE);
        try {
            layoutView.setBackgroundResource(R.drawable.background_gmsubscribe_product_item_unselected);
        } catch (NoSuchMethodError e) {
            layoutView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.background_gmsubscribe_product_item_unselected));
        }
    }


}
