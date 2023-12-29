package com.tokopedia.tokopedianow.category.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetCategoryLayout: GqlQueryInterface {

    const val PARAM_IDENTIFIER = "identifier"
    const val PARAM_IS_LATEST_VERSION = "isLatestVersion"
    const val PARAM_CLIENT_TYPE = "clientType"

    override fun getQuery(): String = """
            query categoryGetDetailModular(
                ${'$'}${PARAM_IDENTIFIER}: String!, 
                ${'$'}${PARAM_IS_LATEST_VERSION}: Boolean, 
                ${'$'}${PARAM_CLIENT_TYPE}: String
            ){
              categoryGetDetailModular(
                ${PARAM_IDENTIFIER}: ${'$'}${PARAM_IDENTIFIER}, 
                ${PARAM_IS_LATEST_VERSION}: ${'$'}${PARAM_IS_LATEST_VERSION}, 
                ${PARAM_CLIENT_TYPE}: ${'$'}${PARAM_CLIENT_TYPE}
              ) {
                header {
                  code
                  message
                }
                basicInfo {
                  id
                  name
                  tree
                  parent
                  rootId
                  url
                  redirectionURL
                  appRedirectionURL
                  applinks
                  redirectTo
                  iconImageURL
                  hidden
                  view
                  intermediary
                  isAdult
                  isBanned
                  appRedirection
                  bannedMsgHeader
                  bannedMsg
                  titleTag
                  description
                  metaDescription
                  useDiscoPage
                  discoIdentifier
                  relatedCategory {
                    id
                  }
                  longDescription {
                    title
                    description
                  }
                }
                components {
                  id
                  name
                  type
                  targetID
                  sticky
                  properties {
                    background
                    dynamic
                    categoryDetail
                    backgroundColor
                    backgroundTransparentImageURL
                    backgroundImageURL
                  }
                  data {
                    ... on CategoryModularOtherCategoriesLink{
                      text
                      applink
                    }
                    ... on CategoryModularChildrenNav {
                      id
                      name
                      url
                      thumbnailImage
                      isAdult
                      applinks
                    }
                    ... on CategoryModularStaticText {
                      text
                    }
                    ... on CategoryModularTab {
                      id
                      categoryName
                      url
                      isAdult
                      targetComponentId
                    }
                  }
                }
              }
            }
        """.trimIndent()

    override fun getOperationNameList(): List<String> {
        return listOf("categoryGetDetailModular")
    }

    override fun getTopOperationName(): String {
        return "categoryGetDetailModular"
    }
}
