package com.tokopedia.topads.common.data.raw

const val TOP_ADS_GROUPS_GQL = """
    query GetTopadsDashboardGroupsV3(${'$'}queryInput:GetTopadsDashboardGroupsInputTypeV3!){
      GetTopadsDashboardGroupsV3(queryInput:${'$'}queryInput){
          page{
            current
            per_page
            min
            max
            total
         }
        data{
          group_id
          group_status
          group_start_date
          group_end_date
          group_name
          group_type
          group_bid_setting{
            product_browse 
            product_search
          }
        }
        errors{
          code
          detail
          title
        } 
      }
}
"""
