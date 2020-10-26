package com.tokopedia.notifcenter.domain

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.notifcenter.data.entity.notification.NotifcenterDetailResponse
import com.tokopedia.notifcenter.data.mapper.NotifcenterDetailMapper
import com.tokopedia.notifcenter.presentation.adapter.typefactory.notification.NotificationTypeFactory
import com.tokopedia.notifcenter.util.coroutines.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class NotifcenterDetailUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<NotifcenterDetailResponse>,
        private val mapper: NotifcenterDetailMapper,
        private var dispatchers: DispatcherProvider
) : CoroutineScope {

    var timeZone = TimeZone.getDefault()
    var lastNotifId = ""
        private set

    override val coroutineContext: CoroutineContext get() = dispatchers.ui() + SupervisorJob()

    fun getNotifications(
            page: Int,
            onSuccess: (List<Visitable<NotificationTypeFactory>>) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        launchCatchError(
                dispatchers.io(),
                {
                    val params = generateParam(page)
                    val response = gqlUseCase.apply {
                        setTypeClass(NotifcenterDetailResponse::class.java)
                        setRequestParams(params)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    val items = mapper.map(response)
                    withContext(dispatchers.ui()) {
                        onSuccess(items)
                    }
                },
                {
                    withContext(dispatchers.ui()) {
                        onError(it)
                    }
                }
        )
    }

    private fun generateParam(page: Int): Map<String, Any?> {
        // TODO: refactor fot account switcher
        return mapOf(
                PARAM_PAGE to page,
                PARAM_TYPE_ID to 1,
                PARAM_TAG_ID to 0,
                PARAM_TIMEZONE to timeZone,
                PARAM_LAST_NOTIF_ID to lastNotifId,
                PARAM_FIELDS to arrayOf("new")
        )
    }

    companion object {
        private const val PARAM_PAGE = "page"
        private const val PARAM_TYPE_ID = "type_id"
        private const val PARAM_TAG_ID = "tag_id"
        private const val PARAM_TIMEZONE = "timezone"
        private const val PARAM_LAST_NOTIF_ID = "last_notif_id"
        private const val PARAM_FIELDS = "fields"
    }

    private val query = """
        query notifcenter_detail_v3(
                $$PARAM_PAGE: Int
                $$PARAM_TYPE_ID: Int
                $$PARAM_TAG_ID: Int
                $$PARAM_TIMEZONE: String
                $$PARAM_LAST_NOTIF_ID: String
                $$PARAM_FIELDS: [String]
            ){ 
            notifcenter_detail_v3(
                    page: $$PARAM_PAGE, 
                    type_id: $$PARAM_TYPE_ID, 
                    tag_id: $$PARAM_TAG_ID, 
                    timezone: $$PARAM_TIMEZONE, 
                    last_notif_id: $$PARAM_LAST_NOTIF_ID, 
                    fields: $$PARAM_FIELDS
                ) { 
                paging {
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
                            badges {
                                title 
                                image_url 
                            }
                            free_shipping_icon 
                        }
                        stock 
                        type_button 
                        is_show 
                        is_reminded 
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
                            badges {
                                title 
                                image_url 
                            }
                            free_shipping_icon 
                        }
                        stock 
                        type_button 
                        is_show 
                        is_reminded 
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