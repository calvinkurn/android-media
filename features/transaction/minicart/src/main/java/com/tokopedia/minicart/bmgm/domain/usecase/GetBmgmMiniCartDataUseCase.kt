package com.tokopedia.minicart.bmgm.domain.usecase

import android.annotation.SuppressLint
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.toLongSafely
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper.Companion.KEY_CHOSEN_ADDRESS
import com.tokopedia.minicart.bmgm.domain.gqlquery.GetBmgmMiniCartDataQuery
import com.tokopedia.minicart.bmgm.domain.mapper.BmgmMiniCartDataMapper
import com.tokopedia.minicart.bmgm.domain.model.BmgmParamModel
import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartDataUiModel
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 03/08/23.
 */

class GetBmgmMiniCartDataUseCase @Inject constructor(
    private val mapper: BmgmMiniCartDataMapper,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper,
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<MiniCartData>(graphqlRepository) {

    init {
        setGraphqlQuery(GetBmgmMiniCartDataQuery())
        setTypeClass(MiniCartData::class.java)
    }

    suspend operator fun invoke(
        shopId: String,
        bmgmParam: BmgmParamModel
    ): BmgmMiniCartDataUiModel {
        try {
            val requestParam = createRequestParam(shopId, bmgmParam)
            setRequestParams(requestParam.parameters)
            val response = executeOnBackground()
            return mapper.mapToUiModel(response)
        } catch (e: Exception) {
            throw RuntimeException(e.message)
        }
    }

    @SuppressLint("PII Data Exposure")
    private fun createRequestParam(shopId: String, bmgmParam: BmgmParamModel): RequestParams {
        return RequestParams.create().apply {
            putString(PARAM_KEY_LANG, PARAM_VALUE_ID)
            putObject(
                PARAM_KEY_ADDITIONAL, mapOf(
                    PARAM_KEY_SHOP_IDS to listOf(shopId.toLongSafely()),
                    KEY_CHOSEN_ADDRESS to chosenAddressRequestHelper.getChosenAddress(),
                    PARAM_KEY_SOURCE to PARAM_VALUE_SOURCE,
                    PARAM_KEY_BMGM to bmgmParam
                )
            )
        }
    }

    companion object {
        private const val PARAM_KEY_LANG = "lang"
        private const val PARAM_KEY_ADDITIONAL = "additional_params"
        private const val PARAM_KEY_SHOP_IDS = "shop_ids"
        private const val PARAM_KEY_SOURCE = "source"
        private const val PARAM_KEY_BMGM = "bmgm"

        private const val PARAM_VALUE_ID = "id"
        private const val PARAM_VALUE_SOURCE = "bmgm_olp_mini_cart"
    }
}