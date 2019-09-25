package com.tokopedia.globalnavwidget

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class GlobalNavWidgetTest: Spek({

    describe("global nav widget") {

        it("should do something") {
            assertThat(2 + 2, `is`(3))
        }
    }
})