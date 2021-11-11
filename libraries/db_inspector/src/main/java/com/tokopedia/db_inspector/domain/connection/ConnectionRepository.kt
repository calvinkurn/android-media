package com.tokopedia.db_inspector.domain.connection

import com.tokopedia.db_inspector.domain.Control
import com.tokopedia.db_inspector.domain.Interactors
import com.tokopedia.db_inspector.domain.connection.models.DatabaseConnection
import com.tokopedia.db_inspector.domain.databases.Repositories
import com.tokopedia.db_inspector.domain.shared.models.parameters.ConnectionParameters
import javax.inject.Inject

internal class ConnectionRepository @Inject constructor(
    private val openInteractor: Interactors.OpenConnection,
    private val closeInteractor: Interactors.CloseConnection,
    private val control: Control.Connection

): Repositories.Connection {
    override suspend fun open(input: ConnectionParameters): DatabaseConnection =
        control.mapper(openInteractor(control.converter(input)))

    override suspend fun close(input: ConnectionParameters) {
        closeInteractor(control.converter(input))
    }
}