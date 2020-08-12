package com.tokopedia.favorite.domain.interactor

import com.tokopedia.favorite.domain.model.DataFavorite
import com.tokopedia.usecase.coroutines.UseCase

class GetInitialDataPageUseCaseWithCoroutine constructor(

): UseCase<DataFavorite>() {

    override suspend fun executeOnBackground(): DataFavorite {
        TODO("Not yet implemented")
    }

}