package com.tokopedia.data_explorer.db_explorer.domain

import com.tokopedia.data_explorer.db_explorer.domain.shared.base.BaseControl

internal interface Control {

    interface Database : BaseControl<Mappers.Database, Converters.Database>

    interface Connection : BaseControl<Mappers.Connection, Converters.Connection>

    interface Pragma: BaseControl<Mappers.Pragma, Converters.Pragma>
    interface Content: BaseControl<Mappers.Content, Converters.Content>

}
