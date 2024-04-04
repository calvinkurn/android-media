package com.tokopedia.tokopedianow.shoppinglist.presentation.model

import android.content.Context
import android.view.View
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.R

class CoachMarkModel(
    private val onDismiss: () -> Unit
) {
    private var coachMark: CoachMark2? = null
    private var listItem: ArrayList<CoachMark2Item> = arrayListOf()

    fun show(context: Context) {
        coachMark = CoachMark2(context).apply {
            onDismissListener = {
                onDismiss()
            }
            stepButtonTextLastChild = context.getString(R.string.tokopedianow_shopping_list_coach_mark_step_button_last_child)
            showCoachMark(listItem)
        }
    }

    fun hide() {
        coachMark?.dismissCoachMark()
    }

    fun setDataForBottomBulkAtc(
        ubAtc: View?
    ) {
        if (ubAtc == null) return

        listItem = arrayListOf(
            CoachMark2Item(
                anchorView = ubAtc,
                title = String.EMPTY,
                description = ubAtc.context?.getString(R.string.tokopedianow_shopping_list_bottom_bulk_atc_coach_mark_description).orEmpty(),
                position = CoachMark2.POSITION_TOP
            )
        )
    }

    fun setDataForShoppingListPage(
        tpShoppingList: View?,
        tpAddToShoppingList: View?,
        tpBuyAgain: View?
    ) {
        if (tpShoppingList == null || tpBuyAgain == null) return

        listItem = if (tpAddToShoppingList != null) {
            arrayListOf(
                CoachMark2Item(
                    anchorView = tpShoppingList,
                    title = tpShoppingList.context?.getString(R.string.tokopedianow_shopping_list_first_page_coach_mark_title).orEmpty(),
                    description = tpShoppingList.context?.getString(R.string.tokopedianow_shopping_list_first_page_coach_mark_description).orEmpty(),
                    position = CoachMark2.POSITION_BOTTOM
                ),
                CoachMark2Item(
                    anchorView = tpAddToShoppingList,
                    title = String.EMPTY,
                    description = tpAddToShoppingList.context?.getString(R.string.tokopedianow_shopping_list_second_page_coach_mark_description).orEmpty(),
                    position = CoachMark2.POSITION_BOTTOM
                ),
                CoachMark2Item(
                    anchorView = tpBuyAgain,
                    title = String.EMPTY,
                    description = tpBuyAgain.context?.getString(R.string.tokopedianow_shopping_list_third_page_coach_mark_description).orEmpty(),
                    position = CoachMark2.POSITION_BOTTOM
                )
            )
        } else {
            arrayListOf(
                CoachMark2Item(
                    anchorView = tpShoppingList,
                    title = tpShoppingList.context?.getString(R.string.tokopedianow_shopping_list_first_page_coach_mark_title).orEmpty(),
                    description = tpShoppingList.context?.getString(R.string.tokopedianow_shopping_list_first_page_coach_mark_description).orEmpty(),
                    position = CoachMark2.POSITION_BOTTOM
                ),
                CoachMark2Item(
                    anchorView = tpBuyAgain,
                    title = String.EMPTY,
                    description = tpBuyAgain.context?.getString(R.string.tokopedianow_shopping_list_third_page_coach_mark_description).orEmpty(),
                    position = CoachMark2.POSITION_BOTTOM
                )
            )
        }
    }
}
