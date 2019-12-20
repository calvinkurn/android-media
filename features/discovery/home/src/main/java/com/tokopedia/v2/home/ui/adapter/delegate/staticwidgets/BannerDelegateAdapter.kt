package com.tokopedia.v2.home.ui.adapter.delegate.staticwidgets

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.banner.BannerView
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.ActivityStateListener
import com.tokopedia.v2.home.base.adapterdelegate.ModelViewType
import com.tokopedia.v2.home.base.adapterdelegate.ViewTypeDelegateAdapter
import com.tokopedia.v2.home.base.adapterdelegate.inflate
import com.tokopedia.v2.home.model.pojo.home.BannerSlide
import com.tokopedia.v2.home.model.vo.BannerDataModel
import kotlinx.android.synthetic.main.home_banner.view.*
import java.util.*

class BannerDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return BannerViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ModelViewType) {
        holder as BannerViewHolder
        holder.bind(item as BannerDataModel)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ModelViewType, payload: List<Any>) {
        if(payload.isNotEmpty() && holder is BannerViewHolder){
            holder.bind(item as BannerDataModel)
        }
    }

    override fun isForViewType(item: ModelViewType): Boolean {
        return item is BannerDataModel
    }

    inner class BannerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.home_banner)), BannerView.OnPromoClickListener,
            BannerView.OnPromoScrolledListener, BannerView.OnPromoAllClickListener,
            BannerView.OnPromoLoadedListener, BannerView.OnPromoDragListener, ActivityStateListener {

        private val bannerView = itemView.banner
        private var slidesList: List<BannerSlide> = arrayListOf()

        init {
            bannerView.onPromoAllClickListener = this
            bannerView.onPromoClickListener = this
            bannerView.onPromoScrolledListener = this
            bannerView.setOnPromoLoadedListener(this)
            bannerView.setOnPromoDragListener(this)

        }

        fun bind(item: BannerDataModel) {
            try {
                slidesList = item.banner.slides
                slidesList.let {
//                    bannerView.addOnImpressionListener(
//                            item,
//                            com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.BannerViewHolder.OnBannerImpressedListener(it, listener)
//                    )

                    bannerView.shouldShowSeeAllButton(it.isNotEmpty())

                    val promoUrls = ArrayList<String>()
                    for (slidesModel in it) {
                        promoUrls.add(slidesModel.imageUrl)
                    }
                    bannerView.setPromoList(promoUrls)
                    bannerView.buildView()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onPromoClick(p0: Int) {

        }

        override fun onPromoScrolled(p0: Int) {

        }

        override fun onPromoAllClick() {

        }

        override fun onPromoLoaded() {

        }

        override fun onPromoDragEnd() {

        }

        override fun onPromoDragStart() {

        }

        override fun onPause() {

        }

        override fun onResume() {

        }
    }
}