package com.tokopedia.product.addedit.imagepicker.domain;

import com.tokopedia.product.addedit.imagepicker.data.model.CatalogImage;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 6/5/18.
 */

public interface CatalogImageRepository {
    Observable<List<CatalogImage>> getCatalogImage(String catalogId);
}
