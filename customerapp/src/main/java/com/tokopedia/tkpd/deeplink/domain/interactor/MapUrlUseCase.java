package com.tokopedia.tkpd.deeplink.domain.interactor;

import android.text.TextUtils;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.deeplink.Deeplink;
import com.tokopedia.tkpd.deeplink.data.repository.DeeplinkRepository;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Rizky on 04/01/18.
 */

public class MapUrlUseCase extends UseCase<Deeplink> {

    private final String KEY_FINAL_SEGMENTS = "KEY_FINAL_SEGMENTS";

    private DeeplinkRepository deeplinkRepository;

    public MapUrlUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, DeeplinkRepository deeplinkRepository) {
        super(threadExecutor, postExecutionThread);
        this.deeplinkRepository = deeplinkRepository;
    }

    @Override
    public Observable<Deeplink> createObservable(final RequestParams requestParams) {
        return deeplinkRepository.mapUrl()
                .flatMapIterable(new Func1<List<Deeplink>, Iterable<Deeplink>>() {
                    @Override
                    public Iterable<Deeplink> call(List<Deeplink> deeplinks) {
                        return deeplinks;
                    }
                })
                .firstOrDefault(new Deeplink(null, null), new Func1<Deeplink, Boolean>() {
                    @Override
                    public Boolean call(Deeplink deeplink) {
                        String applink = findApplink(deeplink, requestParams.getString(KEY_FINAL_SEGMENTS, ""));
                        return !TextUtils.isEmpty(applink);
                    }
                });
    }

    private String findApplink(Deeplink deeplink, String finalSegments) {
        if (deeplink != null) {
            if (deeplink.path.equals(finalSegments)) {
                return deeplink.applink;
            }
        }
        return null;
    }

    public RequestParams setRequestParam(String finalSegments) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(KEY_FINAL_SEGMENTS, finalSegments);
        return requestParams;
    }

}
