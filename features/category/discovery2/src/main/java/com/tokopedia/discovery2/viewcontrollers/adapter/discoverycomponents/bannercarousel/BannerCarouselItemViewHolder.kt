package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannercarousel

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.show

private const val ITEM_DECORATION = 40
private const val DEFAULT_DESIGN = 2.2

class BannerCarouselItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private val parentView: ConstraintLayout = itemView.findViewById(R.id.parent_layout)
    private var titleTextView: TextView = itemView.findViewById(R.id.subTitle_tv)
    private val bannerImage: ImageView = itemView.findViewById(R.id.banner_image)
    private lateinit var bannerCarouselItemViewModel: BannerCarouselItemViewModel
    private val displayMetrics = getDisplayMetric(fragment.context)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        bannerCarouselItemViewModel = discoveryBaseViewModel as BannerCarouselItemViewModel
        parentView.setOnClickListener {
            bannerCarouselItemViewModel.getNavigationUrl()?.let {
                RouteManager.route(fragment.activity, it)
                bannerCarouselItemViewModel.getBannerData()?.let { itemData ->
                    (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackCarouselBannerClick(itemData, adapterPosition)
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
                        val itemData = it[0]
                        try {
                            parentView.layoutParams.width = ((displayMetrics.widthPixels - ITEM_DECORATION) / if (componentItem.design.isEmpty()) DEFAULT_DESIGN else componentItem.design.toDouble() ).toInt()
                            bannerImage.layoutParams.height = parentView.layoutParams.width
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

            })
        }
    }

    private fun getDisplayMetric(context: Context?): DisplayMetrics {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics
    }
}