package com.tokopedia.db_inspector.domain

import com.tokopedia.db_inspector.domain.shared.base.BaseControl

internal interface Control {

    interface Database : BaseControl<Mappers.Database, Converters.Database>

    interface Connection : BaseControl<Mappers.Connection, Converters.Connection>

}
