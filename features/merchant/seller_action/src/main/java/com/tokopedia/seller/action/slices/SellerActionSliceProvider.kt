package com.tokopedia.seller.action.slices

import android.net.Uri
import androidx.slice.Slice
import androidx.slice.SliceProvider
import com.tokopedia.seller.action.common.const.SellerActionConst
import com.tokopedia.seller.action.common.di.DaggerSellerActionComponent
import com.tokopedia.seller.action.common.dispatcher.SellerActionDispatcherProvider
import com.tokopedia.seller.action.domain.usecase.SliceMainOrderListUseCase
import com.tokopedia.seller.action.slices.item.SellerOrderSlice
import com.tokopedia.seller.action.slices.item.SellerSlice
import javax.inject.Inject

class SellerActionSliceProvider: SliceProvider(){

    companion object {
        internal const val SLICE_AUTHORITY = "com.tokopedia.seller.action.slices.provider"
    }

    @Inject
    lateinit var sliceMainOrderListUseCase: SliceMainOrderListUseCase

    @Inject
    lateinit var dispatcher: SellerActionDispatcherProvider

    private val lastSlices = mutableMapOf<Uri, SellerSlice>()

    override fun onCreateSliceProvider(): Boolean = context != null

    override fun onBindSlice(sliceUri: Uri): Slice {
        injectDependencies()
        return createNewSlice(sliceUri).getSlice()
    }

    private fun injectDependencies() {
        DaggerSellerActionComponent.builder()
                .build()
                .inject(this)
    }

    private fun createNewSlice(sliceUri: Uri): SellerSlice {
        val notNullContext = requireNotNull(context)
        return when (sliceUri.path) {
            SellerActionConst.Deeplink.ORDER -> SellerOrderSlice(
                    notNullContext,
                    sliceUri
            )
            else -> SellerOrderSlice(
                    notNullContext,
                    sliceUri
            )
        }

    }
}