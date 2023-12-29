package com.tokopedia.editshipping.util

/**
 * Created by irpan on 26/07/23.
 */
object ShopEditAddressLevenshteinUtils {

    private fun minLenSentence(addr1: String, addr2: String): Int {
        val saddr1 = addr1.split(" ")
        val saddr2 = addr2.split(" ")
        return if (saddr1.size <= saddr2.size) saddr1.size else saddr2.size
    }

    private fun maxLen(w1: String, w2: String): Int {
        return if (w1.length >= w2.length) w1.length else w2.length
    }

    private fun levenshteinDistance(a: String, b: String): Int {
        if (a.isEmpty()) return b.length
        if (b.isEmpty()) return a.length
        val matrix = Array(b.length + 1) { IntArray(a.length + 1) }

        for (i in 0..b.length) {
            matrix[i][0] = i
        }
        for (j in 0..a.length) {
            matrix[0][j] = j
        }

        for (i in 1..b.length) {
            for (j in 1..a.length) {
                if (b[i - 1] == a[j - 1]) {
                    matrix[i][j] = matrix[i - 1][j - 1]
                } else {
                    matrix[i][j] = minOf(
                        matrix[i - 1][j - 1] + 1,
                        minOf(matrix[i][j - 1] + 1, matrix[i - 1][j] + 1)
                    )
                }
            }
        }

        return matrix[b.length][a.length]
    }

    private fun countSentenceMatch(addr1: String, addr2: String): Int {
        val saddr1 = addr1.split(" ")
        val saddr2 = addr2.split(" ")
        val flag = mutableMapOf<Int, Boolean>()
        var matchWord = 0

        for (i in saddr1.indices) {
            var smallestIndex = -1
            var minValue = Int.MAX_VALUE

            for (j in saddr2.indices) {
                if (!flag.containsKey(j)) {
                    val dist = levenshteinDistance(saddr1[i], saddr2[j])
                    val maxLenValue = maxLen(saddr1[i], saddr2[j])
                    val res = dist.toDouble() / maxLenValue.toDouble()

                    if (res <= 0.3) {
                        if (dist < minValue) {
                            minValue = dist
                            smallestIndex = j
                        }
                    }
                }
            }

            if (smallestIndex != -1) {
                matchWord++
            }
            flag[smallestIndex] = true
        }
        return matchWord
    }

    fun normalize(str: String): String {
        var strs = str.toLowerCase()
        strs = strs.replace(Regex("[^\\w\\s&]"), " ")
        strs = strs.replace(
            Regex("jalan|jln|jl|blok|kavling|kav|nomor|no|nmr|kecamatan|kec|kabupaten|kab|kota|unknown|unnamed|location|[0-9]"),
            ""
        )
        strs = strs.replace(Regex("\r?\n|\r"), " ")
        strs = strs.replace(Regex("  +"), " ")
        strs = strs.trim()
        return strs
    }

    fun validateAddressSimilarity(addr1: String, addr2: String): Boolean {
        val matchWord = countSentenceMatch(addr1, addr2)
        val minWordLen = minLenSentence(addr1, addr2)
        val verboseTest = matchWord.toDouble() / minWordLen.toDouble()
        return verboseTest >= 0.4
    }
}
