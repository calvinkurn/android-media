package com.example.akamai_bot_lib

import com.tokopedia.akamai_bot_lib.getAny
import com.tokopedia.akamai_bot_lib.getMutation
import com.tokopedia.akamai_bot_lib.interceptor.registeredGqlFunctions
import org.junit.Assert
import org.junit.Test
import java.util.regex.Pattern

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest2 {
    @Test
    fun addition_isCorrect() {
        Assert.assertEquals(4, 2 + 2.toLong())
    }

    @Test
    fun testMutation() {
        val input = "mutation RegisterUsername (\$affiliateName: String!) {\n  bymeRegisterAffiliateName(affiliateName: \$affiliateName) {\n    success\n    error {\n      message\n      type\n      code\n    }\n  }\n}\n"
        Assert.assertTrue(getMutation(input, "RegisterUsername"))
    }

    @Test
    fun staticTest() {
        val query = "mutation RegisterUsername (\$affiliateName: String!) {\n  validate_use_promo_revamp(affiliateName: \$affiliateName) {\n    success\n    error {\n      message\n      type\n      code\n    }\n  }\n}\n"

        val xTkpdAkamai = getAny(query)
                .asSequence()
                .filter { it ->
                    registeredGqlFunctions.containsKey(it)
                }.take(1).map { it ->
                    registeredGqlFunctions[it]
                }.first()
        assert(!xTkpdAkamai.isNullOrEmpty())
        assert(xTkpdAkamai.equals("promorevamp"))
    }
}