package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcarditem

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow


class ProductCardItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private lateinit var productImage: ImageView
    private lateinit var productName: TextView
    private lateinit var labelDiscount: TextView
    private lateinit var textViewSlashedPrice: TextView
    private lateinit var textViewPrice: TextView
    private lateinit var textViewShopName: TextView
    private lateinit var shopImage: ImageView
    private lateinit var productCardItemViewModel: ProductCardItemViewModel

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        productCardItemViewModel = discoveryBaseViewModel as ProductCardItemViewModel
        initView()
        productCardItemViewModel.getDataItemValue().observe(fragment.viewLifecycleOwner, Observer {
            populateData(it)
        })

    }

    private fun populateData(dataItem: DataItem) {
        ImageHandler.LoadImage(productImage, dataItem.imageUrlMobile)
        ImageHandler.LoadImage(shopImage, dataItem.shopLogo)
        productName.setTextAndCheckShow(dataItem.name)
        textViewShopName.setTextAndCheckShow(dataItem.shopName)
        textViewPrice.setTextAndCheckShow(dataItem.price)
    }

    private fun initView() {
        productImage = itemView.findViewById(R.id.imageProduct)
        productName = itemView.findViewById(R.id.textViewProductName)
        shopImage = itemView.findViewById(R.id.imageShop)
        textViewShopName = itemView.findViewById(R.id.textViewShopName)
        textViewPrice = itemView.findViewById(R.id.textViewPrice)
    }
}