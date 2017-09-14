package com.tokopedia.core.network.apiservices.maps.api;

import android.database.Observable;

import java.util.ArrayList;

/**
 * Created by kris on 9/14/17. Tokopedia
 */

public interface MapApi {

    Observable<ArrayList<String>> getRecommendedPlaces();

}
