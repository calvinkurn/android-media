package com.tokopedia.flight.filter.presentation.adapter.viewholder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder;
import com.tokopedia.flight.search.presentation.model.statistics.AirlineStat;

/**
 * Created by alvarisi on 12/21/17.
 */

public class FlightFilterAirlineViewHolder extends BaseCheckableViewHolder<AirlineStat> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    @LayoutRes
    public static final int LAYOUT = com.tokopedia.flight.R.layout.item_flight_airline_filter;

    Context context;
    ImageView ivLogo;
    TextView tvTitle;
    TextView tvDesc;
    CheckBox checkBox;

    public FlightFilterAirlineViewHolder(View itemView, CheckableInteractionListener checkableInteractionListener) {
        super(itemView, checkableInteractionListener);
        this.context = itemView.getContext();
        ivLogo = (ImageView) itemView.findViewById(com.tokopedia.flight.R.id.iv_logo);
        tvTitle = (TextView) itemView.findViewById(com.tokopedia.flight.R.id.tv_title);
        tvDesc = (TextView) itemView.findViewById(com.tokopedia.flight.R.id.tv_desc);
        checkBox = (AppCompatCheckBox) itemView.findViewById(com.tokopedia.flight.R.id.checkbox);
    }

    @Override
    public void bind(AirlineStat airlineStat) {
        super.bind(airlineStat);
        loadImageWithPlaceholder(ivLogo, airlineStat.getAirlineDB().getLogo(), ContextCompat.getDrawable(itemView.getContext(), com.tokopedia.flight.R.drawable.flight_ic_airline_default));
        tvTitle.setText(airlineStat.getAirlineDB().getName());
        tvDesc.setText(getString(com.tokopedia.flight.R.string.start_from_x, airlineStat.getMinPriceString()));
        itemView.setOnClickListener(this);
    }

    @Override
    public CompoundButton getCheckable() {
        return checkBox;
    }

    @Override
    public void onClick(View v) {
        toggle();
    }

    private void loadImageWithPlaceholder(ImageView imageview, String url, Drawable resId) {
        if (url != null && !TextUtils.isEmpty(url)) {
            Glide.with(imageview.getContext())
                    .load(url)
                    .placeholder(resId)
                    .dontAnimate()
                    .error(resId)
                    .into(imageview);
        } else {
            Glide.with(imageview.getContext())
                    .load(url)
                    .placeholder(resId)
                    .error(resId)
                    .into(imageview);
        }
    }
}

