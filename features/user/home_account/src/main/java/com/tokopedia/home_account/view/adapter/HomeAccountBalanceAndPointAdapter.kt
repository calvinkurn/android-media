package com.tokopedia.home_account.view.adapter

import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.home_account.view.adapter.delegate.HomeAccountBalanceAndPointDelegate
import com.tokopedia.home_account.view.adapter.delegate.HomeAccountBalanceAndPointShimmerDelegate
import com.tokopedia.home_account.view.adapter.uimodel.BalanceAndPointShimmerUiModel
import com.tokopedia.home_account.view.adapter.uimodel.BalanceAndPointUiModel
import com.tokopedia.home_account.view.listener.BalanceAndPointListener
import com.tokopedia.home_account.view.mapper.UiModelMapper

class HomeAccountBalanceAndPointAdapter(
    private val balanceAndPointListener: BalanceAndPointListener,
) : BaseCommonAdapter() {

    init {
        delegatesManager.addDelegate(HomeAccountBalanceAndPointDelegate(balanceAndPointListener))
        delegatesManager.addDelegate(HomeAccountBalanceAndPointShimmerDelegate())
    }

    var tempFourthItem: Any? = null

    fun addItemWallet(item: Any) {
        if (itemCount < 3) {
            addItemAndAnimateChanges(item)
        } else {
            tempFourthItem = item
        }
    }

    fun changeItemToSuccessBySameId(balanceAndPointUiModel: BalanceAndPointUiModel) {
        if (!changeFourthItemToSuccessBySameId(balanceAndPointUiModel)) {
            val items = getItems().toMutableList()
            items.forEach {
                when (it) {
                    is BalanceAndPointUiModel -> {
                        if (it.id == balanceAndPointUiModel.id) {
                            val position = items.indexOf(it)
                            removeItemAt(position)
                            addItem(position, balanceAndPointUiModel)
                            notifyItemChanged(position)
                        }
                    }
                    is BalanceAndPointShimmerUiModel -> {
                        if (it.id == balanceAndPointUiModel.id) {
                            val position = items.indexOf(it)
                            removeItemAt(position)
                            addItem(position, balanceAndPointUiModel)
                            notifyItemChanged(position)
                        }
                    }
                }
            }
        }
    }

    private fun changeFourthItemToSuccessBySameId(balanceAndPointUiModel: BalanceAndPointUiModel): Boolean {
        when (tempFourthItem) {
            is BalanceAndPointUiModel -> {
                (tempFourthItem as BalanceAndPointUiModel).also {
                    if (it.id == balanceAndPointUiModel.id) {
                        tempFourthItem = balanceAndPointUiModel
                        return true
                    }
                }
            }
            is BalanceAndPointShimmerUiModel -> {
                (tempFourthItem as BalanceAndPointShimmerUiModel).also {
                    if (it.id == balanceAndPointUiModel.id) {
                        tempFourthItem = balanceAndPointUiModel
                        return true
                    }
                }
            }
        }
        return false
    }

    fun changeItemToFailedById(id: String) {
        if (!changeFourthItemToFailedById(id)) {
            val items = getItems().toMutableList()
            items.forEach {
                when (it) {
                    is BalanceAndPointUiModel -> {
                        if (it.id == id) {
                            val position = items.indexOf(it)
                            it.isFailed = true
                            notifyItemChanged(position)
                        }
                    }
                    is BalanceAndPointShimmerUiModel -> {
                        if (it.id == id) {
                            val position = items.indexOf(it)
                            removeItemAt(position)
                            addItem(position, UiModelMapper.getBalanceAndPointUiModel(it, true))
                            notifyItemChanged(position)
                        }
                    }
                }
            }
        }
    }

    private fun changeFourthItemToFailedById(id: String): Boolean {
        when (tempFourthItem) {
            is BalanceAndPointUiModel -> {
                (tempFourthItem as BalanceAndPointUiModel).also {
                    if (it.id == id) {
                        it.isFailed = true
                        return true
                    }
                }
            }
            is BalanceAndPointShimmerUiModel -> {
                (tempFourthItem as BalanceAndPointShimmerUiModel).also {
                    if (it.id == id) {
                        tempFourthItem = UiModelMapper.getBalanceAndPointUiModel(it, true)
                        return true
                    }
                }
            }
        }
        return false
    }

    fun removeById(id: String) {
        if (!removeFourthItemById(id)) {
            val items = getItems().toMutableList()
            items.forEach {
                when (it) {
                    is BalanceAndPointUiModel -> {
                        if (it.id == id) {
                            val position = items.indexOf(it)
                            removeItemAt(position)
                            notifyItemRemoved(position)
                            tempFourthItem?.let { temp ->
                                addItemAndAnimateChanges(temp)
                                tempFourthItem = null
                            }
                        }
                    }
                    is BalanceAndPointShimmerUiModel -> {
                        if (it.id == id) {
                            val position = items.indexOf(it)
                            removeItemAt(position)
                            notifyItemRemoved(position)
                            tempFourthItem?.let { temp ->
                                addItemAndAnimateChanges(temp)
                                tempFourthItem = null
                            }
                        }
                    }
                }
            }
        }
    }

    private fun removeFourthItemById(id: String): Boolean {
        when (tempFourthItem) {
            is BalanceAndPointUiModel -> {
                (tempFourthItem as BalanceAndPointUiModel).also {
                    if (it.id == id) {
                        tempFourthItem = null
                        return true
                    }
                }
            }
            is BalanceAndPointShimmerUiModel -> {
                (tempFourthItem as BalanceAndPointShimmerUiModel).also {
                    if (it.id == id) {
                        tempFourthItem = null
                        return true
                    }
                }
            }
        }
        return false
    }

    fun changeItemToShimmer(balanceAndPointShimmerUiModel: BalanceAndPointShimmerUiModel) {
        val items = getItems().toMutableList()
        items.forEach {
            when (it) {
                is BalanceAndPointUiModel -> {
                    if (it.id == balanceAndPointShimmerUiModel.id) {
                        val position = items.indexOf(it)
                        removeItemAt(position)
                        addItem(position, balanceAndPointShimmerUiModel)
                        notifyItemChanged(position)
                    }
                }
            }
        }
    }
}
