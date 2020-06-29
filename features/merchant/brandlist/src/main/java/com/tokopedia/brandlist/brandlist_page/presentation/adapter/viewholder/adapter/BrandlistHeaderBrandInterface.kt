package com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.adapter

import android.os.Parcelable

interface BrandlistHeaderBrandInterface {

    fun onClickedChip(position: Int, chipName: String, current: Long, recyclerViewState: Parcelable?)

}