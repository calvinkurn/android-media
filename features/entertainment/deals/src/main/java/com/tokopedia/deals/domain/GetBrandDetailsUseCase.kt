package com.tokopedia.deals.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.deals.data.DealsNearestLocationParam
import com.tokopedia.deals.data.entity.DealsBrandDetail
import com.tokopedia.deals.ui.brand_detail.DealsBrandDetailViewModel
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

class GetBrandDetailsUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<GetBrandDetailsUseCase.Param, DealsBrandDetail>(dispatchers.io) {

    override suspend fun execute(params: Param): DealsBrandDetail {
        val mapParam = createParams(params.coordinates, params.seoUrl)
        return graphqlRepository.request(graphqlQuery(), mapParam)
    }

    override fun graphqlQuery(): String = """
        query (${'$'}params: [MapParamData]!){
            event_brand_detail_v2(params: ${'$'}params) {
                brand {
                    id
                    title
                    description
                    url
                    seo_url
                    featured_image
                    featured_thumbnail_image
                    city_name
                }
                products {
                    id
                    brand_id
                    category_id
                    provider_id
                    child_category_ids
                    city_ids
                    display_name
                    title
                    url
                    seo_url
                    image_web
                    thumbnail_web
                    image_app
                    thumbnail_app
                    display_tags
                    mrp
                    sales_price
                    priority
                    is_searchable
                    status
                    max_end_date
                    min_start_date
                    sale_end_date
                    sale_start_date
                    custom_text_1
                    min_start_time
                    max_end_time
                    sale_end_time
                    sale_start_time
                    city_name
                    likes
                    is_liked
                    saving_percentage
                    category{
                        id
                        title
                        media_url
                        url
                    }
                    message
                    code
                    message_error
                    no_promo
                    price
                    location
                    schedule
                    web_url
                    app_url
                }
                page{
                    next_page
                    prev_page
                }
                count
            }
        }
    """.trimIndent()

    private fun createParams(coordinates: String, seoUrl: String): Map<String, Any> =
        mapOf(
            DealsBrandDetailViewModel.PARAM_BRAND_DETAIL to arrayListOf(
                DealsNearestLocationParam(DealsNearestLocationParam.PARAM_SEO_URL, seoUrl),
                DealsNearestLocationParam(DealsNearestLocationParam.PARAM_COORDINATES, coordinates),
                DealsNearestLocationParam(
                    DealsNearestLocationParam.PARAM_SIZE,
                    DealsNearestLocationParam.VALUE_SIZE_PRODUCT
                ),
                DealsNearestLocationParam(
                    DealsNearestLocationParam.PARAM_PAGE,
                    DealsNearestLocationParam.VALUE_PAGE_BRAND
                )
            )
        )

    data class Param(
        val coordinates: String,
        val seoUrl: String
    )
}
