package com.tokopedia.topads.dashboard.data.raw

const val STATS_URL = """query topadsDashboardStatisticsV2 (${'$'}startDate: String!, ${'$'}endDate: String!,${'$'}shopID: String!,${'$'}type:Int,${'$'}group:String, ${'$'}goal_id: Int!){
    topadsDashboardStatisticsV2(startDate:${'$'}startDate,endDate:${'$'}endDate,shopID:${'$'}shopID,type:${'$'}type,group:${'$'}group,goal_id:${'$'}goal_id){
    data{
      summary {
        ads_impression_sum
        ads_click_sum
        ads_ctr_percentage
        ads_cost_avg
        ads_conversion_sum
        ads_all_sold_sum
        ads_cost_sum
        ads_all_gross_profit
        ads_follow_count
        ads_impression_sum_fmt
        ads_click_sum_fmt
        ads_ctr_percentage_fmt
        ads_cost_avg_fmt
        ads_conversion_sum_fmt
        ads_all_sold_sum_fmt
        ads_cost_sum_fmt
        ads_all_gross_profit_fmt
        ads_follow_count_fmt
        cost_sum
        all_gross_profit
        cost_sum_fmt
        all_gross_profit_fmt

      }
      cells {
        day
        month
        year
        ads_impression_sum
        ads_click_sum
        ads_ctr_percentage
        ads_cost_avg
        ads_conversion_sum
        ads_all_sold_sum
        ads_cost_sum
        ads_all_gross_profit
        ads_impression_sum_fmt
        ads_click_sum_fmt
        ads_ctr_percentage_fmt
        ads_cost_avg_fmt
        ads_conversion_sum_fmt
        ads_all_sold_sum_fmt
        ads_cost_sum_fmt
        ads_all_gross_profit_fmt
        cost_sum
        all_gross_profit
        cost_sum_fmt
        all_gross_profit_fmt
        aff_impression_sum
        aff_impression_sum_fmt
      }
    }
  }
}"""