package com.tokopedia.tracking.repository;

import com.tokopedia.tracking.viewmodel.TrackingViewModel;

import java.util.Map;

import rx.Observable;


/**
 * Created by kris on 5/14/18. Tokopedia
 */

public interface ITrackingPageRepository {

    Observable<TrackingViewModel> getRates(Map<String, String> trackParameters);

}
