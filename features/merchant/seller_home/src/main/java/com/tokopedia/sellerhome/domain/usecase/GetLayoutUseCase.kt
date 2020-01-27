package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.sellerhome.WidgetType
import com.tokopedia.sellerhome.view.model.*
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created By @ilhamsuaib on 2020-01-15
 */

class GetLayoutUseCase(
        graphqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<List<BaseWidgetUiModel>>() {

    override suspend fun executeOnBackground(): List<BaseWidgetUiModel> {
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
                DescriptionWidgetUiModel(
                        WidgetType.DESCRIPTION,
                        "Description Card",
                        "",
                        "",
                        DescriptionState.IDEAL,
                        "Pertahankan poin minimum 75 untuk tetap menjadi Power Merchant."
                ),
                DescriptionWidgetUiModel(
                        WidgetType.DESCRIPTION,
                        "Description Loading Card",
                        "",
                        "",
                        DescriptionState.LOADING,
                        ""
                ),
                DescriptionWidgetUiModel(
                        WidgetType.DESCRIPTION,
                        "Description Error Card",
                        "",
                        "",
                        DescriptionState.ERROR,
                        ""
                )
        )
    }
}