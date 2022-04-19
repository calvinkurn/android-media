package com.tokopedia.sellerorder.filter.presentation.model

data class SomFilterCancelWrapper(val orderStatus: String,
                                  val orderStatusIdList: List<Int>,
                                  val somFilterUiModelList: List<SomFilterUiModel>,
                                  val filterDate: String,
                                  val requestCancelFilterApplied: Boolean
)