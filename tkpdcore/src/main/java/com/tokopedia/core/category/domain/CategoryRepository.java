package com.tokopedia.core.category.domain;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */
@Deprecated
public interface CategoryRepository {
    Observable<Boolean> checkVersion();
}
