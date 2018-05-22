package com.tokopedia.train.scheduledetail.presentation;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;

/**
 * Created by Rizky on 14/05/18.
 */
public interface TrainScheduleRouteDetailTypeFactory extends AdapterTypeFactory {

    int type(TrainScheduleRouteDetailViewModel viewModel);

}
