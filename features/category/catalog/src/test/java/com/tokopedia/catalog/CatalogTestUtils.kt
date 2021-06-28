package com.tokopedia.catalog

import java.io.File

object CatalogTestUtils {

    const val USER_ID = "23046429"
    const val DEVICE = "mobile"
    const val CATALOG_ID = "63"
    const val CATALOG_URL = "tokopedia://catalog/63"

    fun getJsonFromFile(path: String): String {
        val uri = ClassLoader.getSystemClassLoader().getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }
}