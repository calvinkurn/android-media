package com.tokopedia.discovery.autocomplete.usecase;

import android.text.TextUtils;

import com.tokopedia.discovery.autocomplete.repository.AutoCompleteRepository;
import com.tokopedia.discovery.search.domain.model.SearchData;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

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
        return getParams(query, false, registrationId, userId);
    }

    public static RequestParams getParams(String query, boolean isOfficial, String registrationId, String userId) {
        RequestParams params = RequestParams.create();
        params.putString(KEY_DEVICE, DEFAULT_DEVICE);
        params.putString(KEY_SOURCE, DEFAULT_SOURCE);
        params.putString(KEY_COUNT, DEFAULT_COUNT);
        String uniqueId = AuthUtil.md5(registrationId);
        if (!TextUtils.isEmpty(userId)) {
            uniqueId = AuthUtil.md5(userId);
            params.putString(KEY_USER_ID, userId);
        }
        params.putString(KEY_UNIQUE_ID, uniqueId);
        params.putString(DEVICE_ID, registrationId);
        params.putString(KEY_QUERY, (query.isEmpty() ? "" : query));
        params.putBoolean(KEY_IS_OFFICIAL, isOfficial);
        return params;
    }
}
