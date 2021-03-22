package com.tokopedia.analyticsdebugger.cassava.validator.core

import com.tokopedia.VALID_QUERY
import com.tokopedia.VALID_QUERY_NO_MODE
import com.tokopedia.VALID_QUERY_NO_README
import org.junit.Test

class CassavaQueryKtTest {

    @Test
    fun `given true query returns expected result`() {
        val actual = VALID_QUERY.toCassavaQuery()
        assert(actual.mode == QueryMode.EXACT)
        assert(actual.readme.isNullOrEmpty().not())
        assert(actual.query.size == 2)
        assert(actual.query.first().containsKey("discoveryName"))
    }

    @Test
    fun `given valid query without readme returns success with nullable readme`() {
        val actual = VALID_QUERY_NO_README.toCassavaQuery()
        assert(actual.readme == null)
    }

    @Test
    fun `given valid query without mode returns success with default exact mode`() {
        val actual = VALID_QUERY_NO_MODE.toCassavaQuery()
        assert(actual.mode == QueryMode.EXACT)
    }
}

