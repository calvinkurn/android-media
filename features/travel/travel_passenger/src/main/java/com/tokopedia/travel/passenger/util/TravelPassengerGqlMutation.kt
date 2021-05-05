package com.tokopedia.travel.passenger.util

object TravelPassengerGqlMutation {
    val UPSERT_CONTACT = """
        mutation travelUpsertContact (${'$'}data: UpsertContactArgs!) {
            travelUpsertContact(input:${'$'}data){
                Success
            }
        }
    """.trimIndent()
}