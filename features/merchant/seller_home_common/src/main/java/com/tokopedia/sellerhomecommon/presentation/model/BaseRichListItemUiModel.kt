package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.sellerhomecommon.presentation.view.adapter.factory.RichListFactory

/**
 * Created by @ilhamsuaib on 17/04/23.
 */

sealed class BaseRichListItemUiModel(
    open val title: String = String.EMPTY,
    open val subTitle: String = String.EMPTY,
    open val imageUrl: String = String.EMPTY
) : Visitable<RichListFactory> {

    data class RankItemUiModel(
        override val title: String = String.EMPTY,
        override val subTitle: String = String.EMPTY,
        override val imageUrl: String = String.EMPTY,
        val rankTrend: RankTrend = RankTrend.NONE,
        val rankValue: String = String.EMPTY,
        val rankNote: String = String.EMPTY,
        val tooltip: TooltipListItemUiModel? = null
    ) : BaseRichListItemUiModel(title, subTitle, imageUrl) {

        override fun type(typeFactory: RichListFactory): Int {
            return typeFactory.type(this)
        }

        enum class RankTrend {
            DOWN, NONE, UP, DISABLED
        }
    }

    data class CaptionItemUiModel(
        val caption: String = String.EMPTY,
        val ctaList: List<CaptionCtaUiModel> = emptyList()
    ) : BaseRichListItemUiModel(String.EMPTY, String.EMPTY, String.EMPTY) {

        override fun type(typeFactory: RichListFactory): Int {
            return typeFactory.type(this)
        }

        data class CaptionCtaUiModel(
            val title: String = String.EMPTY,
            val subtitle: String = String.EMPTY,
            val imageUrl: String = String.EMPTY
        )
    }

    data class TickerItemUiModel(
        val tickerDescription: String = String.EMPTY
    ) : BaseRichListItemUiModel(String.EMPTY, String.EMPTY, String.EMPTY) {

        override fun type(typeFactory: RichListFactory): Int {
            return typeFactory.type(this)
        }
    }
}