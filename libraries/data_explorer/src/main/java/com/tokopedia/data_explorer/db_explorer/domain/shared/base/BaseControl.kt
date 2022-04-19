package com.tokopedia.data_explorer.db_explorer.domain.shared.base

internal interface BaseControl<Mapper : BaseMapper<*, *>, Converter : BaseConverter<*, *>> {

    val mapper: Mapper

    val converter: Converter
}
