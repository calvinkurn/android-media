package com.tokopedia.discovery.autocomplete.usecase;

import android.text.TextUtils;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.discovery.autocomplete.repository.AutoCompleteRepository;
import com.tokopedia.discovery.search.domain.model.SearchData;

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

    private final AutoCompleteRepository autoCompleteRepository;

    public AutoCompleteUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            AutoCompleteRepository autoCompleteRepository
    ) {
        super(threadExecutor, postExecutionThread);
        this.autoCompleteRepository = autoCompleteRepository;
    }

    @Override
    public Observable<List<SearchData>> createObservable(RequestParams requestParams) {
        return autoCompleteRepository.getSearchData(requestParams.getParameters());
    }

    public static RequestParams getParams(String query, String registrationId, String userId) {
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
        return params;
    }
}
