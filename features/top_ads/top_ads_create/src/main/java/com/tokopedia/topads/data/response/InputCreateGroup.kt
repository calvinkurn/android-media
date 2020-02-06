package com.tokopedia.topads.data.response


open class InputCreateGroup(

	var keywords: List<KeywordsItem> = listOf(),

	var shopID: String = "",

	var group: Group = Group()
)