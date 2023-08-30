package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.SomBottomSheet
import com.tokopedia.sellerorder.databinding.PartialInfoLayoutBinding

class SomConfirmShippingBottomSheet(context: Context) : SomBottomSheet<PartialInfoLayoutBinding>(
    LAYOUT, true, true, false, true, false, context.getString(R.string.automatic_shipping), context, true
) {

    private var bottomSheetAdapter: SomBottomSheetConfirmShippingAdapter? = null

    companion object {
        private val LAYOUT = R.layout.partial_info_layout
    }

    override fun bind(view: View): PartialInfoLayoutBinding {
        return PartialInfoLayoutBinding.bind(view)
    }

    override fun setupChildView() {
        bottomSheetAdapter = SomBottomSheetConfirmShippingAdapter()
        binding?.apply {
            rvInfo.apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                adapter = bottomSheetAdapter
            }
        }

    }

    fun setInfoText(listInfo: List<String>) {

        binding?.imageBackground?.loadImage("https://images.tokopedia.net/img/img_bottomsheet_dropoff.png")
        bottomSheetAdapter?.updateListInfo(listInfo)
    }
}
