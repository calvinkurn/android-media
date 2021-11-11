package com.tokopedia.db_inspector.domain.pragma.interactors

import com.tokopedia.db_inspector.data.Sources
import com.tokopedia.db_inspector.data.models.cursor.input.Query
import com.tokopedia.db_inspector.data.models.cursor.output.QueryResult
import com.tokopedia.db_inspector.domain.Interactors
import javax.inject.Inject

internal class GetUserVersionInteractor @Inject constructor(
    val sources: Sources.Pragma
) : Interactors.GetUserVersion {
    override suspend fun invoke(input: Query): QueryResult =
        sources.getUserVersion(input)

}