package com.tokopedia.report.view.util

class FlattenedWrapper<P: ParentItem<C>, C: Any>(){

    var parent: P? = null
        set(value) {
            field = value
            value?.let { generateWrappedChildren(value) }
        }
    var child: C? = null
    var isExpanded: Boolean = false
    var isWrappedParent: Boolean = false

    val wrappedChildList = mutableListOf<FlattenedWrapper<P, C>>()

    constructor(_parent: P): this(){
        parent = _parent
        isWrappedParent = true
        generateWrappedChildren(_parent)
    }

    constructor(_child: C): this(){
        child = _child
    }

    private fun generateWrappedChildren(parent: P){
        wrappedChildList.clear()
        wrappedChildList.addAll(parent.getChildList().map { FlattenedWrapper<P, C>(it) })
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val that = other as? FlattenedWrapper<*, *> ?: return false

        if (!(parent?.equals(that.parent) ?: (that.parent == null)) )
            return false

        return child?.equals(that.child) ?: that.child == null
    }

    override fun hashCode(): Int {
        val result = parent?.hashCode() ?: 0

        return 31 * result + (child?.hashCode() ?: 0)
    }
}