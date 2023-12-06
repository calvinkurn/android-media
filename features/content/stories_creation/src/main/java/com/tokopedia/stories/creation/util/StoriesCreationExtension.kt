package com.tokopedia.stories.creation.util

/**
 * Created By : Jonathan Darwin on November 08, 2023
 */

fun List<String>.firstNotEmptyOrNull(): String? {
    return if (isEmpty()) null
    else if (this[0].isEmpty()) null
    else this[0]
}
