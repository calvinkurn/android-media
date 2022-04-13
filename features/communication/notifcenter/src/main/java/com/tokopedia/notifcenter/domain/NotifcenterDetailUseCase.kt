package com.tokopedia.notifcenter.domain

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.notification.NotifcenterDetailResponse
import com.tokopedia.notifcenter.data.entity.notification.NotificationDetailResponseModel
import com.tokopedia.notifcenter.data.entity.notification.Paging
import com.tokopedia.notifcenter.data.mapper.NotifcenterDetailMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class NotifcenterDetailUseCase @Inject constructor(
    private val gqlUseCase: GraphqlUseCase<NotifcenterDetailResponse>,
    private val mapper: NotifcenterDetailMapper,
    private var dispatchers: CoroutineDispatchers
) : CoroutineScope {

    var timeZone = TimeZone.getDefault().id
    var pagingNew = Paging()
    var pagingEarlier = Paging()

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + dispatchers.main

    fun getFirstPageNotification(
        filter: Long,
        @RoleType
        role: Int,
        onSuccess: (NotificationDetailResponseModel) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val fields = if (!hasFilter(filter)) {
            arrayOf("new")
        } else {
            emptyArray()
        }
        val params = generateParam(
            filter, role, "", fields
        )
        val needSectionTitle = !hasFilter(filter)
        val needLoadMoreButton = needSectionTitle
        getNotifications(
            params, onSuccess, onError,
            { response ->
                mapper.mapFirstPage(response, needSectionTitle, needLoadMoreButton)
            },
            { response ->
                updateNewPaging(response)
                updateEarlierPaging(response)
            }
        )
    }

    fun getMoreNewNotifications(
        filter: Long,
        @RoleType
        role: Int,
        onSuccess: (NotificationDetailResponseModel) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val params = generateParam(
            filter, role, pagingNew.lastNotifId, arrayOf("new")
        )
        val needLoadMoreButton = !hasFilter(filter)
        getNotifications(
            params, onSuccess, onError,
            { response ->
                mapper.mapNewSection(response, false, needLoadMoreButton)
            },
            { response ->
                updateNewPaging(response)
            }
        )
    }

    fun getMoreEarlierNotifications(
        filter: Long,
        @RoleType
        role: Int,
        onSuccess: (NotificationDetailResponseModel) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val params = generateParam(
            filter, role, pagingEarlier.lastNotifId, emptyArray()
        )
        val needLoadMoreButton = !hasFilter(filter)
        getNotifications(
            params, onSuccess, onError,
            { response ->
                mapper.mapEarlierSection(response, false, needLoadMoreButton)
            },
            { response ->
                updateEarlierPaging(response)
            }
        )
    }

    private fun hasFilter(filter: Long): Boolean {
        return filter != FILTER_NONE
    }

    private fun getNotifications(
        params: Map<String, Any?>,
        onSuccess: (NotificationDetailResponseModel) -> Unit,
        onError: (Throwable) -> Unit,
        mapping: (response: NotifcenterDetailResponse) -> NotificationDetailResponseModel,
        onResponseReady: (response: NotifcenterDetailResponse) -> Unit
    ) {
        launchCatchError(
            dispatchers.io,
            {
                val response = gqlUseCase.apply {
                    setTypeClass(NotifcenterDetailResponse::class.java)
                    setRequestParams(params)
                    setGraphqlQuery(query)
                }.executeOnBackground()
                val items = mapping(response)
                withContext(dispatchers.main) {
                    onResponseReady(response)
                    onSuccess(items)
                }
            },
            {
                withContext(dispatchers.main) {
                    onError(it)
                }
            }
        )
    }

    private fun updateNewPaging(response: NotifcenterDetailResponse) {
        pagingNew = response.notifcenterDetail.newPaging
    }

    private fun updateEarlierPaging(response: NotifcenterDetailResponse) {
        pagingEarlier = response.notifcenterDetail.paging
    }

    private fun generateParam(
        filter: Long,
        @RoleType
        role: Int,
        lastNotifId: String,
        fields: Array<String>
    ): Map<String, Any?> {
        return mapOf(
            PARAM_TYPE_ID to role,
            PARAM_TAG_ID to filter,
            PARAM_TIMEZONE to timeZone,
            PARAM_LAST_NOTIF_ID to lastNotifId,
            PARAM_FIELDS to fields
        )
    }

    fun cancelRunningOperation() {
        coroutineContext.cancelChildren()
    }

    companion object {
        const val FILTER_NONE: Long = 0

        private const val PARAM_TYPE_ID = "type_id"
        private const val PARAM_TAG_ID = "tag_id"
        private const val PARAM_TIMEZONE = "timezone"
        private const val PARAM_LAST_NOTIF_ID = "last_notif_id"
        private const val PARAM_FIELDS = "fields"

        private val query = """
            query notifcenter_detail_v3(
                $PARAM_TYPE_ID: Int
            	$PARAM_TAG_ID: Int
            	$PARAM_TIMEZONE: String
            	$PARAM_LAST_NOTIF_ID: String
            	$PARAM_FIELDS: [String]
            ) {
            	notifcenter_detail_v3(
            		type_id: $PARAM_TYPE_ID
            		tag_id: $PARAM_TAG_ID
            		timezone: $PARAM_TIMEZONE
            		last_notif_id: $PARAM_LAST_NOTIF_ID
            		fields: $PARAM_FIELDS
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