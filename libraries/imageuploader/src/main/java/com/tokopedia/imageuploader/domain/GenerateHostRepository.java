package com.tokopedia.imageuploader.domain;

import com.tokopedia.imageuploader.domain.model.GenerateHostDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/18/17.
 */

public interface GenerateHostRepository {
    Observable<GenerateHostDomainModel> generateHost();
}
