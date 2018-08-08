package com.tokopedia.product.manage.item.common.domain.repository;

import com.tokopedia.product.manage.item.common.domain.model.GenerateHostDomainModel;
import com.tokopedia.product.manage.item.common.domain.model.GenerateHostDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/18/17.
 */

public interface GenerateHostRepository {
    Observable<GenerateHostDomainModel> generateHost();
}
