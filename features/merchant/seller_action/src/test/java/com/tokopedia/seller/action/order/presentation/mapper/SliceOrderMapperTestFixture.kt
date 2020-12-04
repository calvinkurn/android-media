package com.tokopedia.seller.action.order.presentation.mapper

import android.content.Context
import android.net.Uri
import com.tokopedia.seller.action.common.presentation.slices.SellerFailureSlice
import com.tokopedia.seller.action.common.presentation.slices.SellerLoadingSlice
import com.tokopedia.seller.action.common.presentation.slices.SellerNotLoginSlice
import com.tokopedia.seller.action.common.presentation.slices.SellerSuccessSlice
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.mockito.ArgumentMatchers.anyInt

open class SliceOrderMapperTestFixture {

    open lateinit var mapper: SellerOrderMapper

    @RelaxedMockK
    lateinit var context: Context

    @RelaxedMockK
    lateinit var uri: Uri

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mapper = SellerOrderMapper(context, uri).apply {
            every { context.getString(anyInt()) } returns ""
        }
    }

    protected fun assertSliceIsSuccess(slice: Any?) {
        assert(slice is SellerSuccessSlice<*>)
    }
    protected fun assertSliceIsFail(slice: Any?) {
        assert(slice is SellerFailureSlice)
    }
    protected fun assertSliceIsLoading(slice: Any?) {
        assert(slice is SellerLoadingSlice)
    }
    protected fun assertSliceIsNotLogin(slice: Any?) {
        assert(slice is SellerNotLoginSlice)
    }

}