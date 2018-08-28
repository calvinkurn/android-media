package com.tokopedia.flight.detail.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.detail.presenter.ExpandableOnClickListener;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;

/**
 * Created by alvarisi on 12/27/17.
 */

public class FlightDetailOrderTypeFactory extends BaseAdapterTypeFactory {

    ExpandableOnClickListener expandableOnClickListener;
    float titleFontSize;

    public FlightDetailOrderTypeFactory(ExpandableOnClickListener expandableOnClickListener, float titleFontSize) {
        this.expandableOnClickListener = expandableOnClickListener;
        this.titleFontSize = titleFontSize;
    }

    public int type(FlightOrderJourney flightOrderJourney) {
        return FlightDetailOrderViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == FlightDetailOrderViewHolder.LAYOUT)
            return new FlightDetailOrderViewHolder(parent, expandableOnClickListener, titleFontSize);
        else
            return super.createViewHolder(parent, type);
    }
}
