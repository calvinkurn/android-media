package com.tokopedia.home.beranda.data.repository;

import com.tokopedia.core.base.adapter.Visitable;

import java.util.List;

import rx.Observable;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public interface HomeRepository {

    Observable<List<Visitable>> getAllHomeData();

    Observable<List<Visitable>> getHomeDataCache();

}
