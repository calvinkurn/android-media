package com.tokopedia.play.view.type

/**
 * @author by astidhiyaa on 02/02/22
 */
enum class ProductSectionType(val value: String) {
    Active("active"),
    Other("other"),
    Upcoming("upcoming");

    companion object{
        fun getSectionValue(sectionType: String): ProductSectionType{
            values().forEach {
                if (it.value.equals(sectionType, true)) return it
            }
            return Other
        }
    }
}