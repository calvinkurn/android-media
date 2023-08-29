package com.tokopedia.feedplus.browse.data

import com.tokopedia.content.common.model.FeedXHeaderRequestFields
import com.tokopedia.content.common.usecase.FeedXHeaderUseCase
import com.tokopedia.content.common.usecase.GetPlayWidgetSlotUseCase
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseSlot
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.feedplus.domain.usecase.FeedXHomeUseCase
import javax.inject.Inject

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseRepositoryImpl @Inject constructor(
    private val feedXHeaderUseCase: FeedXHeaderUseCase,
    private val feedXHomeUseCase: FeedXHomeUseCase,
    private val playWidgetSlotUseCase: GetPlayWidgetSlotUseCase,
    private val mapper: FeedBrowseMapper
) : FeedBrowseRepository {

    override suspend fun getTitle(): String {
        feedXHeaderUseCase.setRequestParams(
            FeedXHeaderUseCase.createParam(
                listOf(
                    FeedXHeaderRequestFields.BROWSE.value
                )
            )
        )
        val headerData = feedXHeaderUseCase.executeOnBackground()
        return headerData.feedXHeaderData.data.browse.title
    }

    override suspend fun getSlots(): List<FeedBrowseSlot> {
        return mapper.mapSlots()
    }

    override suspend fun getWidgets(type: String): List<FeedBrowseUiModel> {
        return mapper.mapWidgets(type)
    }
}
