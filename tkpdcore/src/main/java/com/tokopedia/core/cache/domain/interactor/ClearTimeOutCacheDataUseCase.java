package com.tokopedia.core.cache.domain.interactor;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.cache.data.source.db.CacheApiData;
import com.tokopedia.core.cache.data.source.db.CacheApiData_Table;

import java.util.List;

import rx.Observable;

/**
 * Created by normansyahputa on 8/30/17.
 */

public class ClearTimeOutCacheDataUseCase extends UseCase<Boolean> {
    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        long currentTime = System.currentTimeMillis() / 1000L;
        List<CacheApiData> cacheApiDatas = new Select().from(CacheApiData.class).where(CacheApiData_Table.expiredDate.lessThan(currentTime)).queryList();
        if (cacheApiDatas != null) {
            for (int i = 0; i < cacheApiDatas.size(); i++) {
                cacheApiDatas.get(i).delete();
            }
        }

        return Observable.just(true);
    }
}
