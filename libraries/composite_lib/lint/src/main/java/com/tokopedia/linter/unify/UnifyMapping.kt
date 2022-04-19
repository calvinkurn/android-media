package com.tokopedia.linter.unify

data class UnifyMapping (val newName:String) {
    fun getMessage() = "Please Use "+newName

}