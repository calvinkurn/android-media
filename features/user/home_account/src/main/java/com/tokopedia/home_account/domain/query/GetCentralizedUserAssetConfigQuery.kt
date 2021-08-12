package com.tokopedia.home_account.domain.query

object GetCentralizedUserAssetConfigQuery {

    val query: String = """
        query {
          GetCentralizedUserAssetConfig(entryPoint:"user_page"){
            asset_config_horizontal {
              title
              subtitle_color
              subtitle
              applink
              icon
              is_active
              id
            }
            asset_config_vertical {
              title
              subtitle_color
              subtitle
              applink
              icon
              is_active
              id
            }
            asset_config {
              title
              subtitle_color
              subtitle
              applink
              icon
              is_active
              id
            }
          }
        }
    """.trimIndent()
}