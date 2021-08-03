package com.tokopedia.smartbills.data


enum class CategoryTelcoType(val categoryId: String, val categoryValue: String) {
    PULSA("1", "Pulsa"),
    PAKET_DATA("2", "Paket Data"),
    PASCABAYAR("9", "Pasca Bayar");

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