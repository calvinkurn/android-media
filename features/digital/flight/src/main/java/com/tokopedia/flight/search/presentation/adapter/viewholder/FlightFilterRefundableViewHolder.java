package com.tokopedia.flight.search.presentation.adapter.viewholder;

import androidx.annotation.LayoutRes;
import androidx.appcompat.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder;
import com.tokopedia.flight.search.presentation.model.resultstatistics.RefundableStat;

/**
 * Created by alvarisi on 12/21/17.
 */

public class FlightFilterRefundableViewHolder extends BaseCheckableViewHolder<RefundableStat> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    @LayoutRes
    public static final int LAYOUT = com.tokopedia.flight.R.layout.item_flight_general_filter;
    TextView tvTitle;
    TextView tvDesc;
    CheckBox checkBox;

    public FlightFilterRefundableViewHolder(View itemView, CheckableInteractionListener checkableInteractionListener) {
        super(itemView, checkableInteractionListener);
        tvTitle = (TextView) itemView.findViewById(com.tokopedia.design.R.id.tv_title);
        tvDesc = (TextView) itemView.findViewById(com.tokopedia.flight.R.id.tv_desc);
        checkBox = (AppCompatCheckBox) itemView.findViewById(com.tokopedia.flight.R.id.checkbox);
    }

    @Override
    public void bind(RefundableStat refundableStat) {
        super.bind(refundableStat);
        tvTitle.setText(refundableStat.getRefundableEnum().getValueRes());
        tvDesc.setText(getString(com.tokopedia.flight.R.string.start_from_x, refundableStat.getMinPriceString()));
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
