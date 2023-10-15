package com.tokopedia.stories.uimodel

/**
 * @author by astidhiyaa on 08/08/23
 */
enum class StoryActionType(val value: Int) {
    Delete(-1),
    Draft(0),
    Active(1),
    Upcoming(2),
    Moderated(3),
    Expired(4),
    Unknown(-100);

    companion object {
        fun convertValue(value: Int): StoryActionType {
            return StoryActionType.values()
                .firstOrNull { it.value == value } ?: Unknown
        }
    }
}
