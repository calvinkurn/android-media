package com.tokopedia.product.manage.list.view.mapper

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.product.manage.list.data.model.featuredproductresponse.FeaturedProductResponseDomainModel
import com.tokopedia.product.manage.list.data.model.featuredproductresponse.FeaturedProductResponseModel
import rx.functions.Func1
import javax.inject.Inject

class FeaturedProductResponseMapper @Inject constructor() : Func1<GraphqlResponse, FeaturedProductResponseDomainModel?> {

    override fun call(graphqlResponse: GraphqlResponse?): FeaturedProductResponseDomainModel? {
        val dataModel = graphqlResponse?.getData<FeaturedProductResponseModel>(FeaturedProductResponseModel::class.java)
        return mapResponseDataToDomainData(dataModel)
    }

    private fun mapResponseDataToDomainData(dataModel: FeaturedProductResponseModel?): FeaturedProductResponseDomainModel? {
        var featuredProductResponseDomainModel: FeaturedProductResponseDomainModel? = null
        val featuredProductHeader = dataModel?.goldManageFeaturedProductV2?.header
        if (featuredProductHeader != null) {
            featuredProductResponseDomainModel = FeaturedProductResponseDomainModel(
                    featuredProductHeader.processTime,
                    featuredProductHeader.reason,
                    featuredProductHeader.errorCode,
                    featuredProductHeader.message
            )
        }
        return featuredProductResponseDomainModel
    }
}