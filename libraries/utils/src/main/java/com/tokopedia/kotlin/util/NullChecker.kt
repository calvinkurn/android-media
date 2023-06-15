package com.tokopedia.kotlin.util

import com.google.gson.GsonBuilder
import timber.log.Timber

/**
 * @author by milhamj on 29/01/19.
 */

/**
 * The pattern that's used to find null value in an object.
 * It starts with a newline, then some whitespaces, variable name inside quotation marks,
 * a colon `:`, some whitespaces, and ended with the word `null`.
 */
private const val NULL_PATTERN = """\n\s*"\S+":\s*null"""

/**
 * Find null value in an object.
 * This function turns the object into JSON then use REGEX to find the null value.
 * The expected format is `"some_variable": null`.
 * @see NULL_PATTERN
 *
 * @param `object` the object to find the null value in
 * @param `actionWhenNull` optional lambda that will be invoked when null value is found in the `object`
 * @return Return true if null value is found, false otherwise
 */
@JvmOverloads
fun isContainNull(`object`: Any?, actionWhenNull: (String) -> Unit = { }): Boolean {
    val whenNull = { errorMessage: String ->
        Timber.d(errorMessage)
        actionWhenNull(errorMessage)
    }

    if (`object` == null) {
        whenNull("Object is null")
        return true
    }

    //serializeNulls is needed so null value won't be omitted in the JSON
    //setPrettyPrinting is needed because it's easier to find a variable in a formatted JSON
    //In a formatted JSON, a variable is always in a newline
    val gson = GsonBuilder()
        .registerTypeAdapterFactory(DefaultValueAdapterFactory())
        .serializeNulls()
        .setPrettyPrinting().create()
    val objectAsJson = gson.toJson(`object`).toLowerCase()
    val firstNullOccurrence = Regex(NULL_PATTERN).find(objectAsJson)

    return if (firstNullOccurrence == null) {
        false
    } else {
        whenNull("${firstNullOccurrence.value.trim()} in class ${`object`::class.java.name}")
        true
    }
}

/**
 * Will throw ContainNullException if the given object has null value,
 */
@JvmOverloads
fun throwIfNull(`object`: Any?, clazz: Class<*>?, rawMessage: String? = null, actionWhenNull: (String) -> Unit = { }) {
    logIfNull(`object`,clazz,rawMessage) {
        actionWhenNull.invoke(it)
        throw ContainNullException(it)
    }
}

/**
 * Will log message Null if the given object has null value,
 */
@JvmOverloads
fun logIfNull(`object`: Any?, clazz: Class<*>?, rawMessage: String? = null, actionWhenNull: (String) -> Unit = { }) {
    isContainNull(`object`) { errorMessage ->
        val message = String.format("Null Found %s in %s | %s |",
            errorMessage,
            clazz?.canonicalName ?: "",
            rawMessage ?: ""
        )
        Timber.e("P1#NULL_CHECKER#%s", message)
        actionWhenNull(errorMessage)
    }
}

@JvmOverloads
fun throwExceptionWhenNull(`object`: Any?, actionWhenNull: (String) -> Unit = { }) {
    if (isContainNull(`object`, actionWhenNull)) {
        throw ContainNullException()
    }
}
