package com.tokopedia.dropoff.data.query

object AutoCompleteQuery {
    val keroAutoCompleteGeocode = """
        query KeroMapsAutoComplete(${'$'}param: String!) {
          kero_maps_autocomplete(input: ${'$'}param) {
            data {
              predictions {
                description
                place_id
                types
                matched_substrings {
                  length
                  offset
                }
                terms {
                  value
                  offset
                }
                structured_formatting {
                  main_text
                  main_text_matched_substrings {
                    length
                    offset
                  }
                  secondary_text
                }
              }
            }
          }
        }
        """.trimIndent()
}