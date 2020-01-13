package com.tokopedia.home_page_banner.presenter.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_page_banner.presenter.adapter.viewHolder.HomePageBannerViewHolder

class HomePageBannerAdapter(
        private val clickCallback: (Int) -> Unit
) : RecyclerView.Adapter<HomePageBannerViewHolder>(){
    private val imagesUrl = mutableListOf<String>()
    private var firstInitial = true
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePageBannerViewHolder {
        return HomePageBannerViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return if (imagesUrl.size == 0) 0 else imagesUrl.size + 2
    }

    fun getRealCount() = itemCount - 2

    override fun onBindViewHolder(holder: HomePageBannerViewHolder, position: Int) {
        val imagePosition = getImagesUrl(position)
        return holder.bind(imageUrl = imagesUrl[imagePosition], clickListener = clickCallback)
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

    fun setItem(list: List<String>){
        imagesUrl.clear()
        imagesUrl.addAll(list)
        notifyDataSetChanged()
    }
}