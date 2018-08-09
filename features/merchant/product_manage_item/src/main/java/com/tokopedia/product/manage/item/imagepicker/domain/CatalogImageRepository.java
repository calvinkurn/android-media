package com.tokopedia.product.manage.item.imagepicker.domain;

import com.tokopedia.product.manage.item.imagepicker.data.model.CatalogImage;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 6/5/18.
 */

public interface CatalogImageRepository {
    Observable<List<CatalogImage>> getCatalogImage(String catalogId);
}
