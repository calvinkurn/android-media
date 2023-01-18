package com.tokopedia.topads.common.data.raw

const val TOP_ADS_GROUPS_STATISTIC_GQL = """
    query GetTopadsDashboardGroupStatisticsV3(${'$'}queryInput:GetTopadsDashboardGroupStatisticsV3!){
      GetTopadsDashboardGroupStatisticsV3(queryInput:${'$'}queryInput){
          page{
            current
            per_page
            min
            max
            total
         }
        data{
          group_id
          stat_total_impression
          stat_total_click
          stat_total_conversion
        }
        errors{
          code
          detail
          title
        } 
      }
}
"""
