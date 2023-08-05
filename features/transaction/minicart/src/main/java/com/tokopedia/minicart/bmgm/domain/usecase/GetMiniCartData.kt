package com.tokopedia.minicart.bmgm.domain.usecase

import com.tokopedia.minicart.bmgm.presentation.model.BaseMiniCartUiModel
import com.tokopedia.minicart.bmgm.presentation.model.MiniCartDataUiModel
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 03/08/23.
 */

class GetMiniCartData @Inject constructor() {

    operator suspend fun invoke(): MiniCartDataUiModel {
        return MiniCartDataUiModel(getItemList())
    }

    private suspend fun getItemList(): List<BaseMiniCartUiModel> {
        return listOf(

        )
    }
}