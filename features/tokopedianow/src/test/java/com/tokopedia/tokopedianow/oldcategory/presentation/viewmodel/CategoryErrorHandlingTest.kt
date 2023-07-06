package com.tokopedia.tokopedianow.oldcategory.presentation.viewmodel

import com.tokopedia.tokopedianow.searchcategory.ErrorHandlingTestHelper
import com.tokopedia.tokopedianow.searchcategory.ErrorHandlingTestHelper.Callback
import io.mockk.every
import org.junit.Test

class CategoryErrorHandlingTest: CategoryTestFixtures(), Callback {

    private lateinit var errorHandlingTestHelper: ErrorHandlingTestHelper

    override fun setUp() {
        super.setUp()

        errorHandlingTestHelper = ErrorHandlingTestHelper(
                tokoNowCategoryViewModel,
                this
        )
    }

    override fun `Given first page will error`(throwable: Throwable) {
        every {
            getCategoryFirstPageUseCase.execute(any(), any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }
    }

    @Test
    fun `Test first page error`() {
        errorHandlingTestHelper.`Test first page error`()
    }
}
