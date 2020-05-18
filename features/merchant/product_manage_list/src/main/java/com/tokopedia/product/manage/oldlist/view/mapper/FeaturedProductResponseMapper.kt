package com.tokopedia.product.manage.oldlist.view.mapper

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.manage.oldlist.data.model.featuredproductresponse.FeaturedProductResponseModel
import rx.functions.Func1
import javax.inject.Inject

class FeaturedProductResponseMapper @Inject constructor() : Func1<GraphqlResponse, Unit> {

    override fun call(graphqlResponse: GraphqlResponse?) : Unit {
        val dataModel = graphqlResponse?.getData<FeaturedProductResponseModel>(FeaturedProductResponseModel::class.java)
        return mapResponseDataToDomainData(dataModel)
    }

    /**
     * Return Unit? as we actually don't need to pass model data value back to presenter
     */
    private fun mapResponseDataToDomainData(dataModel: FeaturedProductResponseModel?) : Unit {
        val featuredProductHeader = dataModel?.goldManageFeaturedProductV2?.header
        if (featuredProductHeader != null) {
            if (featuredProductHeader.errorCode.isNotEmpty()) {
                val message = featuredProductHeader.message[0]
                throw MessageErrorException(message)
            } else return Unit
        } else throw MessageErrorException(null)
    }
}