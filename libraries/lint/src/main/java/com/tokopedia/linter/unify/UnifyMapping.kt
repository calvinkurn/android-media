package com.tokopedia.linter.unify

data class UnifyMapping (val oldName:String, val newName:String) {
    fun getMessage() = "Please Use "+newName

}