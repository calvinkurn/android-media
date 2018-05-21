package com.tokopedia.imagepicker.picker.instagram.data.source;

import rx.Observable;

/**
 * Created by zulfikarrahman on 5/4/18.
 */

public interface InstagramDataSource {
    Observable<String> getAccessToken(String code);
}
