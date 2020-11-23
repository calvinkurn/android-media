package com.tokopedia.sellerorder.list.domain.usecases

import com.tokopedia.usecase.RequestParams

abstract class BaseGraphqlUseCase {
    protected var params: RequestParams = RequestParams.EMPTY
}