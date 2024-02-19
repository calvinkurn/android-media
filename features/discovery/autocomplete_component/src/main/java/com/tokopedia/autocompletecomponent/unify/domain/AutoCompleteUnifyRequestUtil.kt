package com.tokopedia.autocompletecomponent.unify.domain

object AutoCompleteUnifyRequestUtil {
    private const val QUERY_DATA = """
        {
            data {
              suggestion_id
              feature_id
              feature_type
              template
              url
              applink
              is_ads
              image{
                image_url
                is_border
                radius
                icon_image_url
              }
              title{
                text
                type
                icon_image_url
              }
              subtitle{
                text
                icon_image_url
              }
              label{
                text
                text_color
                bg_color
                action
              }
              cta{
                action
                image_url
              }
              product{
                discount
                original_price
              }
              tracking{
                code
                component_id
                tracking_option
                tracker_url
              }
              flags{
                name
                value
                enable
              }
            }
            flags{
              name
              value
              enable
            }
            header{
              time_process
              status_code
              message
            }
        }
    """

    const val SUGGESTION_STATE_QUERY = """
        query SuggestionUnify(${'$'}params: String) {
          universe_suggestion_unify(param: ${'$'}params) $QUERY_DATA
        }
    """

    const val INITAL_STATE_QUERY = """
        query SuggestionUnify(${'$'}params: String) {
          universe_initial_state_unify(param: ${'$'}params) $QUERY_DATA
        }
    """

    const val INITIAL_STATE_USE_CASE = "initial_state_use_case"
    const val SUGGESTION_STATE_USE_CASE = "suggestion_state_use_case"
}
