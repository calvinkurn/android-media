package com.tokopedia.tkpd.home.feed.data.source;

import java.util.List;

import rx.Observable;

/**
 * @author Kulomady on 12/9/16.
 */

public abstract class FavoritShopIdDataSource {

    public abstract Observable<List<String>> getListShopId();

}
