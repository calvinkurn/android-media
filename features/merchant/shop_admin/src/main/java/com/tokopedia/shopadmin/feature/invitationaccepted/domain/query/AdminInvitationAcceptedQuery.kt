package com.tokopedia.shopadmin.feature.invitationaccepted.domain.query


internal const val GET_ADMIN_INFO_QUERY = """
        query getAdminInfo(${'$'}source: String!, ${'$'}shop_id: Int!) {
              getAdminInfo(source: ${'$'}source, shop_id: ${'$'}shop_id) {
                    admin_data {
                      permission_list {  
                          permission_id
                          permission_name
                          icon_url
                      }
                   }
                }
            }
    """

internal const val ADMIN_MANAGEMENT_INFO_LIST_QUERY = """
        query getAdminManagementInfoList(${'$'}shop_id: String!) {
          getAdminManagementInfoList(shop_id: ${'$'}shop_id, source: "admin-management-ui", lang: "id", device: "android") {
            allPermissionList {
              permissionId
              permissionName
              description
              iconURL
              permissionRecursive {
                permissionId
                permissionName
                description
                iconURL
              }
            }
          }
        }
    """

internal const val ARTICLE_DETAIL_QUERY = """
        query getArticleDetail(${'$'}slug: String!){
          articleDetail(slug: ${'$'}slug) {
            data {
              blog {
                title
                html_content
              }
            }
          }
        }
    """
