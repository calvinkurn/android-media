package com.tokopedia.tracking.mapper;

import com.tokopedia.logisticdata.data.entity.trackingshipment.TrackingResponse;
import com.tokopedia.tracking.viewmodel.TrackingViewModel;

/**
 * Created by kris on 5/14/18. Tokopedia
 */

public interface ITrackingPageMapper {

    TrackingViewModel trackingViewModel(TrackingResponse trackingResponse);

}
