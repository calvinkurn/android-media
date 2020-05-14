package com.tokopedia.interest_pick_common.domain.query

/**
 * @author by yoasfs on 2019-11-05
 */
object GetInterestPickQuery {
    fun getQuery(): String {

        val source = "\$source"
        return  """
            query onboardingInterests($source: String) {
              feedUserOnboardingInterests(source: $source) {
                meta {
                  assets {
                    titleIntro
                    titleFull
                    instruction
                    buttonCta
                  }
                  isEnabled
                  minPicked
                  source
                }
                data {
                  id
                  name
                  image
                  isSelected
                }
              }
            }
        """.trimIndent()
    }

    const val QUERY_GET_INTEREST_PICK = "QUERY_GET_INTEREST_PICK"
}