package com.tokopedia.discovery.autocomplete.usecase;

import android.text.TextUtils;

import com.tokopedia.discovery.autocomplete.repository.AutoCompleteRepository;
import com.tokopedia.discovery.search.domain.model.SearchData;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

public class DeleteRecentSearchUseCase extends UseCase<List<SearchData>> {

    public static final String KEY_UNIQUE_ID = "unique_id";
    public static final String KEY_USER_ID = "user_id";
    private static final String DEVICE_ID = "device_id";
    public static final String UNIQUE_ID = "unique_id";
    public static final String KEY_Q = "q";
    public static final String KEY_DELETE_ALL = "clear_all";

    public static final String DEFAULT_DELETE_ALL = "false";
    private static final String KEY_DEVICE = "device";
    private static final String KEY_SOURCE = "source";

    private final AutoCompleteRepository autoCompleteRepository;
    private final AutoCompleteUseCase autoCompleteUseCase;

    public DeleteRecentSearchUseCase(
            AutoCompleteRepository autoCompleteRepository,
            AutoCompleteUseCase autoCompleteUseCase
    ) {
        super();
        this.autoCompleteRepository = autoCompleteRepository;
        this.autoCompleteUseCase = autoCompleteUseCase;
    }

    @Override
    public Observable<List<SearchData>> createObservable(final RequestParams requestParams) {
        return autoCompleteRepository.deleteRecentSearch(requestParams.getParameters())
                .flatMap(new Func1<Response<Void>, Observable<List<SearchData>>>() {
                    @Override
                    public Observable<List<SearchData>> call(Response<Void> voidResponse) {
                        RequestParams params = AutoCompleteUseCase.getParams(
                                "",
                                requestParams.getString(DEVICE_ID, ""),
                                requestParams.getString(KEY_USER_ID, "")
                        );
                        return autoCompleteUseCase.createObservable(params);
                    }
                });
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        autoCompleteUseCase.unsubscribe();
    }

    public static RequestParams getParams(String query, String registrationId, String userId) {
        RequestParams params = RequestParams.create();
        params.putString(KEY_DEVICE, "android");
        params.putString(KEY_SOURCE, "searchbar");
        params.putString(KEY_DELETE_ALL, "false");
        params.putString(KEY_Q, query);
        String unique_id = AuthUtil.md5(registrationId);
        if (!TextUtils.isEmpty(userId)) {
            unique_id = AuthUtil.md5(userId);
            params.putString(KEY_USER_ID, userId);
        }
        params.putString(KEY_UNIQUE_ID, unique_id);
        params.putString(DEVICE_ID, registrationId);
        return params;
    }

    public static RequestParams getParams(String registrationId, String userId) {
        RequestParams params = RequestParams.create();
        params.putString(KEY_DEVICE, "android");
        params.putString(KEY_SOURCE, "searchbar");
        params.putString(KEY_DELETE_ALL, "true");
        String unique_id = AuthUtil.md5(registrationId);
        if (!TextUtils.isEmpty(userId)) {
            unique_id = AuthUtil.md5(userId);
            params.putString(KEY_USER_ID, userId);
        }
        params.putString(KEY_UNIQUE_ID, unique_id);
        params.putString(DEVICE_ID, registrationId);
        return params;
    }
}