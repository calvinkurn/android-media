package com.tokopedia.home_page_banner.presenter.callback

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.home_page_banner.presenter.model.BannerModel

class BannerDiffCallback(
        private val oldBannerList: List<BannerModel>,
        private val newBannerList: List<BannerModel>
) : DiffUtil.Callback(){

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldBannerList[oldItemPosition] == newBannerList[newItemPosition]
    }

    override fun getOldListSize(): Int {
        return oldBannerList.size
    }

    override fun getNewListSize(): Int {
        return newBannerList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldBannerList[oldItemPosition].url == newBannerList[newItemPosition].url
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}