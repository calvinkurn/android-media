package com.tokopedia.search.result.product.changeview

import com.tokopedia.search.result.product.changeview.ViewType.BIG_GRID
import com.tokopedia.search.result.product.changeview.ViewType.LIST
import com.tokopedia.search.result.product.changeview.ViewType.SMALL_GRID
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class ViewTypeTest {

    @Test
    fun `get view type by value`() {
        assertThat(ViewType.of(1), `is`(SMALL_GRID))
        assertThat(ViewType.of(2), `is`(LIST))
        assertThat(ViewType.of(3), `is`(BIG_GRID))
    }

    @Test
    fun `small grid will change to list`() {
        assertThat(SMALL_GRID.change(), `is`(LIST))
    }

    @Test
    fun `list will change to big grid`() {
        assertThat(LIST.change(), `is`(BIG_GRID))
    }

    @Test
    fun `big grid will change to small grid`() {
        assertThat(BIG_GRID.change(), `is`(SMALL_GRID))
    }
}
