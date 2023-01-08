package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannercarousel

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageWithoutPlaceholder

private const val DEFAULT_DESIGN = 2.1

class BannerCarouselItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private val parentView: ConstraintLayout = itemView.findViewById(R.id.parent_layout)
    private var titleTextView: TextView = itemView.findViewById(R.id.subTitle_tv)
    private var bannerImage: ImageView = itemView.findViewById(R.id.banner_image)
    private lateinit var bannerCarouselItemViewModel: BannerCarouselItemViewModel
    private val displayMetrics = Utils.getDisplayMetric(fragment.context)
    private var compType :String? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        bannerCarouselItemViewModel = discoveryBaseViewModel as BannerCarouselItemViewModel
        parentView.setOnClickListener {
            bannerCarouselItemViewModel.getNavigationUrl()?.let {
                RouteManager.route(fragment.activity, it)
                bannerCarouselItemViewModel.getBannerData()?.let { itemData ->
                    (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackCarouselBannerClick(itemData, adapterPosition, compType ?: "")
                }
            }
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            bannerCarouselItemViewModel.getComponentLiveData().observe(fragment.viewLifecycleOwner, Observer { componentItem ->
                componentItem.data?.let {
                    if (it.isNotEmpty()) {
                        setupImage(it.first(),componentItem)
                    }
                }
                compType = componentItem.properties?.compType
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            bannerCarouselItemViewModel.getComponentLiveData().removeObservers(it)
        }
    }

    private fun setupImage(itemData: DataItem,componentItem:ComponentsItem){
        try {
            val layoutParams: ViewGroup.LayoutParams = bannerImage.layoutParams
            layoutParams.width = ((displayMetrics.widthPixels - itemView.context.resources.getDimensionPixelSize(R.dimen.carousel_gap))
                    / if (componentItem.design.isEmpty()) DEFAULT_DESIGN else componentItem.design.toDouble()).toInt()
            val height = Utils.extractDimension(itemData.image,Constant.Dimensions.HEIGHT)
            val width = Utils.extractDimension(itemData.image,Constant.Dimensions.WIDTH)
            if (width != null && height != null && width != 1 && height != 1) {
                val aspectRatio = width.toFloat() / height.toFloat()
                layoutParams.height = (layoutParams.width / aspectRatio).toInt()
            } else {
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            bannerImage.layoutParams = layoutParams
            bannerImage.loadImageWithoutPlaceholder(itemData.image)
            itemData.description?.let { title ->
                if (title.isEmpty()) {
                    titleTextView.hide()
                } else {
                    titleTextView.text = title
                    titleTextView.show()
                }
            }
        } catch (exception: NumberFormatException) {
            parentView.hide()
            exception.printStackTrace()
        }
    }
}