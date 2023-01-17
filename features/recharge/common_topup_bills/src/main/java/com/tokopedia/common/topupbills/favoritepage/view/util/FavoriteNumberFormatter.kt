package com.tokopedia.common.topupbills.favoritepage.view.util

object FavoriteNumberFormatter {
    private const val DIVIDER_POSITION = 4
    private const val DIVIDER = ' '

    fun concatStringWith16D(text: CharArray): String {
        val formatted = StringBuilder()
        var count = 0
        for (c in text) {
            if (Character.isDigit(c) || c == '*') {
                if (count % DIVIDER_POSITION == 0 && count > 0) {
                    formatted.append(DIVIDER)
                }
                formatted.append(c)
                ++count
            }
        }
        return formatted.toString()
    }
}
