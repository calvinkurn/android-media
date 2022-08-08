package com.tokopedia.sellerorder.filter.presentation.model

data class SomFilterCancelWrapper(
    val orderStatusIdList: List<Int>,
    val somFilterUiModelList: List<SomFilterUiModel>
)