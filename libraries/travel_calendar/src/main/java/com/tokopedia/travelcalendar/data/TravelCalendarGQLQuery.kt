package com.tokopedia.travelcalendar.data

/**
 * @author by furqan on 03/09/2020
 */
object TravelCalendarGQLQuery {
    val GET_TRAVEL_CALENDAR_HOLIDAY = """
        query {
          TravelGetHoliday(input:{
            languageID:"id-id",
            countryID:"ID",
          }){
            data {
              ID,
              attribute{
                date,
                label
              }
            }
          }
        }
    """.trimIndent()
}