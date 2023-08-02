package com.tokopedia.shop.score.penalty.domain.mapper

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.common.ShopScorePrefManager
import com.tokopedia.shop.score.common.convertToFormattedDate
import com.tokopedia.shop.score.penalty.domain.response.GetTargetedTicker
import com.tokopedia.shop.score.penalty.domain.response.ShopPenaltySummaryTypeWrapper
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltyDetailResponse
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltySummary
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltyTypes
import com.tokopedia.shop.score.penalty.presentation.adapter.filter.BaseFilterPenaltyPage
import com.tokopedia.shop.score.penalty.presentation.fragment.ShopPenaltyPageType
import com.tokopedia.shop.score.penalty.presentation.model.BasePenaltyPage
import com.tokopedia.shop.score.penalty.presentation.model.ChipsFilterPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyInfoNotificationUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyPointCardUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltySubsectionUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyTickerUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemSortFilterPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyDataWrapper
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterDateUiModel
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterUiModel
import javax.inject.Inject
import kotlin.math.abs

class PenaltyMapper @Inject constructor(
    @ApplicationContext val context: Context?,
    private val shopScorePrefManager: ShopScorePrefManager
) {

    fun mapToPenaltyData(
        @ShopPenaltyPageType pageType: String,
        shopScorePenaltySummaryWrapper: ShopPenaltySummaryTypeWrapper,
        shopScorePenaltyDetailResponse: ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail,
        notYetDeductedPenalties: List<ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail.Result>?,
        sortBy: Int,
        typeIds: List<Int>,
        tickerList: List<GetTargetedTicker.TickerResponse>
    ): PenaltyDataWrapper {
        val penaltyTypes =
            shopScorePenaltySummaryWrapper.shopScorePenaltyTypesResponse?.result.orEmpty()
        return PenaltyDataWrapper(
            penaltyVisitableList = mapToItemVisitablePenaltyList(
                pageType, shopScorePenaltySummaryWrapper, shopScorePenaltyDetailResponse,
                notYetDeductedPenalties, typeIds, tickerList
            ),
            penaltyFilterList = mapToPenaltyFilterBottomSheet(
                shopScorePenaltyDetailResponse,
                penaltyTypes,
                sortBy,
                typeIds
            )
        )
    }

    private fun mapToCardShopPenalty(
        penaltySummary: ShopScorePenaltySummary.Result,
        penaltyDetail: ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail
    ): ItemPenaltyPointCardUiModel {
        return ItemPenaltyPointCardUiModel(
            result = penaltySummary,
            date = getFormattedDate(penaltyDetail)
        )
    }

    private fun mapToNotYetDeductedItem(list: List<ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail.Result>): ItemPenaltyInfoNotificationUiModel {
        val latestPenaltyId = shopScorePrefManager.getLatestOngoingPenaltyId()
        val isSharedPrefReady =
            latestPenaltyId == null || latestPenaltyId != list.firstOrNull()?.shopPenaltyID
        val shouldShowDot = isSharedPrefReady && list.size > Int.ZERO
        return ItemPenaltyInfoNotificationUiModel(
            notificationCount = list.size,
            shouldShowDot = shouldShowDot,
            latestOngoingId = list.firstOrNull()?.shopPenaltyID
        )
    }

    private fun mapToDetailPenaltyFilter(
        @ShopPenaltyPageType pageType: String,
        penaltyDetail: ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail
    ): ItemPenaltySubsectionUiModel {
        return ItemPenaltySubsectionUiModel(
            date = getFormattedDate(penaltyDetail),
            pageType = pageType
        )
    }

    private fun getFormattedDate(penaltyDetail: ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail): String {
        return "${
            penaltyDetail.startDate.convertToFormattedDate().orEmpty()
        } - ${penaltyDetail.endDate.convertToFormattedDate().orEmpty()}"
    }

    private fun mapToSortFilterPenalty(
        penaltyTypes: List<ShopScorePenaltyTypes.Result>,
        typeIds: List<Int>
    ): List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper> {
        return mutableListOf<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>().apply {
            penaltyTypes.forEachIndexed { index, result ->
                if (index < MAX_SHOWN_FILTER_CHIPS || typeIds.contains(result.id.toIntOrZero())) {
                    add(
                        result.mapToSortFilterWrapper(typeIds)
                    )
                }
            }
        }
    }

    fun mapToItemPenaltyList(
        shopScorePenaltyDetailResponse: ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail,
        @ShopPenaltyPageType pageType: String
    ): Triple<List<ItemPenaltyUiModel>, Boolean, Boolean> {

        val itemPenaltyList = mutableListOf<ItemPenaltyUiModel>().apply {
            shopScorePenaltyDetailResponse.result.forEach {
                val colorTypePenalty = when (pageType) {
                    ShopPenaltyPageType.NOT_YET_DEDUCTED, ShopPenaltyPageType.HISTORY -> {
                        com.tokopedia.unifyprinciples.R.color.Unify_NN500
                    }
                    ShopPenaltyPageType.ONGOING -> {
                        com.tokopedia.unifyprinciples.R.color.Unify_RN600
                    }
                    else -> {
                        null
                    }
                }

                val (prefixDatePenaltyDetail, endDateText) = when (pageType) {
                    ShopPenaltyPageType.ONGOING -> {
                        Pair(
                            ShopScoreConstant.ACTIVE_PENALTY_DETAIL,
                            "${ShopScoreConstant.FINISHED_IN} ${
                                DateFormatUtils.formatDate(
                                    ShopScoreConstant.PATTERN_PENALTY_DATE_PARAM,
                                    ShopScoreConstant.PATTERN_PENALTY_DATE_TEXT,
                                    it.penaltyExpirationDate
                                )
                            }"
                        )
                    }
                    ShopPenaltyPageType.HISTORY -> {
                        Pair(
                            ShopScoreConstant.SINCE_FINISH_PENALTY_DETAIL,
                            "${ShopScoreConstant.SINCE} ${
                                DateFormatUtils.formatDate(
                                    ShopScoreConstant.PATTERN_PENALTY_DATE_PARAM,
                                    ShopScoreConstant.PATTERN_PENALTY_DATE_TEXT,
                                    it.penaltyExpirationDate
                                )
                            }"
                        )
                    }
                    ShopPenaltyPageType.NOT_YET_DEDUCTED -> {
                        Pair(
                            ShopScoreConstant.START_ACTIVE_PENALTY_DETAIL,
                            "${ShopScoreConstant.START} ${
                                DateFormatUtils.formatDate(
                                    ShopScoreConstant.PATTERN_PENALTY_DATE_PARAM,
                                    ShopScoreConstant.PATTERN_PENALTY_DATE_TEXT,
                                    it.penaltyStartDate
                                )
                            }"
                        )
                    }
                    else -> Pair("", "")
                }

                val endDateDetail = when (pageType) {
                    ShopPenaltyPageType.ONGOING, ShopPenaltyPageType.HISTORY -> {
                        DateFormatUtils.formatDate(
                            ShopScoreConstant.PATTERN_PENALTY_DATE_PARAM,
                            ShopScoreConstant.PATTERN_PENALTY_DATE_TEXT,
                            it.penaltyExpirationDate
                        )
                    }
                    ShopPenaltyPageType.NOT_YET_DEDUCTED -> {
                        DateFormatUtils.formatDate(
                            ShopScoreConstant.PATTERN_PENALTY_DATE_PARAM,
                            ShopScoreConstant.PATTERN_PENALTY_DATE_TEXT,
                            it.penaltyStartDate
                        )
                    }
                    else -> ""
                }

                val statusPenaltyRes = when (pageType) {
                    ShopPenaltyPageType.NOT_YET_DEDUCTED -> R.string.title_penalty_not_yet_deducted
                    ShopPenaltyPageType.ONGOING -> R.string.title_penalty_ongoing
                    ShopPenaltyPageType.HISTORY -> R.string.title_penalty_done
                    else -> null
                }

                val descStatusPenaltyDetail = when (pageType) {
                    ShopPenaltyPageType.NOT_YET_DEDUCTED -> R.string.desc_point_have_not_been_deducted
                    ShopPenaltyPageType.ONGOING -> R.string.desc_on_going_status_penalty
                    ShopPenaltyPageType.HISTORY -> R.string.desc_done_status_penalty
                    else -> null
                }

                val scoreAbs = abs(it.score).toString()

                add(
                    ItemPenaltyUiModel(
                        statusPenalty = it.status,
                        statusPenaltyRes = statusPenaltyRes,
                        deductionPoint = scoreAbs,
                        startDate = DateFormatUtils.formatDate(
                            ShopScoreConstant.PATTERN_PENALTY_DATE_PARAM,
                            ShopScoreConstant.PATTERN_PENALTY_DATE_TEXT,
                            it.createTime
                        ),
                        endDate = endDateText,
                        endDateDetail = endDateDetail,
                        typePenalty = it.typeName,
                        invoicePenalty = it.invoiceNumber,
                        reasonPenalty = it.reason,
                        productName = mapPenaltyDetailToProductInfo(
                            it.penaltyTypeGroup,
                            it.productDetail
                        ),
                        colorPenalty = colorTypePenalty,
                        prefixDatePenalty = prefixDatePenaltyDetail,
                        descStatusPenalty = descStatusPenaltyDetail,
                        isOldPage = false,
                        pageType = pageType
                    )
                )
            }
        }

        return Triple(
            itemPenaltyList,
            shopScorePenaltyDetailResponse.hasPrev,
            shopScorePenaltyDetailResponse.hasNext
        )
    }

    private fun mapToItemVisitablePenaltyList(
        @ShopPenaltyPageType pageType: String,
        shopScorePenaltySummaryWrapper: ShopPenaltySummaryTypeWrapper,
        shopScorePenaltyDetailResponse: ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail,
        notYetDeductedPenalties: List<ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail.Result>?,
        typeIds: List<Int>,
        tickerList: List<GetTargetedTicker.TickerResponse>
    ): Triple<List<BasePenaltyPage>, Boolean, Boolean> {

        val visitablePenaltyPage =
            when(pageType) {
                ShopPenaltyPageType.ONGOING -> getMappedOngoingPenaltyVisitables(
                    shopScorePenaltySummaryWrapper,
                    shopScorePenaltyDetailResponse,
                    notYetDeductedPenalties,
                    typeIds,
                    tickerList,
                    pageType
                )
                ShopPenaltyPageType.HISTORY -> getMappedHistoryPenaltyVisitables(
                    shopScorePenaltySummaryWrapper,
                    shopScorePenaltyDetailResponse,
                    typeIds,
                    pageType
                )
                else -> getMappedNotYetDeductedPenaltyVisitables(
                    shopScorePenaltySummaryWrapper,
                    shopScorePenaltyDetailResponse,
                    typeIds,
                    pageType
                )
            }

        return Triple(
            visitablePenaltyPage,
            shopScorePenaltyDetailResponse.hasPrev,
            shopScorePenaltyDetailResponse.hasNext
        )
    }

    private fun getMappedOngoingPenaltyVisitables(
        shopScorePenaltySummaryWrapper: ShopPenaltySummaryTypeWrapper,
        shopScorePenaltyDetailResponse: ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail,
        notYetDeductedPenalties: List<ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail.Result>?,
        typeIds: List<Int>,
        tickerList: List<GetTargetedTicker.TickerResponse>,
        @ShopPenaltyPageType pageType: String
    ): MutableList<BasePenaltyPage> {
        return mutableListOf<BasePenaltyPage>().apply {
            tickerList.firstOrNull()?.let { ticker ->
                add(
                    ItemPenaltyTickerUiModel(
                        ticker.title,
                        ticker.content,
                        ticker.action?.label,
                        ticker.action?.webURL
                    )
                )
            }

            if (notYetDeductedPenalties != null) {
                add(mapToNotYetDeductedItem(notYetDeductedPenalties))
            }

            add(mapToDetailPenaltyFilter(ShopPenaltyPageType.ONGOING, shopScorePenaltyDetailResponse))

            val isHasPenalty = shopScorePenaltyDetailResponse.result.isNotEmpty()

            if (isHasPenalty) {
                shopScorePenaltySummaryWrapper.shopScorePenaltyTypesResponse?.result?.let {
                    add(
                        ItemSortFilterPenaltyUiModel(
                            itemSortFilterWrapperList = mapToSortFilterPenalty(
                                it,
                                typeIds
                            )
                        )
                    )
                }
            }

            shopScorePenaltySummaryWrapper.shopScorePenaltySummaryResponse?.result?.let {
                add(
                    mapToCardShopPenalty(it, shopScorePenaltyDetailResponse)
                )
            }

            val itemPenaltyFilterList = mapToItemPenaltyList(shopScorePenaltyDetailResponse, pageType).first
            addAll(itemPenaltyFilterList)
        }
    }

    private fun getMappedHistoryPenaltyVisitables(
        shopScorePenaltySummaryWrapper: ShopPenaltySummaryTypeWrapper,
        shopScorePenaltyDetailResponse: ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail,
        typeIds: List<Int>,
        @ShopPenaltyPageType pageType: String
    ): MutableList<BasePenaltyPage> {
        return mutableListOf<BasePenaltyPage>().apply {

            val isNoPenalty = shopScorePenaltyDetailResponse.result.isEmpty()

            if (!isNoPenalty) {
                shopScorePenaltySummaryWrapper.shopScorePenaltyTypesResponse?.result?.let {
                    add(
                        ItemSortFilterPenaltyUiModel(
                            itemSortFilterWrapperList = mapToSortFilterPenalty(
                                it,
                                typeIds
                            )
                        )
                    )
                }

                add(mapToDetailPenaltyFilter(ShopPenaltyPageType.HISTORY, shopScorePenaltyDetailResponse))
            }

            val itemPenaltyFilterList = mapToItemPenaltyList(shopScorePenaltyDetailResponse, pageType).first
            addAll(itemPenaltyFilterList)
        }
    }

    private fun getMappedNotYetDeductedPenaltyVisitables(
        shopScorePenaltySummaryWrapper: ShopPenaltySummaryTypeWrapper,
        shopScorePenaltyDetailResponse: ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail,
        typeIds: List<Int>,
        @ShopPenaltyPageType pageType: String
    ): MutableList<BasePenaltyPage> {
        return mutableListOf<BasePenaltyPage>().apply {

            val isNoPenalty = shopScorePenaltyDetailResponse.result.isEmpty()

            if (!isNoPenalty) {
                shopScorePenaltySummaryWrapper.shopScorePenaltyTypesResponse?.result?.let {
                    add(
                        ItemSortFilterPenaltyUiModel(
                            itemSortFilterWrapperList = mapToSortFilterPenalty(
                                it,
                                typeIds
                            )
                        )
                    )
                }
            }

            val itemPenaltyFilterList = mapToItemPenaltyList(shopScorePenaltyDetailResponse, pageType).first
            addAll(itemPenaltyFilterList)
        }
    }

    private fun mapToPenaltyFilterBottomSheet(
        shopScorePenaltyDetailResponse: ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail,
        penaltyTypes: List<ShopScorePenaltyTypes.Result>,
        sortBy: Int,
        typeIds: List<Int>
    ): List<BaseFilterPenaltyPage> {
        return mutableListOf<BaseFilterPenaltyPage>().apply {
            val sortFilterChipList = mapToChipsSortFilter(sortBy)
            add(
                PenaltyFilterUiModel(
                    title = ShopScoreConstant.TITLE_SORT,
                    isDividerVisible = true,
                    chipsFilterList = sortFilterChipList,
                    shownFilterList = sortFilterChipList
                )
            )

            add(
                PenaltyFilterDateUiModel(
                    startDate = shopScorePenaltyDetailResponse.startDate,
                    endDate = shopScorePenaltyDetailResponse.endDate,
                    defaultStartDate = shopScorePenaltyDetailResponse.defaultStartDate,
                    defaultEndDate = shopScorePenaltyDetailResponse.defaultEndDate,
                    initialStartDate = shopScorePenaltyDetailResponse.startDate,
                    initialEndDate = shopScorePenaltyDetailResponse.endDate,
                    completeDate = "${
                        shopScorePenaltyDetailResponse.startDate.convertToFormattedDate().orEmpty()
                    } - ${
                        shopScorePenaltyDetailResponse.endDate.convertToFormattedDate().orEmpty()
                    }"
                )
            )

            val filterTypeChipList = mapToChipsTypePenaltyFilter(penaltyTypes, typeIds)
            add(
                PenaltyFilterUiModel(
                    title = ShopScoreConstant.TITLE_TYPE_PENALTY,
                    chipsFilterList = filterTypeChipList,
                    shownFilterList = filterTypeChipList.take(MAX_SHOWN_FILTER_CHIPS)
                )
            )
        }
    }

    fun mapToChipsSortFilter(sortBy: Int): List<ChipsFilterPenaltyUiModel> {
        return mutableListOf<ChipsFilterPenaltyUiModel>().apply {
            add(
                ChipsFilterPenaltyUiModel(
                    title = ShopScoreConstant.SORT_LATEST,
                    isSelected = sortBy == ShopScoreConstant.SORT_LATEST_VALUE,
                    value = ShopScoreConstant.SORT_LATEST_VALUE
                )
            )
            add(
                ChipsFilterPenaltyUiModel(
                    title = ShopScoreConstant.SORT_OLDEST,
                    isSelected = sortBy == ShopScoreConstant.SORT_OLDEST_VALUE,
                    value = ShopScoreConstant.SORT_OLDEST_VALUE
                )
            )
        }
    }

    private fun mapToChipsTypePenaltyFilter(
        penaltyTypes: List<ShopScorePenaltyTypes.Result>,
        typeIds: List<Int>
    ): List<ChipsFilterPenaltyUiModel> {
        return mutableListOf<ChipsFilterPenaltyUiModel>().apply {
            penaltyTypes.map {
                add(
                    ChipsFilterPenaltyUiModel(
                        it.name,
                        isSelected = typeIds.contains(it.id.toIntOrZero()),
                        value = it.id.toIntOrZero()
                    )
                )
            }
        }
    }

    fun mapToSortFilterItemFromPenaltyList(penaltyFilterList: List<BaseFilterPenaltyPage>): List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper> {
        val mapItemSortFilterWrapper =
            mutableListOf<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>()
        penaltyFilterList.filterIsInstance<PenaltyFilterUiModel>()
            .find { it.title == ShopScoreConstant.TITLE_TYPE_PENALTY }?.chipsFilterList?.forEachIndexed { index, chipsFilterPenaltyUiModel ->
                if (index < MAX_SHOWN_FILTER_CHIPS || chipsFilterPenaltyUiModel.isSelected) {
                    mapItemSortFilterWrapper.add(
                        ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper(
                            title = chipsFilterPenaltyUiModel.title,
                            isSelected = chipsFilterPenaltyUiModel.isSelected,
                            idFilter = chipsFilterPenaltyUiModel.value
                        )
                    )
                }
            }
        return mapItemSortFilterWrapper
    }

    private fun mapPenaltyDetailToProductInfo(
        penaltyTypeGroup: Int,
        productDetail: ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail.Result.ProductDetail
    ): String? {
        return if (penaltyTypeGroup == ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail.Result.PENALTY_TYPE_PRODUCT) {
            productDetail.name.takeIf { it.isNotBlank() }
        } else {
            null
        }
    }

    fun transformUpdateFilterSelected(
        isMultiple: Boolean,
        titleFilter: String,
        updateChipsSelected: Boolean,
        position: Int,
        penaltyFilterUiModel: MutableList<BaseFilterPenaltyPage>,
        itemSortFilterWrapperList: MutableList<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>
    ): Pair<MutableList<BaseFilterPenaltyPage>, MutableList<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>> {
        penaltyFilterUiModel.onEach { model ->
            if (model is PenaltyFilterUiModel) {
                model.takeIf { it.title == titleFilter }?.run {
                    var selectedId: Int? = null
                    shownFilterList.mapIndexed { index, chipsFilterPenaltyUiModel ->
                        if (index == position) {
                            selectedId = chipsFilterPenaltyUiModel.value
                            if (isMultiple) {
                                chipsFilterPenaltyUiModel.isSelected = !updateChipsSelected
                                itemSortFilterWrapperList.getOrNull(index)?.isSelected = !updateChipsSelected
                            } else {
                                chipsFilterPenaltyUiModel.isSelected = true
                                itemSortFilterWrapperList.getOrNull(index)?.isSelected = true
                            }
                        } else {
                            if (!isMultiple) {
                                chipsFilterPenaltyUiModel.isSelected = false
                                itemSortFilterWrapperList.getOrNull(index)?.isSelected = false
                            }
                        }
                    }
                    chipsFilterList.map {
                        if (selectedId == it.value) {
                            if (isMultiple) {
                                it.isSelected = !updateChipsSelected
                            } else {
                                it.isSelected = true
                            }
                        } else {
                            if (!isMultiple) {
                                it.isSelected = false
                            }
                        }
                    }
                }
            }
        }
        return Pair(penaltyFilterUiModel, itemSortFilterWrapperList)
    }

    fun transformUpdateSortFilterSelected(
        penaltyFilterUiModel: MutableList<BaseFilterPenaltyPage>,
        itemSortFilterWrapperList: MutableList<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>,
        titleFilter: String,
        updateChipsSelected: Boolean
    ): Triple<MutableList<BaseFilterPenaltyPage>, MutableList<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>, List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>> {
        penaltyFilterUiModel.filterIsInstance<PenaltyFilterUiModel>().find { it.title == ShopScoreConstant.TITLE_TYPE_PENALTY }?.run {
            chipsFilterList.mapIndexed { index, chipsFilterPenaltyUiModel ->
                if (chipsFilterPenaltyUiModel.title == titleFilter) {
                    chipsFilterPenaltyUiModel.isSelected = !updateChipsSelected
                    itemSortFilterWrapperList.find { it.title == titleFilter }?.isSelected = !updateChipsSelected
                }
            }

            shownFilterList = mutableListOf<ChipsFilterPenaltyUiModel>().apply {
                chipsFilterList.forEachIndexed { index, chipsFilterPenaltyUiModel ->
                    if (index < MAX_SHOWN_FILTER_CHIPS || chipsFilterPenaltyUiModel.isSelected) {
                        add(chipsFilterPenaltyUiModel)
                    }
                }
            }
        }
        return Triple(
            penaltyFilterUiModel,
            itemSortFilterWrapperList,
            getShownSortFilterChips(itemSortFilterWrapperList)
        )
    }

    fun transformUpdateSortFilterSelected(
        penaltyFilterUiModel: MutableList<BaseFilterPenaltyPage>,
        typeIds: List<Int>
    ):
        Triple<List<BaseFilterPenaltyPage>, List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>, List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>> {
        var itemSortFilterWrapperList = mutableListOf<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>()
        penaltyFilterUiModel.filterIsInstance<PenaltyFilterUiModel>().find { it.title == ShopScoreConstant.TITLE_TYPE_PENALTY }?.run {
            chipsFilterList.mapIndexed { index, chipsFilterPenaltyUiModel ->
                val isFilterTypeSelected = typeIds.contains(chipsFilterPenaltyUiModel.value)
                chipsFilterPenaltyUiModel.isSelected = isFilterTypeSelected
            }

            shownFilterList = mutableListOf<ChipsFilterPenaltyUiModel>().apply {
                chipsFilterList.forEachIndexed { index, chipsFilterPenaltyUiModel ->
                    if (index < MAX_SHOWN_FILTER_CHIPS || chipsFilterPenaltyUiModel.isSelected) {
                        add(chipsFilterPenaltyUiModel)
                    }
                }
            }
            itemSortFilterWrapperList = chipsFilterList.map {
                ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper(
                    title = it.title,
                    isSelected = it.isSelected,
                    idFilter = it.value
                )
            }.toMutableList()
        }
        return Triple(penaltyFilterUiModel, itemSortFilterWrapperList, getShownSortFilterChips(itemSortFilterWrapperList))
    }

    fun transformDateFilter(
        penaltyFilterUiModel: MutableList<BaseFilterPenaltyPage>,
        startDate: String,
        endDate: String,
        date: String
    ): MutableList<BaseFilterPenaltyPage> {
        penaltyFilterUiModel.forEach { model ->
            if (model is PenaltyFilterDateUiModel) {
                model.startDate = startDate
                model.endDate = endDate
                model.completeDate = date
            }
        }
        return penaltyFilterUiModel
    }

    fun getChipsFilterList(
        titleFilter: String,
        penaltyFilterUiModel: MutableList<BaseFilterPenaltyPage>,
    ): List<ChipsFilterPenaltyUiModel> {
        return penaltyFilterUiModel.filterIsInstance<PenaltyFilterUiModel>()
            .find { it.title == titleFilter }?.chipsFilterList.orEmpty()
    }

    private fun getShownSortFilterChips(
        filterList: List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>
    ): List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper> {
        return mutableListOf<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>().apply {
            filterList.forEachIndexed { index, itemSortFilterWrapper ->
                if (index < MAX_SHOWN_FILTER_CHIPS || itemSortFilterWrapper.isSelected) {
                    add(itemSortFilterWrapper)
                }
            }
        }
    }

    private fun ShopScorePenaltyTypes.Result.mapToSortFilterWrapper(typeIds: List<Int>): ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper {
        return ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper(
            title = name,
            isSelected = typeIds.contains(id.toIntOrZero()),
            idFilter = id.toIntOrZero()
        )
    }

    companion object {
        const val MAX_SHOWN_FILTER_CHIPS = 4
    }

}
