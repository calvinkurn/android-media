package com.tokopedia.stories.uimodel

/**
 * @author by astidhiyaa on 08/08/23
 */
enum class StoryType(val value: Int) {
    RilisanSpesial(1),
    FlashSale(2),
    Restock(3),
    NewProducts(4),
    DiskonToko(5),
    ManualCreation(6),
    Unknown(-1);

    companion object {
        fun convertValue(value: Int): StoryType {
            return StoryType.values()
                .firstOrNull { it.value == value } ?: Unknown
        }
    }
}
