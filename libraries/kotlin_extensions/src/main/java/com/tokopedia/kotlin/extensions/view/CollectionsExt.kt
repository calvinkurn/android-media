package com.tokopedia.kotlin.extensions.view

fun <T> Iterable<T>.joinToStringWithLast(
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = -1,
    truncated: CharSequence = "...",
    transform: ((T) -> CharSequence)? = null,
    lastSeparator: String? = null
): String {
    return joinToLastSeparator(
        StringBuilder(),
        separator,
        prefix,
        postfix,
        limit,
        truncated,
        transform,
        lastSeparator
    ).toString()
}

fun <T, A : Appendable> Iterable<T>.joinToLastSeparator(
    buffer: A,
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = -1,
    truncated: CharSequence = "...",
    transform: ((T) -> CharSequence)? = null,
    lastSeparator: String? = null
): A {
    buffer.append(prefix)
    var count = 0
    var size = 0
    if (this is Collection) {
        size = this.size
    }
    for (element in this) {
        if (++count > 1) {
            if (count == size) {
                buffer.append(lastSeparator ?: separator)
            } else {
                buffer.append(separator)
            }
        }
        if (limit < 0 || count <= limit) {
            buffer.appendElement(element, transform)
        } else break
    }
    if (limit in 0..(count - 1)) buffer.append(truncated)
    buffer.append(postfix)
    return buffer
}

fun <T> Appendable.appendElement(element: T, transform: ((T) -> CharSequence)?) {
    when {
        transform != null -> append(transform(element))
        element is CharSequence? -> append(element)
        element is Char -> append(element)
        else -> append(element.toString())
    }
}

fun <T> MutableIterable<T>.removeFirst(predicate: (T) -> Boolean): Boolean {
    val iterator: MutableIterator<T> = this.iterator()
    while (iterator.hasNext()) {
        if (predicate(iterator.next())) {
            iterator.remove()
            return true
        }
    }
    return false
}

fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
}

fun <T> MutableList<T>.goToFirst(index: Int) {
    this.moveTo(index, 0)
}

fun <T> MutableList<T>.moveTo(fromPosition: Int, toPosition: Int) {
    if (size == 0 ||
        fromPosition < 0 || fromPosition >= size ||
        toPosition < 0 || toPosition >= size) {
        return
    }
    val tmp = this[fromPosition]
    this.removeAt(fromPosition)
    if (toPosition < this.size) {
        this.add(toPosition, tmp)
    } else {
        this.add(tmp)
    }

}