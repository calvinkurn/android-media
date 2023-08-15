package com.tokopedia.search.result.product.changeview

import com.tokopedia.iconunify.IconUnify

private const val SMALL_GRID_VALUE = 1
private const val SMALL_GRID_LABEL = "grid 2"
private const val SMALL_GRID_TRACKING_LABEL = "grid"

private const val LIST_VALUE = 2
private const val LIST_LABEL = "list"
private const val LIST_TRACKING_LABEL = "list"

private const val BIG_GRID_VALUE = 3
private const val BIG_GRID_LABEL = "grid 1"
private const val BIG_GRID_TRACKING_LABEL = "big-grid"

enum class ViewType(
    val value: Int,
    val label: String,
    val icon: Int,
    val trackingLabel: String,
) {

    SMALL_GRID(SMALL_GRID_VALUE, SMALL_GRID_LABEL, IconUnify.VIEW_GRID, SMALL_GRID_TRACKING_LABEL),
    LIST(LIST_VALUE, LIST_LABEL, IconUnify.VIEW_LIST, LIST_TRACKING_LABEL),
    BIG_GRID(BIG_GRID_VALUE, BIG_GRID_LABEL, IconUnify.VIEW_GRID_BIG, BIG_GRID_TRACKING_LABEL);

    fun change(): ViewType {
        return when (this) {
            SMALL_GRID -> LIST
            LIST -> BIG_GRID
            BIG_GRID -> SMALL_GRID
        }
    }

    companion object {

        fun of(value: Int): ViewType {
            values().forEach {
                if (it.value == value) return it
            }

            return SMALL_GRID
        }
    }
}
