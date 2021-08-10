package com.tokopedia.statistic.domain.usecase

import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 21/06/21
 */

abstract class BaseStatisticUseCase<T: Any> {

    abstract suspend fun execute(params: RequestParams): T
}