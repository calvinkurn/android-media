package com.tokopedia.cmhomewidget.domain.query

const val GQL_QUERY_GET_CM_HOME_WIDGET_DATA = """query(${"$"}param: Notifier_Htdw_v2_request!){
    notifier_getHtdw_v2(input: ${"$"}param){
        status
        data {
            notification_id
            message_id
            is_test
            parent_id
            campaign_id
            session_id
            widget_title
            widget_type
            products {
                id
                name
                image_url
                current_price
                dropped_percent
                badge_image_url
                app_link
                shop {
                    id
                    name
                    badge_image_url
                }
                action_buttons {
                    id
                    text
                    app_link
                }
            }
            payments {
                image_url
                gateway_name
                account_number
                total_payment
                expired_time
                app_link
                action_buttons {
                        id
                        icon
                        text
                        type
                        app_link
                }
            }
            card {
                label
                description
                app_link
            }
        }
    }
}"""

const val GQL_QUERY_DISMISS_CM_HOME_WIDGET = """
    mutation(${'$'}parentID:Int!, ${'$'}campaignID:Int!) {
    notifier_dismissHtdw(parentID: ${'$'}parentID, campaignID: ${'$'}campaignID){
      status
    }
}
"""

