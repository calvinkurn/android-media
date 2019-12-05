package com.tokopedia.product.manage.list.view.mapper

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
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
        val featuredProductHeader = dataModel?.goldManageFeaturedProductV2?.header
        if (featuredProductHeader != null) {
            val featuredProductResponseDomainModel = FeaturedProductResponseDomainModel(
                    featuredProductHeader.errorCode,
                    featuredProductHeader.message
            )
            if (featuredProductResponseDomainModel.errorCode != "") {
                val message = featuredProductResponseDomainModel.message[0]
                throw MessageErrorException(message)
            } else return featuredProductResponseDomainModel
        } else throw MessageErrorException(null)
    }
}