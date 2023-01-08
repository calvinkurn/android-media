package com.tokopedia.travel.passenger.util

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.travel.passenger.util.TravelPassengerGqlMutation.UPSERT_CONTACT

@GqlQuery("MutationUpsertContact", UPSERT_CONTACT)
object TravelPassengerGqlMutation {
    const val UPSERT_CONTACT = """
        mutation travelUpsertContact (${'$'}data: UpsertContactArgs!) {
            travelUpsertContact(input:${'$'}data){
                Success
            }
        }
    """
}