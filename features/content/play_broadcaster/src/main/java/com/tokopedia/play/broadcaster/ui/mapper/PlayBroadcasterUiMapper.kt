package com.tokopedia.play.broadcaster.ui.mapper

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import com.tokopedia.play.broadcaster.domain.model.CreateLiveStreamChannelResponse
import com.tokopedia.play.broadcaster.domain.model.GetProductsByEtalaseResponse
import com.tokopedia.play.broadcaster.type.EtalaseType
import com.tokopedia.play.broadcaster.ui.model.EtalaseContentUiModel
import com.tokopedia.play.broadcaster.ui.model.LiveStreamInfoUiModel
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.ui.model.SearchSuggestionUiModel
import com.tokopedia.play.broadcaster.view.state.SelectableState
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel

/**
 * Created by jegul on 02/06/20
 */
object PlayBroadcasterUiMapper {

    fun mapEtalaseList(etalaseList: List<ShopEtalaseModel>): List<EtalaseContentUiModel> = etalaseList.map {
        val type = EtalaseType.getByType(it.type, it.id)
        EtalaseContentUiModel(
                id = if (type is EtalaseType.Group) type.fMenu else it.id,
                name = it.name,
                productMap = mutableMapOf(),
                totalProduct = it.count,
                stillHasProduct = true
        )
    }

    fun mapProductList(
            productsResponse: GetProductsByEtalaseResponse.GetShopProductData,
            isSelectedHandler: (Long) -> Boolean,
            isSelectableHandler: () -> SelectableState
    ) = productsResponse.data.map {
        ProductContentUiModel(
                id = it.productId.toLong(),
                name = it.name,
                imageUrl = it.primaryImage.resize300,
                originalImageUrl = it.primaryImage.original,
//                stock = it.stock,
                stock = 2, // TODO("for testing only")
                isSelectedHandler = isSelectedHandler,
                isSelectable = isSelectableHandler
        )
    }

    fun mapSearchSuggestionList(
            keyword: String,
            productsResponse: GetProductsByEtalaseResponse.GetShopProductData
    ) = productsResponse.data.map {
        val fullSuggestedText = it.name
        val startIndex = fullSuggestedText.indexOf(keyword)
        val lastIndex = startIndex + keyword.length

        SearchSuggestionUiModel(
                queriedText = keyword,
                suggestedText = it.name,
                spannedSuggestion = SpannableStringBuilder(fullSuggestedText).apply {
                    if (startIndex >= 0) setSpan(StyleSpan(Typeface.BOLD), startIndex, lastIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                }
        )
    }

    fun mapLiveStream(channelId: String, media: CreateLiveStreamChannelResponse.GetMedia) =
            LiveStreamInfoUiModel(
                    channelId = channelId,
                    ingestUrl = media.ingestUrl,
                    streamUrl = media.streamUrl)
}