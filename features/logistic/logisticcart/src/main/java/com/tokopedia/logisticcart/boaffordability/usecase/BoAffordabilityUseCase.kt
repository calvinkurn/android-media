package com.tokopedia.logisticcart.boaffordability.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.logisticcart.boaffordability.model.BoAffordabilityResponse
import com.tokopedia.logisticcart.boaffordability.model.BoAffordabilityTexts
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class BoAffordabilityUseCase @Inject constructor(@ApplicationContext private val gqlRepository: GraphqlRepository) : UseCase<BoAffordabilityResponse>() {

    @GqlQuery("BoAffordabilityQuery", QUERY)
    override suspend fun executeOnBackground(): BoAffordabilityResponse {
//        return gqlRepository.response(listOf(GraphqlRequest(BoAffordabilityQuery(), BoAffordabilityGqlResponse::class.java)))
//                .getData<BoAffordabilityGqlResponse>(BoAffordabilityGqlResponse::class.java).response

        return BoAffordabilityResponse(40_000, BoAffordabilityTexts(
                "asdfasd fas <s>dfas</s> df", "asdfasd fas <s>dfas</s> df"
        ))
    }

    companion object {
        const val QUERY = """
            query freeShipping (input: {
    Origin: "2259|10640|-6.169976,106.86344699999995"
    Destination: "2259|10640|-6.169976,106.86344699999995"
    Source: "cartapp"
    Type: "ios"
    OsType: 2;
    DeviceVersion: "2.47"; 
    Weight: 1;
    ActualWeight: 9;
    OrderValue: 10;
    SpIDs: "1,6,12,18,22,24,26,33,37,45,999";
    ShopID: 5241637;
    UserHistory: 0;
    IsFulfillment: true;
    BOMetadata: "{\"bo_metadata\":{\"bo_type\":3,\"bo_eligibilities\":[{\"key\":\"is_tokonow\",\"value\":\"true\"}]}}";
    Products: "[{"product_id": 123, "is_free_shipping": true, "is_free_shipping_tc": true}, {"product_id": 234, "is_free_shipping" :false, "is_free_shipping_tc": true}]";
    PSLCode: "ONGKIRGRAB";
    UniqueID: "6550386-0-19549-4753178";
  }) {
    freeShipping {
      IsPromo
      PromoCode
      ShipperProductID
      ImageURL
      DiscountedRate
      ShippingRate
      BenefitAmount
      MinTransaction
      COD {
        IsCODAvailable
        CODPrice
      },
      BOType
      BenefitClass
      BOWeight {
        IsBOWeight
        ShippingSubsidy
      },
      Texts {
        TickerCart
        TickerProgressive
      }
    }
  }
        """
    }
}