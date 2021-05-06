package com.example.akamai_bot_lib

import com.tokopedia.akamai_bot_lib.*
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest2 {

    @Rule @JvmField
    public val timberConsoleTree = TimberConsoleRule()

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

    @Test
    fun testTime() {

        val cal2 = Calendar.getInstance()
        cal2.timeInMillis = System.currentTimeMillis()
        var notedTime = cal2.timeInMillis
        cal2.add(Calendar.SECOND, 15)
        var currentTime = cal2.timeInMillis
        var value = ""

        var akamaiValue = setExpire(
                { currentTime },
                { notedTime },
                { time -> notedTime = time },
                { value = "1234" },
                { value }
        )

        assert(akamaiValue.equals("1234"))

        cal2.add(Calendar.SECOND, 1)
        currentTime = cal2.timeInMillis

        akamaiValue = setExpire(
                { currentTime },
                { notedTime },
                { time -> notedTime = time },
                { value = "12345" },
                { value }
        )

        assert(akamaiValue.equals("1234"))

        cal2.add(Calendar.SECOND, 9)
        currentTime = cal2.timeInMillis

        akamaiValue = setExpire(
                { currentTime },
                { notedTime },
                { time -> notedTime = time },
                { value = "12345" },
                { value }
        )

        assert(akamaiValue.equals("12345"))


    }


}