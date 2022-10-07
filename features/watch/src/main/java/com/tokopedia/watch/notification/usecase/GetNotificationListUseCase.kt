package com.tokopedia.watch.notification.usecase

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.watch.orderlist.model.OrderListModel
import rx.functions.Func1

class GetNotificationListUseCase(
    private val graphqlUseCase: GraphqlUseCase,
    private val orderListModelMapper: Func1<GraphqlResponse?, OrderListModel>
) {

    companion object {
        const val FILTER_NONE: Long = 0

        private const val PARAM_TYPE_ID = "type_id"
        private const val PARAM_TAG_ID = "tag_id"
        private const val PARAM_TIMEZONE = "timezone"
        private const val PARAM_LAST_NOTIF_ID = "last_notif_id"
        private const val PARAM_FIELDS = "fields"

        private val query = """
            query notifcenter_detail_v3(
                $$PARAM_TYPE_ID: Int
            	$$PARAM_TAG_ID: Int
            	$$PARAM_TIMEZONE: String
            	$$PARAM_LAST_NOTIF_ID: String
            	$$PARAM_FIELDS: [String]
            ) {
            	notifcenter_detail_v3(
            		type_id: $$PARAM_TYPE_ID
            		tag_id: $$PARAM_TAG_ID
            		timezone: $$PARAM_TIMEZONE
            		last_notif_id: $$PARAM_LAST_NOTIF_ID
            		fields: $$PARAM_FIELDS
            	) {
            		paging {
            			has_next
            			has_prev
            		}
            		new_paging {
            			has_next
            			has_prev
            		}
            		new_list {
            			notif_id
            			user_id
            			shop_id
            			section_id
            			section_key
            			section_icon
            			subsection_key
            			template_key
            			title
            			button_text
            			short_description
            			short_description_html
            			is_longer_content
            			show_bottomsheet
            			type_bottomsheet
            			content
            			type_of_user
            			type_link
            			create_time
            			create_time_unix
            			update_time
            			update_time_unix
            			expire_time
            			expire_time_unix
            			status
            			read_status
            		    notif_order_type
            		    is_last_journey
            		    is_show_expire
                        is_pinned
                        pinned_text
                        unique_id
                        widget {
                            title
                            description
                            message
                            image
                            button_text
                            desktop_button_link
                            mobile_button_link
                            android_button_link
                            ios_button_link
                        }
                        track_history {
                            title
                            create_time_unix
                        }
            			image {
            				url
            				width
            				height
            				ratio {
            					x
            					y
            				}
            			}
            			data_notification {
            				app_link
            				desktop_link
            				info_thumbnail_url
            				mobile_link
            				product_name
            				product_url
            				checkout_url
            			}
            			total_product
            			product_data {
            				has_reminder
            				product_id
            				name
            				url
                            android_url
                            ios_url
            				image_url
            				department_id
            				min_order
            				price
            				price_fmt
            				currency
            				price_idr
            				is_buyable
            				is_topads
            				is_wishlist
            				is_preorder
            				rating
            				count_review
                            warehouse_id
            				labels {
            					title
            					color
            				}
            				campaign {
            					active
            					original_price
            					original_price_fmt
            					discount_percentage
            					discount_price
            					discount_price_fmt
            				}
            				variant {
            					value
            					identifier
            					hex
            				}
            				shop {
            					id
            					name
            					location
            					is_gold
            					is_official
            					is_tokonow
            					badges {
            						title
            						image_url
            					}
            					free_shipping_icon
            				}
            				stock
            				type_button
            				is_show
            				is_variant
            			}
            			bottomsheet {
            				title
            				button_cta {
            					type
            					text
            					link
            					data
            				}
            				components {
            					identifier
            					data_components
            				}
            			}
            		}
            		list {
            			notif_id
            			user_id
            			shop_id
            			section_id
            			section_key
            			section_icon
            			subsection_key
            			template_key
            			title
            			button_text
            			short_description
            			short_description_html
            			is_longer_content
            			show_bottomsheet
            			type_bottomsheet
            			content
            			type_of_user
            			type_link
            			create_time
            			create_time_unix
            			update_time
            			update_time_unix
            			expire_time
            			expire_time_unix
            			status
            			read_status
            		    notif_order_type
            		    is_last_journey
                        is_show_expire
                        is_pinned
                        pinned_text
                        unique_id
                        widget {
                            title
                            description
                            message
                            image
                            button_text
                            desktop_button_link
                            mobile_button_link
                            android_button_link
                            ios_button_link
                        }
                        track_history {
                            title
                            create_time_unix
                        }
            			image {
            				url
            				width
            				height
            				ratio {
            					x
            					y
            				}
            			}
            			data_notification {
            				app_link
            				desktop_link
            				info_thumbnail_url
            				mobile_link
            				product_name
            				product_url
            				checkout_url
            			}
            			total_product
            			product_data {
            				has_reminder
            				product_id
            				name
            				url
                            android_url
                            ios_url
            				image_url
            				department_id
            				min_order
            				price
            				price_fmt
            				currency
            				price_idr
            				is_buyable
            				is_topads
            				is_wishlist
            				is_preorder
            				rating
            				count_review
                            warehouse_id
            				labels {
            					title
            					color
            				}
            				campaign {
            					active
            					original_price
            					original_price_fmt
            					discount_percentage
            					discount_price
            					discount_price_fmt
            				}
            				variant {
            					value
            					identifier
            					hex
            				}
            				shop {
            					id
            					name
            					location
            					is_gold
            					is_official
            					is_tokonow
            					badges {
            						title
            						image_url
            					}
            					free_shipping_icon
            				}
            				stock
            				type_button
            				is_show
            				is_variant
            			}
            			bottomsheet {
            				title
            				button_cta {
            					type
            					text
            					link
            					data
            				}
            				components {
            					identifier
            					data_components
            				}
            			}
            		}
            		user_info {
            			user_id
            			shop_id
            			email
            			fullname
            		}
            		options {
            			longer_content
            		}
            		empty_state_content {
            			content
            			link {
            				desktop_link
            				mobile_link
            				android_link
            				ios_link
            			}
            		}
            	}
            }
        """.trimIndent()
    }

}