package com.tokopedia.autocomplete.usecase;

import android.text.TextUtils;

import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.autocomplete.domain.model.SearchData;
import com.tokopedia.autocomplete.repository.AutoCompleteRepository;
import com.tokopedia.discovery.common.constants.SearchApiConst;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

public class AutoCompleteUseCase extends UseCase<List<SearchData>> {

    public static final String KEY_DEVICE = "device";
    public static final String KEY_SOURCE = "source";
    public static final String KEY_QUERY = "q";
    public static final String KEY_UNIQUE_ID = "unique_id";
    public static final String KEY_COUNT = "count";
    public static final String KEY_USER_ID = "user_id";
    public static final String DEFAULT_DEVICE = "android";
    public static final String DEFAULT_SOURCE = "searchbar";
    public static final String DEFAULT_COUNT = "5";
    private static final String DEVICE_ID = "device_id";
    private static final String KEY_IS_OFFICIAL = "official";

    private final AutoCompleteRepository autoCompleteRepository;

    public AutoCompleteUseCase(
            AutoCompleteRepository autoCompleteRepository
    ) {
        super();
        this.autoCompleteRepository = autoCompleteRepository;
    }

    @Override
    public Observable<List<SearchData>> createObservable(RequestParams requestParams) {
        return autoCompleteRepository.getSearchData(requestParams.getParameters());
    }

    public static RequestParams getParams(String query, String registrationId, String userId) {
        Map<String, Object> searchParameter = new HashMap<>();
        searchParameter.put(SearchApiConst.Q, query);
        return getParams(searchParameter, registrationId, userId);
    }

    public static RequestParams getParams(Map<String, Object> searchParameter, String registrationId, String userId) {
        RequestParams params = RequestParams.create();

        params.putAll(searchParameter);

        params.putString(KEY_DEVICE, DEFAULT_DEVICE);
        params.putString(KEY_SOURCE, DEFAULT_SOURCE);
        params.putString(KEY_COUNT, DEFAULT_COUNT);
        String uniqueId = AuthHelper.getMD5Hash(registrationId);
        if (!TextUtils.isEmpty(userId)) {
            uniqueId = AuthHelper.getMD5Hash(userId);
            params.putString(KEY_USER_ID, userId);
        }
        params.putString(KEY_UNIQUE_ID, uniqueId);
        params.putString(DEVICE_ID, registrationId);

        return params;
    }
}
