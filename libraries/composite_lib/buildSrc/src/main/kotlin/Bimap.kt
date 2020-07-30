package com.tokopedia.plugin

class Bimap {
    var bimapSize = 0

    private val indexToVerticesMap = mutableMapOf<Int, String>()
    private val verticesToIndexMap = mutableMapOf<String, Int>()

    fun addString(str: String) {
        if (!verticesToIndexMap.containsKey(str)) {
            verticesToIndexMap[str] = bimapSize
            indexToVerticesMap[bimapSize] = str
            bimapSize++
        }
    }

    fun getString(index:Int):String {
        return indexToVerticesMap[index] ?: ""
    }

    fun getIndex(string: String):Int {
        return verticesToIndexMap[string] ?: -1
    }
}