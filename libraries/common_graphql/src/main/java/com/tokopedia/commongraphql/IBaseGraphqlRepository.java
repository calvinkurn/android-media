package com.tokopedia.commongraphql;


import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public interface IBaseGraphqlRepository {

    Observable<String> request(RequestParams requestParams);
}
