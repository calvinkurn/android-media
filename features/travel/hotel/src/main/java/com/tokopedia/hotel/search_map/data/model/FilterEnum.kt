package com.tokopedia.hotel.search_map.data.model

/**
 * @author by jessica on 27/08/20
 */

enum class FilterStarEnum(val value: String) {
    STAR_ONE("1"),
    STAR_TWO("2"),
    STAR_THREE("3"),
    STAR_FOUR("4"),
    STAR_FIVE("5");
}

enum class FilterRatingEnum(val value: String) {
    ALL_RATING("Semua"),
    ABOVE_6("6.0"),
    ABOVE_7("7.0"),
    ABOVE_8("8.0"),
    ABOVE_9("9.0");
}