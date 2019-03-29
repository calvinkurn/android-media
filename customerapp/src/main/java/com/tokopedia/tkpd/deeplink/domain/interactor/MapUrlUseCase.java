package com.tokopedia.tkpd.deeplink.domain.interactor;

import android.text.TextUtils;

import com.tokopedia.tkpd.deeplink.WhitelistItem;
import com.tokopedia.tkpd.deeplink.data.repository.DeeplinkRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Rizky on 04/01/18.
 */

public class MapUrlUseCase extends UseCase<WhitelistItem> {

    private final String KEY_FINAL_SEGMENTS = "KEY_FINAL_SEGMENTS";

    private DeeplinkRepository deeplinkRepository;

    public MapUrlUseCase(DeeplinkRepository deeplinkRepository) {
        this.deeplinkRepository = deeplinkRepository;
    }

    @Override
    public Observable<WhitelistItem> createObservable(final RequestParams requestParams) {
        return deeplinkRepository.mapUrl()
                .flatMapIterable((Func1<List<WhitelistItem>, Iterable<WhitelistItem>>) whitelistItems -> whitelistItems)
                .firstOrDefault(new WhitelistItem(null, null), whitelistItem -> {
                    String applink = findApplink(whitelistItem,
                            requestParams.getString(KEY_FINAL_SEGMENTS, ""));
                    return !TextUtils.isEmpty(applink);
                });
    }

    private String findApplink(WhitelistItem whitelistItem, String finalSegments) {
        if (whitelistItem != null) {
            if (!finalSegments.isEmpty()) {
                if (whitelistItem.path.equals(finalSegments)) {
                    return whitelistItem.applink;
                }
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
