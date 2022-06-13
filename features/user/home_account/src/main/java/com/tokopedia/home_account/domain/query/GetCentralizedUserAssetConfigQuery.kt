package com.tokopedia.home_account.domain.query

object GetCentralizedUserAssetConfigQuery {

    private const val entryPoint = "\$entryPoint"

    val query: String = """
        query get_centralized_user_asset_config($entryPoint: String!){
          GetCentralizedUserAssetConfig(entryPoint:$entryPoint){
            asset_config_horizontal {
              title
              subtitle_color
              subtitle
              applink
              icon
              is_active
              id
              hide_title
            }
            asset_config_vertical {
              title
              subtitle_color
              subtitle
              applink
              icon
              is_active
              id
              hide_title
            }
            asset_config {
              title
              subtitle_color
              subtitle
              applink
              icon
              is_active
              id
              hide_title
            }
          }
        }
    """.trimIndent()
}