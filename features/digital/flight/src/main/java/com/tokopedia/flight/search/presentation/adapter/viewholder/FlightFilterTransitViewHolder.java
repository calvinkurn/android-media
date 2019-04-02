package com.tokopedia.flight.search.presentation.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.presentation.model.resultstatistics.TransitStat;

/**
 * Created by alvarisi on 12/21/17.
 */

public class FlightFilterTransitViewHolder extends BaseCheckableViewHolder<TransitStat> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_flight_general_filter;
    TextView tvTitle;
    TextView tvDesc;
    CheckBox checkBox;

    public FlightFilterTransitViewHolder(View itemView, CheckableInteractionListener checkableInteractionListener) {
        super(itemView, checkableInteractionListener);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        tvDesc = (TextView) itemView.findViewById(R.id.tv_desc);
        checkBox = (AppCompatCheckBox) itemView.findViewById(R.id.checkbox);
    }

    @Override
    public void bind(TransitStat transitStat) {
        super.bind(transitStat);
        tvTitle.setText(transitStat.getTransitType().getValueRes());
        tvDesc.setText(getString(R.string.start_from_x, transitStat.getMinPriceString()));
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
}
