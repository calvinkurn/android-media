package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.sellerhome.WidgetType
import com.tokopedia.sellerhome.view.model.BaseWidgetUiModel
import com.tokopedia.sellerhome.view.model.CardWidgetUiModel
import com.tokopedia.sellerhome.view.model.CarouselWidgetUiModel
import com.tokopedia.sellerhome.view.model.LineGraphWidgetUiModel
import com.tokopedia.usecase.coroutines.UseCase
import java.util.ArrayList

/**
 * Created By @ilhamsuaib on 2020-01-15
 */

class GetLayoutUseCase(
        graphqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<List<BaseWidgetUiModel>>() {

    override suspend fun executeOnBackground(): List<BaseWidgetUiModel> {

        val strings: ArrayList<String> = ArrayList()
        strings.add("http://placekitten.com/100/50")
        strings.add("http://placekitten.com/100/51")
        strings.add("http://placekitten.com/100/53")
        strings.add("http://placekitten.com/100/54")

        //handle request here
        return listOf(
                CardWidgetUiModel(
                        WidgetType.CARD,
                        "Card 1",
                        "",
                        ""
                ),
                CardWidgetUiModel(
                        WidgetType.CARD,
                        "Card 2",
                        "",
                        ""
                ),
                CardWidgetUiModel(
                        WidgetType.CARD,
                        "Card 3",
                        "",
                        ""
                ),
                LineGraphWidgetUiModel(
                        WidgetType.LINE_GRAPH,
                        "Line Graph",
                        "",
                        ""
                ),
                CarouselWidgetUiModel(
                        WidgetType.CAROUSEL,
                        "Carousel",
                        "",
                        "",
                        strings
                )
        )
    }
}