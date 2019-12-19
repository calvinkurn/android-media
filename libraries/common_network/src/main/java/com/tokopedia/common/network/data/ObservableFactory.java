package com.tokopedia.common.network.data;

import android.content.Context;
import androidx.annotation.NonNull;

import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.common.network.data.model.RestResponseIntermediate;
import com.tokopedia.common.network.data.source.repository.RestRepositoryImpl;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import rx.Observable;

public class ObservableFactory {
    public static Observable<Map<Type, RestResponse>> create(@NonNull List<RestRequest> requests) {
        RestRepositoryImpl repository = new RestRepositoryImpl();
        return repository.getResponses(requests).map(responses -> {
            Map<Type, RestResponse> results = new HashMap<>();
            for (RestResponseIntermediate res : responses) {
                if (res == null) {
                    continue;
                }

                RestResponse response = new RestResponse(res.getOriginalResponse(), res.getCode(), res.isCached());
                response.setError(res.isError());
                response.setErrorBody(res.getErrorBody());
                results.put(res.getType(), response);
            }

            return results;
        });
    }

    public static Observable<Map<Type, RestResponse>> create(@NonNull List<RestRequest> requests, @NonNull List<Interceptor> interceptors, @NonNull Context context) {
        RestRepositoryImpl repository = new RestRepositoryImpl(interceptors, context);
        return repository.getResponses(requests).map(responses -> {
            Map<Type, RestResponse> results = new HashMap<>();
            for (RestResponseIntermediate res : responses) {
                if (res == null) {
                    continue;
                }

                RestResponse response = new RestResponse(res.getOriginalResponse(), res.getCode(), res.isCached());
                response.setError(res.isError());
                response.setErrorBody(res.getErrorBody());
                results.put(res.getType(), response);
            }

            return results;
        });
    }
}
