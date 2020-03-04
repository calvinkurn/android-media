package com.tokopedia.home_page_banner.presenter.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_page_banner.presenter.adapter.viewHolder.HomePageBannerViewHolder
import com.tokopedia.home_page_banner.presenter.callback.BannerDiffCallback
import com.tokopedia.home_page_banner.presenter.model.BannerModel

class HomePageBannerAdapter(
        private val clickCallback: (Int) -> Unit
) : RecyclerView.Adapter<HomePageBannerViewHolder>(){
    companion object{
        private const val OFFSET_LEFT_N_RIGHT = 2
    }
    private val bannerModels = mutableListOf<BannerModel>()
    private var firstInitial = true
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePageBannerViewHolder {
        return HomePageBannerViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return if (bannerModels.size == 0) 0 else bannerModels.size + OFFSET_LEFT_N_RIGHT
    }

    fun getRealCount() = itemCount - OFFSET_LEFT_N_RIGHT

    override fun onBindViewHolder(holder: HomePageBannerViewHolder, position: Int) {
        val imagePosition = getImagesUrl(position)
        holder.bind(imageUrl = bannerModels[imagePosition].url, clickListener = clickCallback)
    }

    private fun getImagesUrl(position: Int): Int{
        if(firstInitial) {
            firstInitial = false
            return 0
        }
        if (position == 0) {
            return getRealCount() - 1
        }
        // Put first page model to the last position.
        if (position > getRealCount() ) {
            return 0
        }
        return position - 1
    }

    fun setItem(list: List<BannerModel>){
        val callback = BannerDiffCallback(bannerModels, list)
        val diffResult = DiffUtil.calculateDiff(callback)
        bannerModels.clear()
        bannerModels.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    fun getList() = bannerModels
}