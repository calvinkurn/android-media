package com.tokopedia.buy_more_get_more.minicart.domain.usecase

import com.tokopedia.buy_more_get_more.common.Const
import com.tokopedia.buy_more_get_more.minicart.domain.mapper.BmgmMiniCartDataMapper
import com.tokopedia.buy_more_get_more.minicart.domain.model.MiniCartParam
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartDataUiModel
import com.tokopedia.minicart.domain.GetMiniCartParam
import com.tokopedia.minicart.domain.GetMiniCartWidgetUseCase
import dagger.Lazy
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 04/12/23.
 */

class GetMiniCartUseCase @Inject constructor(
    private val getMiniCartWidgetUseCase: Lazy<GetMiniCartWidgetUseCase>,
    private val mapper: Lazy<BmgmMiniCartDataMapper>
) {

    suspend operator fun invoke(param: MiniCartParam): BmgmMiniCartDataUiModel {
        val response = getMiniCartWidgetUseCase.get().invoke(createMiniCartParams(param))
        return mapper.get().mapToUiModel(response)
    }

    private fun createMiniCartParams(param: MiniCartParam): GetMiniCartParam {
        return GetMiniCartParam(
            shopIds = param.shopIds.map { it.toString() },
            source = Const.MINI_CART_SOURCE,
            bmgm = GetMiniCartParam.GetMiniCartBmgmParam(
                offerIds = param.offerIds,
                warehouseIds = param.warehouseIds,
                offerJsonData = param.offerJsonData
            )
        )
    }
}