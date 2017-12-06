package com.tokopedia.discovery.dynamicfilter;

import android.content.Context;
import android.util.Log;

import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.core.network.apiservices.product.apis.DynamicFilter;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.RetrofitUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by noiz354 on 7/11/16.
 * migrate retrofit 2 by Angga.Prasetiyo
 */
@Deprecated
public class DynamicFilterFactory {

    private static final String TAG = DynamicFilterFactory.class.getSimpleName();

    public static DynamicFilter createDynamicFilter() {
        return RetrofitUtils
                .createRetrofit(DynamicFilter.DYNAMIC_FILTER_URL)
                .create(DynamicFilter.class);
    }

    public static Observable<Response<DynamicFilterModel>> createDynamicFilterObservable(
            Context context, String source, String device, String sc, String query) {

        Log.d(TAG, "Source " + source + " Device " + device + " SC " + sc + " q " + query);

        Map<String, String> params = new HashMap<>();
        params.put(DynamicFilter.SC, sc);
        params.put(DynamicFilter.DEVICE, device);
        params.put(DynamicFilter.SOURCE, source);
        params.put(DynamicFilter.QUERY, query);
        Observable<Response<DynamicFilterModel>> responseObservable
                = createDynamicFilter().browseCatalogs(AuthUtil.generateParams(context, params));

        return responseObservable;
    }
}
