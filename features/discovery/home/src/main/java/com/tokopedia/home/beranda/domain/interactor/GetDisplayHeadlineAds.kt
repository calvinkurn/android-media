package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.domain.model.DisplayHeadlineAdsEntity
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by Lukas on 08/09/20.
 */
class GetDisplayHeadlineAds @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<DisplayHeadlineAdsEntity>
): UseCase<List<DisplayHeadlineAdsEntity.DisplayHeadlineAds>>(){
    companion object{
        private const val PARAMS_QUERY = "params"
        private const val query = "query DisplayHeadlineAds(${'$'}params: String) {\n" +
                "  displayAdsV3(displayParams: ${'$'}params) {\n" +
                "    data {\n" +
                "      id\n" +
                "      ad_ref_key\n" +
                "      ad_click_url\n" +
                "      applinks\n" +
                "      redirect\n" +
                "      headline {\n" +
                "        button_text\n" +
                "        description\n" +
                "        name\n" +
                "        promoted_text\n" +
                "        template_id\n" +
                "        uri\n" +
                "        image {\n" +
                "          full_ecs\n" +
                "          full_url\n" +
                "        }\n" +
                "        badges {\n" +
                "          image_url\n" +
                "          show\n" +
                "          title\n" +
                "        }\n" +
                "        shop {\n" +
                "          id\n" +
                "          name\n" +
                "          domain\n" +
                "          tagline\n" +
                "          slogan\n" +
                "          location\n" +
                "          city\n" +
                "          gold_shop\n" +
                "          gold_shop_badge\n" +
                "          shop_is_official\n" +
                "          pm_pro_shop\n" +
                "          is_followed\n" +
                "          image_shop {\n" +
                "            s_url\n" +
                "          }\n" +
                "          product {\n" +
                "            id\n" +
                "            name\n" +
                "            price_format\n" +
                "            uri\n" +
                "            applinks\n" +
                "            product_rating\n" +
                "            count_review_format\n" +
                "            image_product{\n" +
                "              image_url\n" +
                "              image_click_url\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}"
    }

    private var params : Map<String, Any> = mapOf()

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(DisplayHeadlineAdsEntity::class.java)
    }

    fun createParams(widgetParam: String) {
         params = mutableMapOf<String, Any>().apply{
             put(PARAMS_QUERY, widgetParam)
         }
    }

    override suspend fun executeOnBackground(): List<DisplayHeadlineAdsEntity.DisplayHeadlineAds> {
        graphqlUseCase.clearCache()
        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setRequestParams(params)
        return graphqlUseCase.executeOnBackground().displayAds.data
    }

}