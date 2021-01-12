package com.tokopedia.editshipping.ui.shopeditaddress

class Test{

    private fun minLenSentence(addr1: String, addr2: String): Int {
        var _addr1 = addr1.split(' ')
        var _addr2 = addr2.split(' ')

        return if (_addr1.size <= _addr2.size) {
            _addr1.size
        } else _addr2.size
    }

    private fun maxLen(addr1: String, addr2: String): Int {
        return if (addr1.length >= addr2.length) {
            addr1.length
        } else addr2.length
    }

    private fun levenshteinDistance(addr1: String, addr2: String): Int {
        val lhsLength = addr1.length
        val rhsLength = addr2.length

        var cost = Array(lhsLength) { it }
        var newCost = Array(lhsLength) { 0 }

        for (i in 1..rhsLength-1) {
            newCost[0] = i

            for (j in 1..lhsLength-1) {
                val match = if(addr1[j - 1] == addr2[i - 1]) 0 else 1

                val costReplace = cost[j - 1] + match
                val costInsert = cost[j] + 1
                val costDelete = newCost[j - 1] + 1

                newCost[j] = Math.min(Math.min(costInsert, costDelete), costReplace)
            }

            val swap = cost
            cost = newCost
            newCost = swap
        }

        return cost[lhsLength - 1]
    }

    private fun countSentenceMatch(addr1: String, addr2: String): Int {
        var maddr1 = addr1.split(' ')
        var maddr2 = addr2.split(' ')
        var flag = mutableListOf<Int>()
        var matchWord = 0

        for (i in 0..maddr1.size) {
            var smallestIndex = -1
            var minValue = 2147383647
            for (j in 0..maddr2.size) {
                if (j !in flag) {
                    var isMatch = false
                    var dist = levenshteinDistance(addr1[i].toString(), addr2[j].toString())
                    var maxLen = maxLen(addr1[i].toString(), addr2[j].toString())

                    var res = dist.toDouble()/maxLen.toDouble()

                    isMatch = res <= 0.3

                    if (isMatch && dist.toString().toInt() < minValue) {
                        minValue = dist.toString().toInt()
                        smallestIndex = j
                    }
                }
            }
                if (smallestIndex != -1) {
                matchWord +=1
            }
           flag.add(smallestIndex)
        }
        return matchWord
    }

    private fun validatorAddress(addr1: String, addr2: String): Boolean {
        var matchWord = countSentenceMatch(addr1, addr2)
        var minWordLen = minLenSentence(addr1, addr2)
        var verboseTest = matchWord.toDouble()/minWordLen.toDouble()
        return verboseTest >= 0.4
    }


    private fun normalize(text: String) {
        var newString = text.toLowerCase()
//        newString = newString.replace("/[^\w\s&]/gi", " ")
        newString = newString.replace("jalan|jalan", " ")
    }
}