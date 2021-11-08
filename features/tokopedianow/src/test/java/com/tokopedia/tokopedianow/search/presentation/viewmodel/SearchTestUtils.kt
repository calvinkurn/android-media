package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.search.presentation.model.SuggestionDataView
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.`is` as shouldBe

fun Visitable<*>.assertSuggestionDataView(
    suggestion: AceSearchProductModel.Suggestion
) {
    assertThat(this, instanceOf(SuggestionDataView::class.java))

    val suggestionDataView = this as SuggestionDataView
    assertThat(suggestionDataView.text, shouldBe(suggestion.text))
    assertThat(suggestionDataView.query, shouldBe(suggestion.query))
    assertThat(suggestionDataView.suggestion, shouldBe(suggestion.suggestion))
}