package com.tokopedia.editshipping.util

import kotlin.math.min

object ShopEditAddressUtils {

    fun normalize(address: String): String {
        var newAddress = address.toLowerCase()
        newAddress = Regex("[^\\w\\s&]").replace(newAddress, " ")
        val regex = """(jalan|jln|jl|blok|kavling|kav|nomor|no|nmr|kecamatan|kec|kabupaten|kab|kota|unknown|unnamed|location|[0-9])""".toRegex()
        newAddress = regex.replace(newAddress, " ")
        newAddress = Regex("\\r?\\n|\\r").replace(newAddress, " ")
        newAddress = Regex("  +").replace(newAddress, " ")
        newAddress = newAddress.trim()
        return newAddress
    }

    fun validateAddressSimilarity(addr1: String, addr2: String): Boolean {
        val matchWord = levenshteinDistance(addr1, addr2)
        val minWordLen = minLenSentence(addr1, addr2)
        val verboseTest = matchWord.toDouble()/minWordLen.toDouble()
        return verboseTest <= 0.4
    }

    private fun levenshteinDistance(addr1: String, addr2: String): Int {
        val _addr1 = addr1.split(' ')
        val _addr2 = addr2.split(' ')

        val lhsLength = _addr1.size
        val rhsLength = _addr2.size

        var cost = Array(lhsLength) { it }
        var newCost = Array(lhsLength) { 0 }

        for (i in 1..rhsLength-1) {
            newCost[0] = i

            for (j in 1..lhsLength-1) {
                val match = if(addr1[j - 1] == addr2[i - 1]) 0 else 1

                val costReplace = cost[j - 1] + match
                val costInsert = cost[j] + 1
                val costDelete = newCost[j - 1] + 1

                newCost[j] = min(min(costInsert, costDelete), costReplace)
            }

            val swap = cost
            cost = newCost
            newCost = swap
        }

        return cost[lhsLength - 1]
    }


    private fun minLenSentence(addr1: String, addr2: String): Int {
        val _addr1 = addr1.split(' ')
        val _addr2 = addr2.split(' ')

        return if (_addr1.size <= _addr2.size) {
            _addr1.size
        } else _addr2.size
    }
}