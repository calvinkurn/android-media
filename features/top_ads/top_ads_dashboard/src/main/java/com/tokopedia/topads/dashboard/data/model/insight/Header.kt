package com.tokopedia.topads.dashboard.data.model.insight

data class Header(
	val negative: Negative? = null,
	val btnAction: BtnAction? = null,
	val keyword: Keyword? = null,
	val bid: Bid? = null
)
