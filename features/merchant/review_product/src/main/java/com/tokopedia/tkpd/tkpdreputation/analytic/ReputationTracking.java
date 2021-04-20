package com.tokopedia.tkpd.tkpdreputation.analytic;

import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

import java.util.Map;

/**
 * Created by zulfikarrahman on 3/13/18.
 */

public class ReputationTracking {

    public void eventImageClickOnReview(String productId,
                                        String reviewId) {

        String KEY_PRODUCT_ID = "productId";
        Map<String, Object> mapEvent = TrackAppUtils.gtmData(
                ReputationTrackingConstant.CLICK_PDP,
                ReputationTrackingConstant.PRODUCT_DETAIL_PAGE,
                "click - review gallery on rating list",
                String.format(
                        "product_id: %s - review_id : %s",
                        productId,
                        reviewId
                )
        );
        mapEvent.put(KEY_PRODUCT_ID, productId);
        TrackApp.getInstance().getGTM().sendGeneralEvent(mapEvent);
    }

    public void eventClickFilterReview(String filterName,
                                       String productId) {
        String KEY_PRODUCT_ID = "productId";
        Map<String, Object> mapEvent = TrackAppUtils.gtmData(
                ReputationTrackingConstant.CLICK_PDP,
                ReputationTrackingConstant.PRODUCT_DETAIL_PAGE,
                String.format(
                        "click - filter review by %s",
                        filterName.toLowerCase()
                ),
                productId
        );
        mapEvent.put(KEY_PRODUCT_ID, productId);
        TrackApp.getInstance().getGTM().sendGeneralEvent(mapEvent);
    }
}
