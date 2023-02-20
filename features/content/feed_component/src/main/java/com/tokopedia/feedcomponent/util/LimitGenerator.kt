package com.tokopedia.feedcomponent.util

/** created by fachrizalmrsln at 23/11/22 **/
object LimitGenerator {

    /** this function used to find expected limit from item currentPosition **/
    fun getExpectedLimit(currentPosition: Int): Int {

        /** currentPosition is index **/
        val actualPosition = currentPosition + 1

        /** 10 determined as default limit **/
        if (actualPosition <= 0) return DEFAULT_LIMIT

        /** mostly limit multiples of 10 **/
        return if (actualPosition < DEFAULT_LIMIT) DEFAULT_LIMIT
        else actualPosition / DEFAULT_LIMIT * DEFAULT_LIMIT + DEFAULT_LIMIT
    }

    private const val DEFAULT_LIMIT = 10

}
