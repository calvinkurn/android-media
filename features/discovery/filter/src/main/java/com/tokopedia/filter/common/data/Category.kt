package com.tokopedia.filter.common.data


import com.tokopedia.filter.newdynamicfilter.adapter.MultiLevelExpIndListAdapter

import java.util.ArrayList

class Category : MultiLevelExpIndListAdapter.ExpIndData {

    override var children: List<MultiLevelExpIndListAdapter.ExpIndData> = ArrayList()
    var id: String? = null
    var name: String? = null
    var iconImageUrl: String? = null
    var hasChild: Boolean? = null
    var indentation: Int = 0
    var key: String? = null

    val isAnnotation: Boolean
        get() = Option.KEY_ANNOTATION_ID.equals(key)

    override var isGroup: Boolean
        get() = hasChild!!
        set(value) {
            hasChild = value
        }

    override fun setGroupSize(groupSize: Int) {

    }

    override fun toString(): String {
        return id.toString()
    }

    fun addChildren(children: List<Category>, indentation: Int) {
        this.children = children
        for (category in children) {
            category.indentation = indentation
        }
    }
}
