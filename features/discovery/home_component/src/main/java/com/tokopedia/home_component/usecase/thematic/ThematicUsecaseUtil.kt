package com.tokopedia.home_component.usecase.thematic

object ThematicUsecaseUtil {
    const val THEMATIC_QUERY_NAME = "ThematicQuery"
    const val THEMATIC_QUERY: String =
        """query getHomeThematic(${'$'}page: String!) {
              getHomeThematic (page: ${'$'}page) {
                thematic {
                  isShown
                  colorMode
                  heightPercentage
                  backgroundImageURL
                  foregroundImageURL
                  foregroundImageWidthRatio
                  foregroundImageHeightRatio
                }
              }
            }
        """

    /**
     * Used for request param and variable
     */
    const val THEMATIC_PARAM = "page"

    const val THEMATIC_DI_NAME = "ThematicUseCase"

    const val THEMATIC_PAGE_HOME = "home"
    const val THEMATIC_PAGE_SRP = "srp"
}
