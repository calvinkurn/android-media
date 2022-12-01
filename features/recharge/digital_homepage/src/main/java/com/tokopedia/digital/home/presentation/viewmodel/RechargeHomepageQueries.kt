package com.tokopedia.digital.home.presentation.viewmodel

import com.tokopedia.digital.home.presentation.viewmodel.RechargeHomepageActionQuery.ACTION_QUERY
import com.tokopedia.digital.home.presentation.viewmodel.RechargeHomepageActionQuery.ACTION_QUERY_NAME
import com.tokopedia.digital.home.presentation.viewmodel.RechargeHomepageSectionQuery.SECTION_QUERY
import com.tokopedia.digital.home.presentation.viewmodel.RechargeHomepageSectionQuery.SECTION_QUERY_NAME
import com.tokopedia.digital.home.presentation.viewmodel.RechargeHomepageSkeletonQuery.SKELETON_QUERY
import com.tokopedia.digital.home.presentation.viewmodel.RechargeHomepageSkeletonQuery.SKELETON_QUERY_NAME
import com.tokopedia.digital.home.presentation.viewmodel.RechargeHomepageTickerQuery.TICKER_QUERY
import com.tokopedia.digital.home.presentation.viewmodel.RechargeHomepageTickerQuery.TICKER_QUERY_NAME
import com.tokopedia.gql_query_annotation.GqlQuery

@GqlQuery(SKELETON_QUERY_NAME, SKELETON_QUERY)
internal object RechargeHomepageSkeletonQuery{
    const val SKELETON_QUERY_NAME = "QueryRechargeHomepageSkeleton"
    const val SKELETON_QUERY = """
        query rechargeGetDynamicPageSkeleton(${"$"}platformID: Int!, ${"$"}enablePersonalize: Boolean) {
            rechargeGetDynamicPageSkeleton(platformID: ${"$"}platformID, enablePersonalize: ${"$"}enablePersonalize){
                search_bar_placeholder
                search_bar_app_link
                search_bar_web_link
                search_bar_screen_name
                search_bar_redirection
                search_bar_type
                sections {
                  id
                  template
                }
           }
        }
    """
}

@GqlQuery(SECTION_QUERY_NAME, SECTION_QUERY)
internal object RechargeHomepageSectionQuery{
    const val SECTION_QUERY_NAME = "QueryRechargeHomepageSection"
    const val SECTION_QUERY = """
        query rechargeGetDynamicPage(${"$"}platformID: Int!, ${"$"}sectionIDs: [Int], ${"$"}enablePersonalize: Boolean) {
            rechargeGetDynamicPage(platformID: ${"$"}platformID, sectionIDs: ${"$"}sectionIDs, enablePersonalize: ${"$"}enablePersonalize){
                sections {
                  id
                  object_id
                  title
                  sub_title
                  template
                  app_link
                  text_link
                  media_url
                  label_1
                  label_2
                  tracking {
                    action
                    data
                  }
                  items {
                    id
                    object_id
                    title
                    sub_title
                    tracking {
                        action
                        data
                    }
                    content
                    app_link
                    web_link
                    text_link
                    media_url
                    template
                    button_type
                    label_1
                    label_2
                    label_3
                    server_date
                    due_date
                    attributes {
                        title_color
                        sub_title_color
                        media_url_title
                        media_url_type
                        icon_url
                        sold_value
                        sold_percentage_value
                        sold_percentage_label
                        sold_percentage_label_color
                        show_sold_percentage
                        campaign_label_text
                        campaign_label_text_color
                        campaign_label_background_url
                        rating_type
                        rating
                        review
                        special_info_text
                        special_info_color
                        special_discount
                        cashback
                        price_prefix
                        price_suffix
                    }
                  }
                }
            }
        }
    """
}

@GqlQuery(ACTION_QUERY_NAME, ACTION_QUERY)
internal object RechargeHomepageActionQuery{
    const val ACTION_QUERY_NAME = "QueryRechargeHomepageAction"
    const val ACTION_QUERY = """
        mutation rechargePostDynamicPageAction(${"$"}sectionID: Int!, ${"$"}action: String!) {
            rechargePostDynamicPageAction(sectionID: ${"$"}sectionID, action: ${"$"}action) {
                message
            }
        }
    """
}

@GqlQuery(TICKER_QUERY_NAME, TICKER_QUERY)
internal object RechargeHomepageTickerQuery{
    const val TICKER_QUERY_NAME = "QueryRechargeHomepageTicker"
    const val TICKER_QUERY = """
        query rechargeTicker(${"$"}categoryIDs: [Int], ${"$"}deviceID: Int!) {
            rechargeTicker(categoryIDs:${"$"}categoryIDs, deviceID: ${"$"}deviceID) {
                ID
                Name
                Content
                Type
                Environment
                ActionText
                ActionLink
                StartDate
                ExpireDate
                Status
            }
        }
    """
}
