package com.tokopedia.categorylevels.raw

const val GQL_CATEGORY_GET_DETAIL_MODULAR: String = """query categoryGetDetailModular(${'$'}identifier:String!, ${'$'}isLatestVersion:Boolean){
  categoryGetDetailModular(identifier:${'$'}identifier, isLatestVersion:${'$'}isLatestVersion){
    header{
      code
      message
    }
    basicInfo{
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
    }
    components{
      id
      name
      type
      targetID
      sticky
      properties{
        background
        dynamic
        categoryDetail
        backgroundImageURL
      }
      data{
        ... on CategoryModularChildrenNav{
          id
          name
          url
          thumbnailImage
          isAdult
          applinks
        }
          ... on CategoryModularStaticText{
            text
        } 
          ... on CategoryModularTab {
            id
            categoryName
	        targetComponentId
        }
      }
    }
  }
}"""
