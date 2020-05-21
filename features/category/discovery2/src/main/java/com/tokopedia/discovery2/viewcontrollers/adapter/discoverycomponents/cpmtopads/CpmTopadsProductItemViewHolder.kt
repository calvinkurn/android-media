package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.cpmtopads

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.unifyprinciples.Typography

class CpmTopadsProductItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {


    private lateinit var cpmTopadsProductItemViewModel: CpmTopadsProductItemViewModel

    private val productImage: ImageView = itemView.findViewById(R.id.product_image)
    private val productName: Typography = itemView.findViewById(R.id.product_name)
    private val productPrice: Typography = itemView.findViewById(R.id.product_price)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        cpmTopadsProductItemViewModel = discoveryBaseViewModel as CpmTopadsProductItemViewModel
        cpmTopadsProductItemViewModel.getComponent().observe(fragment.viewLifecycleOwner, Observer { item ->
            val data = item.data?.getOrElse(0) { DataItem() }
            ImageHandler.LoadImage(productImage, data?.imageUrlMobile)
            productName.text = data?.name
            productPrice.text = data?.priceFormat
            setClick(data?.applinks, data?.imageClickUrl)
        })

    }

    private fun setClick(applinks: String?, imageClickUrl: String?) {
        if (!applinks.isNullOrEmpty()) {
            itemView.setOnClickListener {
                RouteManager.route(itemView.context, applinks)
//                ImpresionTask().execute(imageClickUrl)
            }
        }
    }


}