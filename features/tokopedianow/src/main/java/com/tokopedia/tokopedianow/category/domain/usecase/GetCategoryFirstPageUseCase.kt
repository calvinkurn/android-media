package com.tokopedia.tokopedianow.category.domain.usecase

import android.util.Log
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_DEVICE
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEVICE
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.NAVSOURCE
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.OB
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.PAGE
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
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.category.domain.query.AceSearchProduct
import com.tokopedia.tokopedianow.category.domain.response.CategoryModel
import com.tokopedia.tokopedianow.searchcategory.data.getTokonowQueryParam
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.usecase.coroutines.UseCase

class GetCategoryFirstPageUseCase(
    private val graphqlUseCase: MultiRequestGraphqlUseCase,
): UseCase<CategoryModel>() {

    override suspend fun executeOnBackground(): CategoryModel {
        val params = useCaseRequestParams.parameters[KEY_PARAMS] ?: String.EMPTY

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(createAceSearchProductRequest(params))
        val response = graphqlUseCase.executeOnBackground()
        Log.d("HELLO_GUYS_INI", getSearchProduct(response).toString())

        return CategoryModel(
            searchProduct = getSearchProduct(response)
        )
    }

    private fun getSearchProduct(
        graphqlResponse: GraphqlResponse
    ): AceSearchProductModel.SearchProduct = graphqlResponse.getData<AceSearchProductModel?>(AceSearchProductModel::class.java)?.searchProduct ?: AceSearchProductModel.SearchProduct()

    private fun createAceSearchProductRequest(
        params: Any
    ) = GraphqlRequest(
        AceSearchProduct.getQuery(),
        typeOfT = AceSearchProductModel::class.java,
        variables = mapOf(
            KEY_PARAMS to params
        )
    )

    fun setParams(
        chooseAddressData: LocalCacheModel,
        uniqueId: String,
        categoryIdL1: String
    ) {
        useCaseRequestParams.apply {
            val queryParams = mutableMapOf<String?, Any>()

            queryParams[DEVICE] = DEFAULT_VALUE_OF_PARAMETER_DEVICE

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

            queryParams[UNIQUE_ID] = uniqueId
            queryParams[PAGE] = "1"
            queryParams[USE_PAGE] = "true"
            queryParams[OB] = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT
            queryParams[NAVSOURCE] = "category_tokonow_directory"
            queryParams[SOURCE] = "category_tokonow_directory"
            queryParams[SRP_PAGE_ID] = categoryIdL1

            useCaseRequestParams.parameters[KEY_PARAMS] = UrlParamUtils.generateUrlParamString(queryParams)
        }
    }

}
