package com.tokopedia.db_inspector.domain.connection.interactors

import android.database.sqlite.SQLiteDatabase
import com.tokopedia.db_inspector.data.Sources
import com.tokopedia.db_inspector.domain.Interactors
import javax.inject.Inject

internal class OpenConnectionInteractor @Inject constructor(
    val sources: Sources.Memory
    ): Interactors.OpenConnection {
    override suspend fun invoke(input: String): SQLiteDatabase =
        sources.openConnection(input)

}