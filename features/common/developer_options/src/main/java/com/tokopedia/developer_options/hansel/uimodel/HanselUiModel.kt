package com.tokopedia.developer_options.hansel.uimodel

data class HanselUiModel (
    val patchId: Long = 0,
    val functionId: Long = 0,
    val functionName: String = "",
    val patchName: String = "",
    val counter: Int = 0
) {
    override fun equals(other: Any?): Boolean {
        return patchId == (other as? HanselUiModel?)?.patchId
    }

    override fun hashCode(): Int {
        var result = patchId.hashCode()
        result = 31 * result + functionId.hashCode()
        result = 31 * result + functionName.hashCode()
        result = 31 * result + counter
        return result
    }
}
