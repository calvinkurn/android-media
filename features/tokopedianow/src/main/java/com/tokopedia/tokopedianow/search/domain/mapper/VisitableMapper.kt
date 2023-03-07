package com.tokopedia.tokopedianow.search.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.search.presentation.model.SuggestionDataView
import com.tokopedia.tokopedianow.search.presentation.typefactory.SearchTypeFactory
import com.tokopedia.tokopedianow.searchcategory.cartservice.CartService
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel

object VisitableMapper {
    private fun createSuggestionDataView(suggestionModel: AceSearchProductModel.Suggestion) =
        SuggestionDataView(
            text = suggestionModel.text,
            query = suggestionModel.query,
            suggestion = suggestionModel.suggestion,
        )

    fun MutableList<Visitable<SearchTypeFactory>>.addSuggestionDataView(
        suggestionModel: AceSearchProductModel.Suggestion?
    ) {
        if (suggestionModel != null && suggestionModel.text.isNotEmpty()) {
            val suggestionDataView = createSuggestionDataView(suggestionModel)
            add(suggestionDataView)
        }
    }

    fun MutableList<Visitable<SearchTypeFactory>>.addBroadMatchDataView(
        related: AceSearchProductModel.Related?,
        cartService: CartService
    ) {
        related?.otherRelatedList?.forEach { otherRelated ->
            val broadMatchDataView = SearchBroadMatchMapper.createBroadMatchDataView(
                otherRelated = otherRelated,
                cartService = cartService
            )
            add(broadMatchDataView)
        }
    }

    fun MutableList<Visitable<*>>.updateSuggestionDataView(
        suggestionModel: AceSearchProductModel.Suggestion?,
        suggestionIndex: Int
    ) {
        if (suggestionModel != null && suggestionModel.text.isNotEmpty()) {
            val suggestionDataView = createSuggestionDataView(suggestionModel)
            add(suggestionIndex, suggestionDataView)
        }
    }
}
