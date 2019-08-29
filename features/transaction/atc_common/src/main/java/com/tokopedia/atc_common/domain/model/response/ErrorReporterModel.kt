package com.tokopedia.atc_common.domain.model.response

data class ErrorReporterModel(
        var eligible: Boolean = false,
        var description: String = ""
)