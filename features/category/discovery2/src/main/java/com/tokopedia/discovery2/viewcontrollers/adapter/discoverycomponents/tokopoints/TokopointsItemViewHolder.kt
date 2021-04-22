package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tokopoints

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.media.loader.loadImageWithoutPlaceholder

class TokopointsItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView), View.OnClickListener {

    private lateinit var tokopointsItemViewModel: TokopointsItemViewModel
    private lateinit var bannerImageView: ImageView

    private lateinit var couponTitleTv: TextView
    private lateinit var slashedPriceTv: TextView
    private lateinit var pointsValueTv: TextView
    private lateinit var discountPriceTv: TextView

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        tokopointsItemViewModel = discoveryBaseViewModel as TokopointsItemViewModel

        itemView.setOnClickListener(this)
        tokopointsItemViewModel.getDataItemValue().observe(fragment.viewLifecycleOwner, Observer { item ->
            initView()
            couponTitleTv.text = item.title
            bannerImageView.loadImageWithoutPlaceholder(item.thumbnailUrlMobile ?: "")
            if (item.pointsSlash.toIntOrZero() > 0 && (item.pointsSlashStr ?: "").isNotEmpty()) {
                slashedPriceTv.text = item.pointsSlashStr
                pointsValueTv.text = item.pointsStr

                if (item?.discountPercentageStr?.isNotEmpty() == true) {
                    discountPriceTv.show()
                    discountPriceTv.text = item.discountPercentageStr
                } else {
                    resetDiscountData()
                }
            } else if (item.pointsStr?.isNotEmpty() == true) {
                slashedPriceTv.text = ""
                pointsValueTv.text = item.pointsStr
                resetDiscountData()
            }
        })
    }

    fun resetDiscountData() {
        discountPriceTv.text = ""
        discountPriceTv.invisible()
    }

    private fun initView() {
        bannerImageView = itemView.findViewById(R.id.img_banner)
        couponTitleTv = itemView.findViewById(R.id.coupon_title_tv)
        slashedPriceTv = itemView.findViewById(R.id.slashed_price_tv)
        pointsValueTv = itemView.findViewById(R.id.points_tv)
        discountPriceTv = itemView.findViewById(R.id.discount_tv)
    }

    override fun onClick(v: View?) {
        v?.context?.let { tokopointsItemViewModel.onTokopointsItemClicked(it) }
    }
}