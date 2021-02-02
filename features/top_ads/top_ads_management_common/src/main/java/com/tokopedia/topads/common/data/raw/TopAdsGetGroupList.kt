package com.tokopedia.topads.common.data.raw

const val GROUP_LIST_QUERY = """
    query GetTopadsDashboardGroups(${'$'}queryInput: GetTopadsDashboardGroupsInputType!) {
  GetTopadsDashboardGroups(queryInput: ${'$'}queryInput) {
       meta {
          page {
            per_page
            current
            total
          }
        }
    data {
      group_id
      total_item
      total_keyword
      group_status_desc
      group_name

    }
  }
}
"""