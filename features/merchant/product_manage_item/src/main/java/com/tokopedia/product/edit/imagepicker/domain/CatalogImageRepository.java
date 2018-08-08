package com.tokopedia.product.edit.imagepicker.domain;

import com.tokopedia.product.edit.imagepicker.data.model.CatalogImage;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 6/5/18.
 */

public interface CatalogImageRepository {
    Observable<List<CatalogImage>> getCatalogImage(String catalogId);
}
