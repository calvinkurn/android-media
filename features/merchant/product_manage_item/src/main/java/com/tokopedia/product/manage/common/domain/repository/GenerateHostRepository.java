package com.tokopedia.product.edit.common.domain.repository;

import com.tokopedia.product.edit.common.domain.model.GenerateHostDomainModel;
import com.tokopedia.product.edit.common.domain.model.GenerateHostDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/18/17.
 */

public interface GenerateHostRepository {
    Observable<GenerateHostDomainModel> generateHost();
}
