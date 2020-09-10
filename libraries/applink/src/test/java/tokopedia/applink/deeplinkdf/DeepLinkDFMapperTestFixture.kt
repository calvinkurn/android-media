package tokopedia.applink.deeplinkdf

import com.tokopedia.applink.DFP
import com.tokopedia.applink.DeeplinkDFMapper
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStream
import java.io.InputStreamReader

abstract class DeepLinkDFMapperTestFixture {

    @Before
    fun setup() {
        mockkObject(DeeplinkDFMapper)
    }

    @After
    fun finish() {
        unmockkAll()
    }

    protected fun getDeepLinkIdFromDeepLink(dfpList: List<DFP>?, path: InputStream?): List<DFP>? {
        val turnedOnDfSet = path?.let { getDFFilterMap(it) }
        if (turnedOnDfSet == null || turnedOnDfSet.isEmpty()) {
            return listOf()
        }
        val resultList = mutableListOf<DFP>()
        dfpList?.forEach {
            if (turnedOnDfSet.contains(it.moduleId)) {
                resultList.add(it)
            }
        }
        return resultList
    }

    private fun getDFFilterMap(path: InputStream): Set<String>? {
        return try {
            val set: HashSet<String> = hashSetOf()
            val reader = BufferedReader(InputStreamReader(path))
            var line: String? = reader.readLine()
            while (line != null) {
                if (line.isNotEmpty()) {
                    set.add(line)
                }
                line = reader.readLine()
            }
            set
        } catch (e: FileNotFoundException) {
            null
        }
    }
}