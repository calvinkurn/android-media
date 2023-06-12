package com.tokopedia.tokopedianow.category.domain.usecase

import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_DEVICE
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_SORT
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEVICE
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.NAVSOURCE
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.OB
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.PAGE
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.ROWS
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.SOURCE
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.SRP_PAGE_ID
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.UNIQUE_ID
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_ADDRESS_ID
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_CITY_ID
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_DISTRICT_ID
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_LAT
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_LONG
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_POST_CODE
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_WAREHOUSE_ID
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USE_PAGE
import com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.category.domain.query.AceSearchProduct
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetCategoryProductUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) {
    companion object {
        const val USE_PAGE_VALUE = "true"
        const val SOURCE_VALUE = "category_tokonow_directory"
        const val PAGE_VALUE = "1"
        const val ROWS_VALUE = "7"
    }

    private val graphql by lazy { GraphqlUseCase<AceSearchProductModel>(graphqlRepository) }

    init {
        graphql.apply {
            setGraphqlQuery(AceSearchProduct)
            setTypeClass(AceSearchProductModel::class.java)
        }
    }

    suspend fun execute(
        chooseAddressData: LocalCacheModel,
        uniqueId: String,
        categoryIdL2: String
    ): AceSearchProductModel {
        return graphql.run {
            val queryParams = mutableMapOf<String?, Any>()

            if (chooseAddressData.city_id.isNotEmpty())
                queryParams[USER_CITY_ID] = chooseAddressData.city_id
            if (chooseAddressData.address_id.isNotEmpty())
                queryParams[USER_ADDRESS_ID] = chooseAddressData.address_id
            if (chooseAddressData.district_id.isNotEmpty())
                queryParams[USER_DISTRICT_ID] = chooseAddressData.district_id
            if (chooseAddressData.lat.isNotEmpty())
                queryParams[USER_LAT] = chooseAddressData.lat
            if (chooseAddressData.long.isNotEmpty())
                queryParams[USER_LONG] = chooseAddressData.long
            if (chooseAddressData.postal_code.isNotEmpty())
                queryParams[USER_POST_CODE] = chooseAddressData.postal_code
            if (chooseAddressData.warehouse_id.isNotEmpty())
                queryParams[USER_WAREHOUSE_ID] = chooseAddressData.warehouse_id

            queryParams[DEVICE] = DEFAULT_VALUE_OF_PARAMETER_DEVICE
            queryParams[UNIQUE_ID] = uniqueId
            queryParams[PAGE] = PAGE_VALUE
            queryParams[USE_PAGE] = USE_PAGE_VALUE
            queryParams[OB] = DEFAULT_VALUE_OF_PARAMETER_SORT
            queryParams[NAVSOURCE] = SOURCE_VALUE
            queryParams[SOURCE] = SOURCE_VALUE
            queryParams[SRP_PAGE_ID] = categoryIdL2
            queryParams[ROWS] = ROWS_VALUE

            setRequestParams(
                RequestParams.create().apply {
                    putString(KEY_PARAMS, UrlParamUtils.generateUrlParamString(queryParams))
                }.parameters
            )

            executeOnBackground()
        }
    }
}
