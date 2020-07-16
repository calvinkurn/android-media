package com.tokopedia.topads.dashboard.data.model.insight

data class Keyword(
	val box: Box? = null,
	val id: String? = null,
	val table: List<TableItem?>? = null,
	val tag: String? = null,
	val source: String? = null,
	val type: String? = null,
	val priceBid: Int? = null,
	val status: String? = null
)
