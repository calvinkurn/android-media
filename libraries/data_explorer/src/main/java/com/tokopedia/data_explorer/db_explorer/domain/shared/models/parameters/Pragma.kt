package com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters

internal class Pragma {

    private lateinit var pragmaName: String
    private var pragmaValue: String? = null

    fun name(pragmaName: String) {
        this.pragmaName = "\"$pragmaName\""
    }

    fun value(pragmaValue: String) {
        this.pragmaValue = "\"$pragmaValue\""
    }

    fun build(): String {
        if (!::pragmaName.isInitialized) {
            throw IllegalStateException("Failed to build - pragmaName is undefined")
        }
        return toString()
    }

    override fun toString(): String {
        return "PRAGMA $pragmaName${pragmaValue?.let { "($it)" }.orEmpty()}"
    }

}