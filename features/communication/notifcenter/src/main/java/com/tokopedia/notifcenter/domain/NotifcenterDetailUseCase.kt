package com.tokopedia.notifcenter.domain

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.notifcenter.data.entity.notification.NotifcenterDetailResponse
import com.tokopedia.notifcenter.data.entity.notification.NotificationDetailResponseModel
import com.tokopedia.notifcenter.data.entity.notification.Paging
import com.tokopedia.notifcenter.data.mapper.NotifcenterDetailMapper
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class NotifcenterDetailUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    private val mapper: NotifcenterDetailMapper,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<NotifcenterDetailUseCase.Param, NotificationDetailResponseModel>(dispatchers.io) {

    private var timeZone = TimeZone.getDefault().id
    private var pagingNew = Paging()
    private var pagingEarlier = Paging()
    private var newArrayFields = arrayListOf(NEW)

    override fun graphqlQuery(): String = """
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

    override suspend fun execute(params: Param): NotificationDetailResponseModel {
        return when (params.loadType) {
            NotificationDetailLoadType.FIRST_PAGE -> {
                getFirstPageNotification(params)
            }
            NotificationDetailLoadType.LOAD_MORE_NEW -> {
                getMoreNewNotifications(params)
            }
            NotificationDetailLoadType.LOAD_MORE_EARLIER -> {
                getMoreEarlierNotifications(params)
            }
        }
    }

    private suspend fun getFirstPageNotification(params: Param): NotificationDetailResponseModel {
        params.apply {
            fields = if (!hasFilter(params.filter)) {
                newArrayFields
            } else {
                arrayListOf()
            }
            timeZone = this@NotifcenterDetailUseCase.timeZone
        }

        val needSectionTitleAndLoadMoreButton = !hasFilter(params.filter)
        val response = repository.request<Param, NotifcenterDetailResponse>(
            graphqlQuery(),
            params
        )
        val result = mapper.mapFirstPage(
            response = response,
            needSectionTitle = needSectionTitleAndLoadMoreButton,
            needLoadMoreButton = needSectionTitleAndLoadMoreButton,
            needDivider = needDividerOnFirstPage(params.role)
        )
        updateNewPaging(response)
        updateEarlierPaging(response)
        return result
    }

    private suspend fun getMoreNewNotifications(
        params: Param
    ): NotificationDetailResponseModel {
        params.apply {
            fields = newArrayFields
            timeZone = this@NotifcenterDetailUseCase.timeZone
            lastNotifId = pagingNew.lastNotifId
        }
        val needLoadMoreButton = !hasFilter(params.filter)
        val response = repository.request<Param, NotifcenterDetailResponse>(
            graphqlQuery(),
            params
        )
        val result = mapper.mapNewSection(response, false, needLoadMoreButton)
        updateNewPaging(response)
        return result
    }

    private suspend fun getMoreEarlierNotifications(
        params: Param
    ): NotificationDetailResponseModel {
        params.apply {
            fields = arrayListOf()
            timeZone = this@NotifcenterDetailUseCase.timeZone
            lastNotifId = pagingEarlier.lastNotifId
        }
        val needLoadMoreButton = !hasFilter(params.filter)
        val response = repository.request<Param, NotifcenterDetailResponse>(
            graphqlQuery(),
            params
        )
        val result = mapper.mapEarlierSection(response, false, needLoadMoreButton)
        updateEarlierPaging(response)
        return result
    }

    private fun hasFilter(filter: Long): Boolean {
        return filter != FILTER_NONE
    }

    private fun needDividerOnFirstPage(@RoleType role: Int): Boolean {
        return when (role) {
            RoleType.BUYER, RoleType.SELLER -> false
            RoleType.AFFILIATE -> true
            else -> false
        }
    }

    private fun updateNewPaging(response: NotifcenterDetailResponse) {
        pagingNew = response.notifcenterDetail.newPaging
    }

    private fun updateEarlierPaging(response: NotifcenterDetailResponse) {
        pagingEarlier = response.notifcenterDetail.paging
    }

    data class Param(
        @SerializedName(PARAM_TAG_ID)
        val filter: Long,
        @RoleType
        @SerializedName(PARAM_TYPE_ID)
        val role: Int,
        @SerializedName(PARAM_TIMEZONE)
        var timeZone: String = "",
        @SerializedName(PARAM_LAST_NOTIF_ID)
        var lastNotifId: String = "",
        @SerializedName(PARAM_FIELDS)
        var fields: ArrayList<String> = arrayListOf()
    ) : GqlParam {
        var loadType: NotificationDetailLoadType = NotificationDetailLoadType.FIRST_PAGE
    }

    @Keep
    enum class NotificationDetailLoadType {
        FIRST_PAGE,
        LOAD_MORE_NEW,
        LOAD_MORE_EARLIER
    }

    companion object {
        const val FILTER_NONE: Long = 0

        private const val PARAM_TYPE_ID = "type_id"
        private const val PARAM_TAG_ID = "tag_id"
        private const val PARAM_TIMEZONE = "timezone"
        private const val PARAM_LAST_NOTIF_ID = "last_notif_id"
        private const val PARAM_FIELDS = "fields"

        private const val NEW = "new"
    }
}
