package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.sellerhome.view.model.TrendLineViewUiModel
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created By @ilhamsuaib on 2020-01-15
 */

class GetSellerHomeLayoutUseCase(
        multiRequestGraphqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<List<TrendLineViewUiModel>>() {

    override suspend fun executeOnBackground(): List<TrendLineViewUiModel> {
        //handle request here
        return listOf(
                TrendLineViewUiModel("Hello"),
                TrendLineViewUiModel("World!")
        )
    }
}