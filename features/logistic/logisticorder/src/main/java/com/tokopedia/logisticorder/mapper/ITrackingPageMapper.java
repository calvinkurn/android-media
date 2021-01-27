package com.tokopedia.logisticorder.mapper;

import com.tokopedia.logisticCommon.data.entity.trackingshipment.TrackingResponse;
import com.tokopedia.logisticorder.uimodel.TrackingUiModel;

/**
 * Created by kris on 5/14/18. Tokopedia
 */

public interface ITrackingPageMapper {

    TrackingUiModel trackingUiModel(TrackingResponse trackingResponse);

}
