package com.tokopedia.seller.action.order.presentation.mapper

import com.tokopedia.seller.action.common.presentation.model.SellerActionStatus
import org.junit.Test

class SliceOrderMapperTest: SliceOrderMapperTestFixture() {

    @Test
    fun `success status will return success slice`() {
        val returnSlice = mapper.getSlice(SellerActionStatus.Success(listOf()))
        assertSliceIsSuccess(returnSlice)
    }

    @Test
    fun `fail status will return fail slice`() {
        val returnSlice = mapper.getSlice(SellerActionStatus.Fail)
        assertSliceIsFail(returnSlice)
    }

    @Test
    fun `loading status will return loading slice`() {
        val returnSlice = mapper.getSlice(SellerActionStatus.Loading)
        assertSliceIsLoading(returnSlice)
    }

    @Test
    fun `not login status will return no login slice`() {
        val returnSlice = mapper.getSlice(SellerActionStatus.NotLogin)
        assertSliceIsNotLogin(returnSlice)
    }

}