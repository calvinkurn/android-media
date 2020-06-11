package com.tokopedia.home.beranda.domain.interactor

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.domain.model.salam_widget.SalamWidget
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * @author by firman on 09-06-2020
 */

class GetSalamWidgetUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<SalamWidget>)
    : UseCase<SalamWidget>() {

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(SalamWidget::class.java)
    }

    companion object {
        const val NULL_RESPONSE = "null response"
    }

    //region query
    private val query by lazy {
        """query salamWidget{
                salamWidget{
                     ID
                     mainText
                     SubText
                     AppLink
                     Link
                     IconURL
                     Title
                     BackgroundColor
                     ButtonText
                  }
            }
        """.trimIndent()
    }
    //endregion

    private val dummyResponse by lazy {
        "{\n" +
                "    \"salamWidget\": {\n" +
                "      \"ID\": 4,\n" +
                "      \"mainText\": \"Hi, Wulandari Saputri!\",\n" +
                "      \"SubText\": \"Jumat adalah hari yang baik untuk sedekah. Yuk sedekahkan Rp5.000 dari OVO kamu!\",\n" +
                "      \"AppLink\": \"tokopedia://s/umroh\",\n" +
                "      \"Link\": \"\",\n" +
                "      \"IconURL\": \"https://ecs7.tokopedia.net/img/cache/100-square/attachment/2019/1/9/20723472/20723472_4f6c17e5-5434-4da0-9dc0-35ae1395570c.png\",\n" +
                "      \"Title\": \"Yuk, berbagi setelah belanja!\",\n" +
                "      \"BackgroundColor\": \"#ff8b00\",\n" +
                "      \"ButtonText\": \"Berbagi Sekarang\"\n" +
                "    }\n" +
                "}"
    }

    override suspend fun executeOnBackground(): SalamWidget {
        graphqlUseCase.clearCache()
        graphqlUseCase.setGraphqlQuery(query)
      //  val response = graphqlUseCase.executeOnBackground()
        ///if (response != null) response
            return Gson().fromJson(dummyResponse, SalamWidget::class.java)
       // else throw (MessageErrorException(NULL_RESPONSE))
    }
}