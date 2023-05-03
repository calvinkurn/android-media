package com.tokopedia.play.domain

import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject


/**
 * Created by mzennis on 2020-03-05.
 */
class GetCartCountUseCase @Inject constructor(
    private val updateCartCounterUseCase: UpdateCartCounterUseCase
) : UseCase<Int>() {

    override suspend fun executeOnBackground(): Int {
       return updateCartCounterUseCase
               .createObservable(RequestParams.EMPTY)
               .toBlocking()
               .single()
    }
}
