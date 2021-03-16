package com.tokopedia.sessioncommon.domain.query

/**
 * Created by Yoris Prayogo on 17/02/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */

object GenerateKeyQuery {
    private const val module = "\$module"

    val generateKeyQuery: String = """
        query generate_key($module: String!) {
            generate_key(module: $module) {
                key
                server_timestamp
                h
            }
        }
    """.trimIndent()
}