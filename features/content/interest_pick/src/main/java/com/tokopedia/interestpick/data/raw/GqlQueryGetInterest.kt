package com.tokopedia.interestpick.data.raw

const val GQL_QUERY_GET_INTEREST: String = """{
  feed_interest_user(){
    header{
      image_url
      title
      description
    }
    interests{
      id
      name
      image_url
      relationships{
        is_selected
      }
    }
    error
  }
}"""