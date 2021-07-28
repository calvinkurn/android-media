package com.tokopedia.smartbills.data


enum class CategoryTelcoType(val categoryId: String, val categoryValue: String) {
    PULSA("2", "Pulsa"),
    PAKET_DATA("3", "Paket Data");

    companion object {
        @JvmStatic
        fun getCategoryString(label: String?): String {
            for (type in values()) {
                if (type.categoryId == label) return type.categoryValue
            }
            return PULSA.categoryValue
        }
    }
}