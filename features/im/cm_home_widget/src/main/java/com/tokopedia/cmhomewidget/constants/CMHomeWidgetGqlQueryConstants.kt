package com.tokopedia.cmhomewidget.constants

const val GQL_GET_CM_HOME_WIDGET_DATA = """query{
    notifier_getHtdw(){
        status
        data {
            user_id
            parent_id
            campaign_id
            widget_title
            widget_type
            products {
                id
                name
                image_url
                current_price
                dropped_percent
                badge_type
                badge_image_url
                app_link
                shop {
                    id
                    name
                    badge_title
                    badge_image_url
                }
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

