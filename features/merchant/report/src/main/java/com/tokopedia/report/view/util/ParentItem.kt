package com.tokopedia.report.view.util

/**
 * Interface for implementing required methods in a parent.
 */
interface ParentItem<out C: Any> {

    fun getChildList(): List<C>

    val isInitiallyExpanded: Boolean
}