package com.tokopedia.logisticaddaddress.features.autocomplete.model

import com.tokopedia.logisticaddaddress.domain.model.autocomplete.MatchedSubstringsItem
import com.tokopedia.logisticaddaddress.domain.model.autocomplete.StructuredFormatting
import com.tokopedia.logisticaddaddress.domain.model.autocomplete.TermsItem
import com.tokopedia.logisticaddaddress.features.autocomplete.AutoCompleteAdapter

data class AutoCompleteResultUi(
        val types: List<String> = emptyList(),
        val matchedSubstrings: List<MatchedSubstringsItem> = emptyList(),
        val terms: List<TermsItem> = emptyList(),
        val structuredFormatting: StructuredFormatting = StructuredFormatting(),
        val description: String = "",
        val placeId: String = "") : AutoCompleteAdapter.AutoCompleteVisitable