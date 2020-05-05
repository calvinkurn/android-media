package com.tokopedia.product.manage.oldlist.view.presenter

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.product.manage.oldlist.constant.option.SortProductOption
import com.tokopedia.product.manage.oldlist.data.model.ProductManageSortModel
import java.util.*
import javax.inject.Inject

class ProductManageSortViewModel @Inject constructor() : ViewModel() {

    /**
     * https://developer.android.com/topic/libraries/architecture/livedata#transform_livedata
     */
    private val listOfSort = MutableLiveData<List<ProductManageSortModel>>()
    val listOfSortData = listOfSort

    fun getListSortManageProduct(context: Context, stringArray: Array<String>) {
        val productManageSortModels = ArrayList<ProductManageSortModel>()
        productManageSortModels.addAll(
                stringArray.map {
                    val productManageSortModel = ProductManageSortModel()
                    when (it) {
                        context.getString(com.tokopedia.product.manage.R.string.sort_position) -> {
                            productManageSortModel.sortId = SortProductOption.POSITION
                            productManageSortModel.titleSort = it
                        }
                        context.getString(com.tokopedia.product.manage.R.string.sort_new_product) -> {
                            productManageSortModel.sortId = SortProductOption.NEW_PRODUCT
                            productManageSortModel.titleSort = it
                        }
                        context.getString(com.tokopedia.product.manage.R.string.sort_last) -> {
                            productManageSortModel.sortId = SortProductOption.LAST_UPDATE
                            productManageSortModel.titleSort = it
                        }
                        context.getString(com.tokopedia.product.manage.R.string.sort_product_name) -> {
                            productManageSortModel.sortId = SortProductOption.PRODUCT_NAME
                            productManageSortModel.titleSort = it
                        }
                        context.getString(com.tokopedia.product.manage.R.string.sort_most_viewed) -> {
                            productManageSortModel.sortId = SortProductOption.MOST_VIEW
                            productManageSortModel.titleSort = it
                        }
                        context.getString(com.tokopedia.product.manage.R.string.sort_most_discussed) -> {
                            productManageSortModel.sortId = SortProductOption.MOST_TALK
                            productManageSortModel.titleSort = it
                        }
                        context.getString(com.tokopedia.product.manage.R.string.sort_most_reviewed) -> {
                            productManageSortModel.sortId = SortProductOption.MOST_REVIEW
                            productManageSortModel.titleSort = it
                        }
                        context.getString(com.tokopedia.product.manage.R.string.sort_most_buy) -> {
                            productManageSortModel.sortId = SortProductOption.MOST_BUY
                            productManageSortModel.titleSort = it
                        }
                        context.getString(com.tokopedia.product.manage.R.string.sort_lowest_price) -> {
                            productManageSortModel.sortId = SortProductOption.LOWEST_PRICE
                            productManageSortModel.titleSort = it
                        }
                        context.getString(com.tokopedia.product.manage.R.string.sort_highest_price) -> {
                            productManageSortModel.sortId = SortProductOption.HIGHEST_PRICE
                            productManageSortModel.titleSort = it
                        }
                        else -> {
                            productManageSortModel.sortId = SortProductOption.POSITION
                            productManageSortModel.titleSort = it
                        }
                    }
                    productManageSortModel
                })

        listOfSort.value = productManageSortModels

    }
}