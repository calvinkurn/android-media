package com.tokopedia.interestpick.domain.query

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
}