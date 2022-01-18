package com.tokopedia.pdp.fintech.domain.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.pdp.fintech.constants.GQL_GET_WIDGET_DETAIL_V2
import com.tokopedia.pdp.fintech.domain.datamodel.WidgetDetail
import javax.inject.Inject


@GqlQuery("PayLaterGetPdpWidget", GQL_GET_WIDGET_DETAIL_V2)
class FintechWidgetUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<WidgetDetail>(graphqlRepository) {

    fun getWidgetData(
        onSuccess: (WidgetDetail) -> Unit,
        onError: (Throwable) -> Unit,
        productCategory: String,
        listofAmount: List<Double>,
    ) {
        try {
            this.setTypeClass(WidgetDetail::class.java)
            this.setRequestParams(getRequestParams(productCategory, listofAmount))
            this.setGraphqlQuery(PayLaterGetPdpWidget.GQL_QUERY)
            this.execute(
                { result ->
                    onSuccess(result)
                }, { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }


    private fun getRequestParams(
        productCategory: String, listofAmount: List<Double>
    ): MutableMap<String, Any?> {

        var listOfVariantDetail: MutableList<WidgetRequestModel> = setAmountList(listofAmount)

        return mutableMapOf("request" to setProductDetailMap(productCategory, listOfVariantDetail))
    }

    private fun setAmountList(listofAmount: List<Double>): MutableList<WidgetRequestModel> {
        var listOfVariantDetail: MutableList<WidgetRequestModel> = ArrayList()
        for (i in listofAmount.indices) {
            listOfVariantDetail.add(WidgetRequestModel(amount = listofAmount[i],"" ))
        }
        return listOfVariantDetail
    }


    private fun setProductDetailMap(
        productCategory: String,
        listOfVariantDetail: List<WidgetRequestModel>
    ): MutableMap<String, Any> {
        val detailMap = mutableMapOf<String, Any>()
        detailMap[PARAM_PRODUCT_CATEGORY] = productCategory
        detailMap[PARAM_LIST_PRODUCT_DETAIL] = listOfVariantDetail
        return detailMap
    }


    companion object {
        const val PARAM_PRODUCT_CATEGORY = "product_category"
        const val PARAM_LIST_PRODUCT_DETAIL = "list"

    }


}


data class WidgetRequestModel(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("redirect_url")
    val redirectionUrl: String?
)