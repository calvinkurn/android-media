package com.tokopedia.filter.common.data


import com.tokopedia.filter.newdynamicfilter.adapter.MultiLevelExpIndListAdapter

import java.util.ArrayList

class Category : MultiLevelExpIndListAdapter.ExpIndData {

    override var children: List<MultiLevelExpIndListAdapter.ExpIndData> = ArrayList()
    var id: String = ""
    var name: String = ""
    var iconImageUrl: String = ""
    var hasChild: Boolean = false
    var indentation: Int = 0
    var key: String = ""

    val isAnnotation: Boolean
        get() = Option.KEY_ANNOTATION_ID.equals(key)

    override var isGroup: Boolean
        get() = hasChild
        set(value) {
            hasChild = value
        }

    override fun setGroupSize(groupSize: Int) {

    }

    override fun toString(): String {
        return id
    }

    fun addChildren(children: List<Category>, indentation: Int) {
        this.children = children
        for (category in children) {
            category.indentation = indentation
        }
    }
}
