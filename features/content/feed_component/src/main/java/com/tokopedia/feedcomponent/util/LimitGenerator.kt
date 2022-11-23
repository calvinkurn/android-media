package com.tokopedia.feedcomponent.util

/** created by fachrizalmrsln at 23/11/22 **/
object LimitGenerator {

    /** this function used to find expected limit from item currentPosition **/
    fun getExpectedLimit(currentPosition: Int): Int {

        /** currentPosition is index **/
        val actualPosition = currentPosition + 1

        /** 10 determined as default limit **/
        if (actualPosition <= 0) return 10

        /** mostly limit multiples of 10 **/
        return if (actualPosition < 10) 10 else actualPosition / 10 * 10 + 10
    }

}
