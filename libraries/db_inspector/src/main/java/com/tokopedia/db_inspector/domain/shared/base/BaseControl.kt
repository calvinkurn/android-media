package com.tokopedia.db_inspector.domain.shared.base

internal interface BaseControl<Mapper : BaseMapper<*, *>, Converter : BaseConverter<*, *>> {

    val mapper: Mapper

    val converter: Converter
}
