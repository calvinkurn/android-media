package com.tokopedia.media.loader.utils

object AspectRatio {

    private fun euclid(a: Int, b: Int): Int {
        if (b == 0) return a
        return euclid(b, a % b)
    }

    fun calculate(numerator: Int, denominator: Int): Pair<Int, Int> {
        var numeratorTemp = 0
        var left: Int
        var right: Int

        if (numerator == denominator) return Pair(1, 1)
        if (numerator < denominator) numeratorTemp = numerator

        val divisor = euclid(numerator, denominator)

        if (numeratorTemp == 0) {
            left = numerator / divisor
            right = denominator / divisor
        } else {
            left = denominator / divisor
            right = numerator / divisor
        }

        if (left == 8 && right == 5) {
            left = 16
            right = 10
        }

        return Pair(left, right)
    }

}