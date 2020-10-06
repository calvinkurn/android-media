package com.tokopedia.seller.action.slices

import android.net.Uri
import androidx.slice.Slice
import androidx.slice.SliceProvider

class SellerActionSliceProvider: SliceProvider(){

    companion object {
        internal const val SLICE_AUTHORITY = "com.tokopedia.seller.action.slices.provider"
    }

    override fun onCreateSliceProvider(): Boolean = true

    override fun onBindSlice(sliceUri: Uri?): Slice {
        TODO("Not yet implemented")
    }
}