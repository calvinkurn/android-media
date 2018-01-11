package com.tokopedia.tkpd.deeplink.domain.interactor;

import android.text.TextUtils;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.deeplink.WhitelistItem;
import com.tokopedia.tkpd.deeplink.data.repository.DeeplinkRepository;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Rizky on 04/01/18.
 */

public class MapUrlUseCase extends UseCase<WhitelistItem> {

    private final String KEY_FINAL_SEGMENTS = "KEY_FINAL_SEGMENTS";

    private DeeplinkRepository deeplinkRepository;

    public MapUrlUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, DeeplinkRepository deeplinkRepository) {
        super(threadExecutor, postExecutionThread);
        this.deeplinkRepository = deeplinkRepository;
    }

    @Override
    public Observable<WhitelistItem> createObservable(final RequestParams requestParams) {
        return deeplinkRepository.mapUrl()
                .flatMapIterable(new Func1<List<WhitelistItem>, Iterable<WhitelistItem>>() {
                    @Override
                    public Iterable<WhitelistItem> call(List<WhitelistItem> whitelistItems) {
                        return whitelistItems;
                    }
                })
                .firstOrDefault(new WhitelistItem(null, null), new Func1<WhitelistItem, Boolean>() {
                    @Override
                    public Boolean call(WhitelistItem whitelistItem) {
                        String applink = findApplink(whitelistItem, requestParams.getString(KEY_FINAL_SEGMENTS, ""));
                        return !TextUtils.isEmpty(applink);
                    }
                });
    }

    private String findApplink(WhitelistItem whitelistItem, String finalSegments) {
        if (whitelistItem != null) {
            if (whitelistItem.path.equals(finalSegments)) {
                return whitelistItem.applink;
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
