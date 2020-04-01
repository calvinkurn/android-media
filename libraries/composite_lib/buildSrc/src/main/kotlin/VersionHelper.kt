

private fun String.toIntegerTruncated() :Int {
    try {
        return toInt()
    } catch (e:Exception) {
        val len = length
        var newString = ""
        for (i in 0 until len) {
            val charEntry = this[i]
            if (charEntry.isDigit()) {
                newString += charEntry
            } else {
                break
            }
        }
        try {
            return newString.toInt()
        } catch (e2 : Exception) {
            return 1
        }
    }
}

fun Int.toVersion(configMap:Map<String, Int>):String{
    val tail = configMap["Tail"]?: 100
    val mid = configMap["Mid"] ?: 10
    val head = 1
    return toVersion(head, mid, tail)
}

fun Int.toVersion(head:Int, mid:Int, tail:Int):String{
    val unitMultiply = listOf(mid*tail, tail, 1)
    val result = mutableListOf<Int>()
    var tempInt = this
    for (i in unitMultiply.indices){
        result.add (tempInt/unitMultiply[i])
        tempInt %= unitMultiply[i]
    }
    return result.joinToString(".")
}

// version config to set maximum head:mid:tail
fun String.versionToInt(configMap:Map<String, Int>):Pair<Int, String>{
    val tail = configMap["Tail"]?: 100
    val mid = configMap["Mid"] ?: 10
    val head = configMap["Head"] ?: 1
    val unitMultiply = listOf(mid*tail, tail, 1)

    val splits = split(".")
    var valueInteger = 0
    splits.forEachIndexed { index, str ->
        valueInteger += str.toIntegerTruncated() * unitMultiply[index]
    }
    return (valueInteger) to ((valueInteger).toVersion(head, mid, tail))
}