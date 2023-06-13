package com.tokopedia.digital.home.util

import com.tokopedia.digital.home.util.DigitalHomeBannerQuery.HOME_BANNER_QUERY
import com.tokopedia.digital.home.util.DigitalHomeBannerQuery.HOME_BANNER_QUERY_NAME
import com.tokopedia.digital.home.util.DigitalHomeCategoryQuery.HOME_CATEGORY_QUERY
import com.tokopedia.digital.home.util.DigitalHomeCategoryQuery.HOME_CATEGORY_QUERY_NAME
import com.tokopedia.digital.home.util.DigitalHomeRechargeFavoriteRecommendationListQuery.HOME_RECHARGE_FAVORITE_RECOMMENDATION_QUERY
import com.tokopedia.digital.home.util.DigitalHomeRechargeFavoriteRecommendationListQuery.HOME_RECHARGE_FAVORITE_RECOMMENDATION_QUERY_NAME
import com.tokopedia.digital.home.util.DigitalHomeSearchAutoCompleteQuery.HOME_SEARCH_AUTO_COMPLETE_QUERY
import com.tokopedia.digital.home.util.DigitalHomeSearchAutoCompleteQuery.HOME_SEARCH_AUTO_COMPLETE_QUERY_NAME
import com.tokopedia.digital.home.util.DigitalHomeSectionQuery.HOME_SECTION_QUERY
import com.tokopedia.digital.home.util.DigitalHomeSectionQuery.HOME_SECTION_QUERY_NAME
import com.tokopedia.gql_query_annotation.GqlQuery

@GqlQuery(HOME_SECTION_QUERY_NAME, HOME_SECTION_QUERY)
internal object DigitalHomeSectionQuery{
    const val HOME_SECTION_QUERY_NAME = "QueryDigitalHomeSection"
    const val HOME_SECTION_QUERY = """
        query rechargeSubHomePageSection(${'$'}sectionType: SubHomePageSection!) {
            rechargeSubHomePageSection(sectionType:${'$'}sectionType){
            slug_name
            section {
              title
              items {
                id
                title
                media_url
                navigate_url
                app_link
                html_content
                status
              }
            }
          }
        }
    """
}

@GqlQuery(HOME_CATEGORY_QUERY_NAME, HOME_CATEGORY_QUERY)
internal object DigitalHomeCategoryQuery{
    const val HOME_CATEGORY_QUERY_NAME = "QueryDigitalHomeCategory"
    const val HOME_CATEGORY_QUERY = """
        {
          rechargeCatalogMenu(platformID:31){
            name
            label
            app_link
            sub_menu{
              id
              name
              label
              app_link
              icon
              category_ids
            }
          }
        }
    """
}

@GqlQuery(HOME_BANNER_QUERY_NAME, HOME_BANNER_QUERY)
internal object DigitalHomeBannerQuery{
    const val HOME_BANNER_QUERY_NAME = "QueryDigitalHomeBanner"
    const val HOME_BANNER_QUERY = """
        {
          rechargeBanner(platformID: 31) {
            id
            img_url
            title
            app_link
            filename
          }
        }
    """
}

@GqlQuery(HOME_SEARCH_AUTO_COMPLETE_QUERY_NAME, HOME_SEARCH_AUTO_COMPLETE_QUERY)
internal object DigitalHomeSearchAutoCompleteQuery{
    const val HOME_SEARCH_AUTO_COMPLETE_QUERY_NAME = "QueryDigitalHomeSearchAutoComplete"
    const val HOME_SEARCH_AUTO_COMPLETE_QUERY = """
        query DigitalSearchSuggestionQuery(${'$'}param: String!){
            digiPersoSearchSuggestion(param: ${'$'}param) {
                data {
                    id
                    name
                    tracking {
                        userType
                        keyword
                    }
                    items {
                        template
                        type
                        appLink
                        url
                        title
                        subtitle
                        iconTitle
                        iconSubtitle
                        shortcutImage
                        imageURL
                        urlTracker
                        discountPercentage
                        discountedPrice
                        originalPrice
                        tracking {
                            itemType
                            categoryID
                            categoryName
                            operatorID
                            operatorName
                        }
                        childItems {
                            template
                            type
                            appLink
                            url
                            title
                        }
                    }
                }
            }
        }
    """
}

@GqlQuery(HOME_RECHARGE_FAVORITE_RECOMMENDATION_QUERY_NAME, HOME_RECHARGE_FAVORITE_RECOMMENDATION_QUERY)
internal object DigitalHomeRechargeFavoriteRecommendationListQuery{
    const val HOME_RECHARGE_FAVORITE_RECOMMENDATION_QUERY_NAME = "QueryDigitalHomeRechargeFavoriteRecommendationList"
    const val HOME_RECHARGE_FAVORITE_RECOMMENDATION_QUERY = """
        query rechargeFavoriteRecommendationList(${'$'}device_id: Int!) {
        	rechargeFavoriteRecommendationList(device_id:${'$'}device_id) {
        		title
        		recommendations{
        			iconUrl
        			title
        			clientNumber
        			appLink
        			webLink
        			position
        			categoryName
        			categoryId
        			productName
        			type
        		}
        	}
        }
    """
}