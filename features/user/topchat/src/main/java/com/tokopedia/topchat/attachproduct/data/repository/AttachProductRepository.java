package com.tokopedia.topchat.attachproduct.data.repository;

import com.tokopedia.topchat.attachproduct.domain.model.AttachProductDomainModel;

import java.util.Map;

import rx.Observable;

/**
 * Created by Hendri on 13/02/18.
 */

public interface AttachProductRepository {
    Observable<AttachProductDomainModel> loadProductFromShop(Map<String, String> params);
}