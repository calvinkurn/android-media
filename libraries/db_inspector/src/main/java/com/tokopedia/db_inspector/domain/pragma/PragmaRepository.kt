package com.tokopedia.db_inspector.domain.pragma

import com.tokopedia.db_inspector.domain.Control
import com.tokopedia.db_inspector.domain.Interactors
import com.tokopedia.db_inspector.domain.databases.Repositories
import com.tokopedia.db_inspector.domain.shared.models.Page
import com.tokopedia.db_inspector.domain.shared.models.parameters.PragmaParameters
import javax.inject.Inject

internal class PragmaRepository @Inject constructor(
    private val userVersion: Interactors.GetUserVersion,
    private val control: Control.Pragma,
) : Repositories.Pragma {
    override suspend fun getUserVersion(input: PragmaParameters.Version): Page =
         control.mapper(userVersion(control.converter version input) )

}