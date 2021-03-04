package com.tokopedia.analyticsdebugger.cassava.validator.core

import com.tokopedia.VALID_QUERY
import org.junit.Test

import org.junit.Assert.*

class ValidatorTransformKtTest {

    @Test
    fun `given valid json returns expected map`() {
        val actual = VALID_QUERY.toJsonMap()
        assert(actual.size == 3)
    }

    @Test
    fun `given random string returns empty map`() {
        val actual = RANDOM_STRING.toJsonMap()
        assert(actual.isEmpty())
    }
}

const val RANDOM_STRING = "Exception foo: foo"