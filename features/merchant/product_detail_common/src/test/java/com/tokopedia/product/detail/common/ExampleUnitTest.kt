package com.tokopedia.product.detail.common

import com.tokopedia.product.detail.common.bmgm.ui.model.BMGMUiModel
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    fun hashCodeChecker() {
        var bmgm = BMGMUiModel.Product(imageUrl = "aaaa")
        println(bmgm.hashCode())
        bmgm = bmgm.copy(loadMoreText = "bbb")
        println(bmgm.hashCode())
        bmgm = bmgm.copy(loadMoreText = "bbb")
        println(bmgm.hashCode())
        bmgm = bmgm.copy(loadMoreText = "aaa")
        println(bmgm.hashCode())
        bmgm = bmgm.copy(loadMoreText = "bbb", imageUrl = "cccc")
        println(bmgm.hashCode())
    }
}
