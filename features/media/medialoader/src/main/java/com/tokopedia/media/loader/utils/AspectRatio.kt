package com.tokopedia.media.loader.utils

/*
* This aspectRatio is needed to calculate the size of the ImageView,
* which is the image media that will be displayed. This aspectRatio is
* to check the aspect ratio of the existing imageView and will be sent
* to the BlurHashDecoder as the width and height of the new image blur.
* */
object AspectRatio {

    // using euclidean algorithm
    private fun euclid(a: Int, b: Int): Int {
        if (b == 0) return a
        return euclid(b, a % b)
    }

    /*
    * it will measure based on the ImageView size,
    * for example, the ImageView has sizes (300x300) px,
    * the calculation it will be return like this:
    * Pair(ratioWidth, ratioHeight) = Pair(1,1)
    * */
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