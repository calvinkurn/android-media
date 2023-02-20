package com.tokopedia.affiliate.model.raw

val GQL_Affiliate_Education_Article_Cards: String = """query GetEducationArticleCards(
    ${"$"}filter:String!, ${"$"}category_id:Int, ${"$"}limit:Int,
    ${"$"}offset:Int, ${"$"}source:String, ${"$"}sort_by:String
    ) {
  cardsArticle(
  filter: ${"$"}filter, category_id: ${"$"}category_id, limit: ${"$"}limit, 
  offset: ${"$"}offset, source: ${"$"}source, sort_by: ${"$"}sort_by
  ) {
    data {
      status
      cards{
        id
        has_more
        title
        items{
          id
          title
          description
          slug
          modified_date
          publish_time
          categories{
            id
            title
            level
          }
          thumbnail{
            desktop
            mobile
            android
            ios
          }
          attributes{
            read_time
          }
          youtube_url
          duration
          upload_datetime
        }
        action_title
        action_link
        app_action_link
        limit
        offset
        total_count
      }
    }
  }
}
""".trimIndent()
