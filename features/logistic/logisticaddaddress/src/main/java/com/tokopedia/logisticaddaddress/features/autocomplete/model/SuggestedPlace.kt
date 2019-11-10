package com.tokopedia.logisticaddaddress.features.autocomplete.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.logisticaddaddress.domain.model.autocomplete.MatchedSubstringsItem
import com.tokopedia.logisticaddaddress.domain.model.autocomplete.StructuredFormatting
import com.tokopedia.logisticaddaddress.domain.model.autocomplete.TermsItem
import com.tokopedia.logisticaddaddress.features.autocomplete.AutoCompleteAdapter

data class SuggestedPlace(
        val mainText: String = "",
        val secondaryText: String = "",
        val placeId: String = "") : AutoCompleteAdapter.AutoCompleteVisitable