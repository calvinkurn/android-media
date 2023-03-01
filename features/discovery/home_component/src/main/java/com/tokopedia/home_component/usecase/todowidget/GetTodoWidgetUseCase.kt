package com.tokopedia.home_component.usecase.todowidget

import android.os.Bundle
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home_component.model.HomeComponentCta
import com.tokopedia.home_component.query.TodoWidgetQuery
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by frenzel
 */
class GetTodoWidgetUseCase @Inject constructor(
    private val graphqlUseCase: GraphqlUseCase<HomeTodoWidgetData.HomeTodoWidget>
) : UseCase<HomeTodoWidgetData.HomeTodoWidget>() {

    init {
        graphqlUseCase.setTypeClass(HomeTodoWidgetData.HomeTodoWidget::class.java)
        graphqlUseCase.setCacheStrategy(
            GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        )
    }

    private val params = RequestParams.create()

    companion object {
        const val PARAM = "param"
        const val LOCATION_PARAM = "location"
    }

    override suspend fun executeOnBackground(): HomeTodoWidgetData.HomeTodoWidget {
        return HomeTodoWidgetData.HomeTodoWidget(
            HomeTodoWidgetData.GetHomeTodoWidget(
                listOf<HomeTodoWidgetData.Todo>(
                    HomeTodoWidgetData.Todo(
                        id = 1,
                        title = "Pembayaran Virtual Account-mu",
                        dueDate = "Jatuh Tempo 2 Hari Lagi!",
                        contextInfo = "Contextual description maximum 2 lines",
                        feParam = "close=123",
                        imageUrl = "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2022/9/26/d4fa8b84-bc32-44d7-b4bd-4ba059ae192e.jpg",
                        cta = HomeTodoWidgetData.Cta(type = HomeComponentCta.CTA_TYPE_FILLED, mode = HomeComponentCta.CTA_MODE_MAIN, text = "Bayar")
                    ),
                    HomeTodoWidgetData.Todo(
                        id = 2,
                        title = "Selesaikan pembayaran kamu, yuk!",
                        dueDate = "Paling Lambat Besok",
                        contextInfo = "BCA - Virtual Account",
                        price = "Rp7.999.000",
                        feParam = "close=124",
                        imageUrl = "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2022/9/26/d4fa8b84-bc32-44d7-b4bd-4ba059ae192e.jpg",
                        cta = HomeTodoWidgetData.Cta(type = HomeComponentCta.CTA_TYPE_FILLED, mode = HomeComponentCta.CTA_MODE_MAIN, text = "Bayar")
                    ),
                    HomeTodoWidgetData.Todo(
                        id = 3,
                        title = "Perlu cicilan? Pakai GoPayLater Cicil aja!",
                        feParam = "close=125",
                        contextInfo = "Limit Rp5.000.000 siap dipakai, tinggal aktifkan. Yuk pakai sekarang!",
                        imageUrl = "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2022/9/26/d4fa8b84-bc32-44d7-b4bd-4ba059ae192e.jpg",
                        cta = HomeTodoWidgetData.Cta(type = HomeComponentCta.CTA_TYPE_GHOST, mode = HomeComponentCta.CTA_MODE_MAIN, text = "Cek")
                    )
                )
            )
        )
        graphqlUseCase.clearCache()
        graphqlUseCase.setGraphqlQuery(TodoWidgetQuery())
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()
    }

    fun generateParam(bundle: Bundle) {
        bundle.getString(LOCATION_PARAM, "")?.let {
            params.putString(LOCATION_PARAM, it)
        }
        bundle.getString(PARAM, "")?.let {
            params.putString(PARAM, it)
        }
    }
}
