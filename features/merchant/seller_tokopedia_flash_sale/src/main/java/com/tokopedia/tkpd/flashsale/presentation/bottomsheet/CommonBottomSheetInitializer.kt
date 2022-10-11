package com.tokopedia.tkpd.flashsale.presentation.bottomsheet

import android.content.Context
import androidx.annotation.ArrayRes
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.campaign.components.bottomsheet.selection.multiple.MultipleSelectionBottomSheet
import com.tokopedia.campaign.components.bottomsheet.selection.single.SingleSelectionBottomSheet
import com.tokopedia.campaign.components.bottomsheet.selection.entity.MultipleSelectionItem
import com.tokopedia.campaign.components.bottomsheet.selection.entity.SingleSelectionItem
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.Category
import java.lang.Exception
import javax.inject.Inject

class CommonBottomSheetInitializer @Inject constructor(@ApplicationContext private val context: Context) {

    private fun getResourceList(
        @ArrayRes resId: Int,
        @ArrayRes contentResId: Int
    ): List<Pair<String, String>> {
        return try {
            val ids = context.resources.getStringArray(resId).toList()
            val items = context.resources.getStringArray(contentResId).toList()
            val outputList = ids.mapIndexed { index, id ->
                Pair(id, items.getOrNull(index).orEmpty())
            }
            outputList
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun getResourceListSingle(
        @ArrayRes resId: Int,
        @ArrayRes contentResId: Int
    ): ArrayList<SingleSelectionItem> {
        val output = getResourceList(resId, contentResId).map {
            SingleSelectionItem(it.first, it.second)
        }
        return ArrayList(output)
    }

    private fun getResourceListMultiple(
        @ArrayRes resId: Int,
        @ArrayRes contentResId: Int
    ): ArrayList<MultipleSelectionItem> {
        val output = getResourceList(resId, contentResId).map {
            MultipleSelectionItem(it.first, it.second)
        }
        return ArrayList(output)
    }

    fun initSortBottomSheet(selectedItemId: String): SingleSelectionBottomSheet{
        val items = getResourceListSingle(R.array.sort_items_id, R.array.sort_items)
        val title = context.getString(R.string.commonbs_sort_title)
        val actionText = context.getString(R.string.action_apply)
        val bottomSheet = SingleSelectionBottomSheet.newInstance(selectedItemId, items)
        return bottomSheet.apply {
            setBottomSheetTitle(title)
            setBottomSheetButtonTitle(actionText)
        }
    }

    fun initFilterStatusBottomSheet(selectedItemIds: ArrayList<String>): MultipleSelectionBottomSheet{
        val multipleSelectionItems = getResourceListMultiple(R.array.status_filter_items_id, R.array.status_filter_items)
        val title = context.getString(R.string.commonbs_status_filter_title)
        val actionText = context.getString(R.string.action_apply)
        val bottomSheet = MultipleSelectionBottomSheet.newInstance(selectedItemIds, multipleSelectionItems)
        return bottomSheet.apply {
            setBottomSheetTitle(title)
            setBottomSheetButtonTitle(actionText)
        }
    }

    fun initFilterCategoryBottomSheet(
        selectedItemIds: List<String>,
        categoryItems: List<Category>
    ): MultipleSelectionBottomSheet{
        val items = categoryItems.map { MultipleSelectionItem(it.categoryId, it.categoryName) }
        val title = context.getString(R.string.commonbs_category_filter_title)
        val actionText = context.getString(R.string.action_apply)
        val bottomSheet = MultipleSelectionBottomSheet.newInstance(selectedItemIds, items)
        return bottomSheet.apply {
            setBottomSheetTitle(title)
            setBottomSheetButtonTitle(actionText)
        }
    }

    fun initCategoryFilterBottomSheet(selectedItemId: String): SingleSelectionBottomSheet{
        val items = getResourceListSingle(R.array.criteria_filter_items_id, R.array.criteria_filter_items)
        val title = context.getString(R.string.commonbs_criteria_filter_title)
        val actionText = context.getString(R.string.action_apply)
        val bottomSheet = SingleSelectionBottomSheet.newInstance(selectedItemId, items)
        return bottomSheet.apply {
            setBottomSheetTitle(title)
            setBottomSheetButtonTitle(actionText)
        }
    }

}
