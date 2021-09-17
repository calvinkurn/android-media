package com.tokopedia.home_account.view.adapter

import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.home_account.view.adapter.delegate.HomeAccountBalanceAndPointDelegate
import com.tokopedia.home_account.view.adapter.delegate.HomeAccountBalanceAndPointShimmerDelegate
import com.tokopedia.home_account.view.adapter.uimodel.BalanceAndPointShimmerUiModel
import com.tokopedia.home_account.view.adapter.uimodel.BalanceAndPointUiModel
import com.tokopedia.home_account.view.listener.BalanceAndPointListener

class HomeAccountBalanceAndPointAdapter(
    private val balanceAndPointListener: BalanceAndPointListener,
) : BaseCommonAdapter() {

    init {
        delegatesManager.addDelegate(HomeAccountBalanceAndPointDelegate(balanceAndPointListener))
        delegatesManager.addDelegate(HomeAccountBalanceAndPointShimmerDelegate())
    }

    private val listBalanceAndPoint: ArrayList<BalanceAndPointUiModel> = arrayListOf()
    var counter = 0

    fun displayShimmer() {
        val list: MutableList<BalanceAndPointShimmerUiModel> = mutableListOf()
        for (i in 1..3) {
            list.add(BalanceAndPointShimmerUiModel(true))
        }
        clearAllItemsAndAnimateChanges()
        addItemsAndAnimateChanges(list)
    }

    fun setupPlaceholderBalanceAndPoints(items: List<BalanceAndPointUiModel>) {
        listBalanceAndPoint.clear()
        listBalanceAndPoint.addAll(items)
        counter = items.size
    }

    @Synchronized
    fun setupBalanceAndPointsWithData(item : BalanceAndPointUiModel) {
        val position = getPositionBalanceAndPoint(item.id)
        if(position != null) {
            listBalanceAndPoint[position] = item
        }
        counter--
        checkIfAllWidgetIsFinished()
    }

    @Synchronized
    fun changeItemToFailed(id: String) {
        val position = getPositionBalanceAndPoint(id)
        if(position != null) {
            listBalanceAndPoint[position].isFailed = true
        }
        counter--
        checkIfAllWidgetIsFinished()
    }

    @Synchronized
    fun removeById(id: String): Boolean {
        val position = getPositionBalanceAndPoint(id)
        if(position != null) {
            listBalanceAndPoint.removeAt(position)
            return true
        }
        return false
    }

    @Synchronized
    fun checkIfAllWidgetIsFinished() {
        if(counter == ZERO_COUNTER) {
            clearAllItemsAndAnimateChanges()
            addItemsAndAnimateChanges(listBalanceAndPoint)
            balanceAndPointListener.onZeroCounter()
        }
    }

    @Synchronized
    private fun getPositionBalanceAndPoint(id: String): Int? {
        for(i in 0..listBalanceAndPoint.size) {
            if(i < listBalanceAndPoint.size &&
                listBalanceAndPoint[i].id == id) {
                return i
            }
        }
        return null
    }

    companion object {
        private const val ZERO_COUNTER = 0
    }
}