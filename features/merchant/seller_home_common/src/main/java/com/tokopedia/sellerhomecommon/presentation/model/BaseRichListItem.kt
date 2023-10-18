package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.sellerhomecommon.presentation.view.adapter.factory.RichListFactory

/**
 * Created by @ilhamsuaib on 17/04/23.
 */

interface BaseRichListItem : Visitable<RichListFactory> {

    data class RankItemUiModel(
        val title: String = String.EMPTY,
        val subTitle: String = String.EMPTY,
        val rankTrend: RankTrend = RankTrend.NONE,
        val rankValue: String = String.EMPTY,
        val rankNote: String = String.EMPTY,
        val tooltip: TooltipUiModel? = null
    ) : BaseRichListItem {

        override fun type(typeFactory: RichListFactory): Int {
            return typeFactory.type(this)
        }

        enum class RankTrend {
            DOWN, NONE, UP, DISABLED
        }
    }

    data class CaptionItemUiModel(
        val caption: String = String.EMPTY,
        val ctaText: String = String.EMPTY,
        val url: String = String.EMPTY
    ) : BaseRichListItem {

        override fun type(typeFactory: RichListFactory): Int {
            return typeFactory.type(this)
        }
    }

    data class TickerItemUiModel(
        val tickerDescription: String = String.EMPTY
    ) : BaseRichListItem {

        override fun type(typeFactory: RichListFactory): Int {
            return typeFactory.type(this)
        }
    }

    object LoadingStateUiModel : BaseRichListItem {

        override fun type(typeFactory: RichListFactory): Int {
            return typeFactory.type(this)
        }
    }

    object ErrorStateUiModel : BaseRichListItem {

        override fun type(typeFactory: RichListFactory): Int {
            return typeFactory.type(this)
        }
    }
}