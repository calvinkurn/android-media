package com.tokopedia.train.reviewdetail.presentation.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.train.reviewdetail.presentation.model.TrainReviewPassengerInfoViewModel;

/**
 * Created by Rizky on 02/07/18.
 */
public class TrainPassengerAdapterTypeFactory extends BaseAdapterTypeFactory implements AdapterTypeFactory {

    public int type(TrainReviewPassengerInfoViewModel viewModel) {
        return TrainPassengerSeatViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == TrainPassengerSeatViewHolder.LAYOUT) {
            return new TrainPassengerSeatViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }

}