package com.tokopedia.test.application.id_generator

/**
 * Created by kenny.hadisaputra on 15/04/22
 */
private const val folderName = "res_id_result"

fun FileWriter.writeGeneratedViewIds(
    fileName: String,
    text: String
) {
    write(folderName, fileName, text)
}