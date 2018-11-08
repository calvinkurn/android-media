package com.tokopedia.flight.search.util;

import com.tokopedia.flight.search.presentation.model.FlightSearchApiRequestModel;

import java.util.HashMap;

/**
 * Created by User on 10/30/2017.
 */

public class FlightSearchParamUtil {
    private static final String PARAM_INITIAL_SEARCH = "initial_search";
    private static final String PARAM_IS_RETURNING = "is_return";
    private static final String PARAM_FROM_CACHE = "from_cache";
    private static final String PARAM_FILTER_MODEL = "filter_model";
    private static final String PARAM_SORT = "param_sort";

    public static FlightSearchApiRequestModel getInitialPassData(HashMap<String, Object> hashMap){
        return (FlightSearchApiRequestModel) hashMap.get(PARAM_INITIAL_SEARCH);
    }

}
