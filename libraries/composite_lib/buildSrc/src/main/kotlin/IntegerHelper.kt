

fun String.toIntegerTruncated() :Int {
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