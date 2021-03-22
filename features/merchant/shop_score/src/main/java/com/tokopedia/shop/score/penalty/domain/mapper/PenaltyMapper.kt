package com.tokopedia.shop.score.penalty.domain.mapper

import com.tokopedia.shop.score.common.ShopScoreConstant.CASH_ADVANCE
import com.tokopedia.shop.score.common.ShopScoreConstant.PRODUCT_DUPLICATE
import com.tokopedia.shop.score.common.ShopScoreConstant.PRODUCT_VIOLATION
import com.tokopedia.shop.score.common.ShopScoreConstant.TRANSACTION_MANIPULATION
import com.tokopedia.shop.score.penalty.presentation.model.BasePenaltyPage
import com.tokopedia.shop.score.penalty.presentation.model.ItemCardShopPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemDetailPenaltyFilterUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import javax.inject.Inject

class PenaltyMapper @Inject constructor() {

    fun mapToPenaltyVisitableDummy(): List<BasePenaltyPage> {
        return mutableListOf<BasePenaltyPage>().apply {
            add(mapToCardShopPenalty())
            add(mapToDetailPenaltyFilter())
            addAll(mapToItemPenaltyList())
        }
    }

    private fun mapToCardShopPenalty(): ItemCardShopPenaltyUiModel {
        return ItemCardShopPenaltyUiModel(totalPenalty = 9, hasPenalty = true, deductionPoints = -5)
    }

    private fun mapToDetailPenaltyFilter(): ItemDetailPenaltyFilterUiModel {
        return ItemDetailPenaltyFilterUiModel(periodDetail = "5 july - 6 sept", itemSortFilterWrapperList = mapToSortFilterPenalty())
    }

    private fun mapToSortFilterPenalty(): List<ItemDetailPenaltyFilterUiModel.ItemSortFilterWrapper> {
        return mutableListOf<ItemDetailPenaltyFilterUiModel.ItemSortFilterWrapper>().apply {
            add(ItemDetailPenaltyFilterUiModel.ItemSortFilterWrapper(
                    sortFilterItem = SortFilterItem(
                            title = CASH_ADVANCE,
                            type = ChipsUnify.TYPE_NORMAL,
                            size = ChipsUnify.SIZE_SMALL
                    ),
                    isSelected = false
            ))
            add(ItemDetailPenaltyFilterUiModel.ItemSortFilterWrapper(
                    sortFilterItem = SortFilterItem(
                            title = PRODUCT_DUPLICATE,
                            type = ChipsUnify.TYPE_NORMAL,
                            size = ChipsUnify.SIZE_SMALL
                    ),
                    isSelected = false
            ))
            add(ItemDetailPenaltyFilterUiModel.ItemSortFilterWrapper(
                    sortFilterItem = SortFilterItem(
                            title = PRODUCT_VIOLATION,
                            type = ChipsUnify.TYPE_NORMAL,
                            size = ChipsUnify.SIZE_SMALL
                    ),
                    isSelected = false
            ))
            add(ItemDetailPenaltyFilterUiModel.ItemSortFilterWrapper(
                    sortFilterItem = SortFilterItem(
                            title = TRANSACTION_MANIPULATION,
                            type = ChipsUnify.TYPE_NORMAL,
                            size = ChipsUnify.SIZE_SMALL
                    ),
                    isSelected = false
            ))
        }
    }

    private fun mapToItemPenaltyList(): List<ItemPenaltyUiModel> {
        return mutableListOf<ItemPenaltyUiModel>().apply {
            add(ItemPenaltyUiModel(statusPenalty = "Sedang berlangsung",
                    statusDate = "27 jan 2021", endDate = "8 Mei 2021",
                    transactionPenalty = "Cash advance",
                    descPenalty = "INV/20210126/XX/V/553738285",
                    colorPenalty = "#E02954"
            ))
            add(ItemPenaltyUiModel(statusPenalty = "Penalti selesai",
                    statusDate = "25 jan 2021", endDate = "10 Mei 2021",
                    transactionPenalty = "Pelanggaran Produk",
                    descPenalty = "INV/20210126/XX/V/553738285",
                    colorPenalty = "#8D96AA"
            ))
            add(ItemPenaltyUiModel(statusPenalty = "Sedang berlangsung",
                    statusDate = "27 jan 2021", endDate = "30 juli 2021",
                    transactionPenalty = "Manipulasi transaksi",
                    descPenalty = "INV/20210126/XX/V/553738285",
                    colorPenalty = "#E02954"
            ))
            add(ItemPenaltyUiModel(statusPenalty = "Poin belum dipotong",
                    statusDate = "17 jan 2021", endDate = "24 juli 2021",
                    transactionPenalty = "Duplikasi produk",
                    descPenalty = "INV/20210126/XX/V/553738285",
                    colorPenalty = "#E02954"
            ))
        }
    }
}