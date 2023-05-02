package com.tokopedia.tkpd.flashsale.presentation.bottomsheet

import android.content.Context
import androidx.annotation.ArrayRes
import com.tokopedia.campaign.components.bottomsheet.selection.entity.MultipleSelectionItem
import com.tokopedia.campaign.components.bottomsheet.selection.entity.SingleSelectionItem
import com.tokopedia.campaign.components.bottomsheet.selection.multiple.MultipleSelectionBottomSheet
import com.tokopedia.campaign.components.bottomsheet.selection.single.SingleSelectionBottomSheet
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.Category
import javax.inject.Inject

class CommonBottomSheetInitializer @Inject constructor() {

    private fun getResourceList(
        context: Context,
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
        context: Context,
        @ArrayRes resId: Int,
        @ArrayRes contentResId: Int
    ): ArrayList<SingleSelectionItem> {
        val output = getResourceList(context, resId, contentResId).map {
            SingleSelectionItem(it.first, it.second)
        }
        return ArrayList(output)
    }

    fun initFilterCategoryBottomSheet(
        context: Context,
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

    fun initCategoryFilterBottomSheet(context: Context, selectedItemId: String): SingleSelectionBottomSheet{
        val items = getResourceListSingle(context, R.array.criteria_filter_items_id, R.array.criteria_filter_items)
        val title = context.getString(R.string.commonbs_criteria_filter_title)
        val actionText = context.getString(R.string.action_apply)
        val bottomSheet = SingleSelectionBottomSheet.newInstance(selectedItemId, items)
        return bottomSheet.apply {
            setBottomSheetTitle(title)
            setBottomSheetButtonTitle(actionText)
        }
    }

}
