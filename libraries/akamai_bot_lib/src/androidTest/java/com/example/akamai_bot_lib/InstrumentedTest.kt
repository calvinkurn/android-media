package com.example.akamai_bot_lib


import android.util.Log
import android.util.Pair
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.akamai_bot_lib.getQueryListFromQueryString
import com.tokopedia.akamai_bot_lib.registeredGqlFunctions
import com.tokopedia.akamai_bot_lib.test.R
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.reflect.Field


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedTest {

    fun listRaw(): Pair<Array<String>, Array<String>> {
        val context = InstrumentationRegistry.getInstrumentation().context
        val resources = context.resources
        val fields: Array<Field> = R.raw::class.java.getFields()
        val names = Array(fields.size){
            ""
        }
        val allResourcesIds = Array(fields.size){
            ""
        }
        for (count in fields.indices) {
            try {
                val resourceID = fields[count].getInt(fields[count])
                val input = GraphqlHelper.loadRawString(resources, resourceID)
                allResourcesIds[count] = input
                names[count] = fields[count].name
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
        return Pair(allResourcesIds, names)
    }

    @Test
    fun getAllQueryTest() {
        var whatitget: String? = null
        val pair = listRaw()
        for (i in 0 until pair.first.size) {
            val input = pair.first[i]
            val name = pair.second[i]
            try {
                val xTkpdAkamai = getQueryListFromQueryString(input)
                        .asSequence()
                        .filter { it ->
                            registeredGqlFunctions.containsKey(it)
                        }.take(1).map { it ->
                            registeredGqlFunctions[it]
                        }.firstOrNull()
                Log.d(this.javaClass.name, xTkpdAkamai + " ][ name ][ " + name + " ")
                whatitget = xTkpdAkamai
            } catch (e: Exception) {
                Log.e("EIT", "$e ][ $whatitget")
            }
        }
    }
}