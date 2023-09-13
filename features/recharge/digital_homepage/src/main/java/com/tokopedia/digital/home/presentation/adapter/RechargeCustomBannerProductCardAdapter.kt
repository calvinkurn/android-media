package com.tokopedia.digital.home.presentation.adapter

import android.graphics.Paint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeProductCardCustomBannerItemBinding
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageRounded

class RechargeCustomBannerProductCardAdapter(
    val items: List<RechargeHomepageSections.Item>,
    val listener: RechargeHomepageItemListener
) :
    RecyclerView.Adapter<RechargeCustomBannerProductCardAdapter.ViewHolder>() {

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ViewRechargeHomeProductCardCustomBannerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(val binding: ViewRechargeHomeProductCardCustomBannerItemBinding, val listener: RechargeHomepageItemListener) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(element: RechargeHomepageSections.Item) {
            with(binding) {
                tvRechargeProductTitle.text = element.title
                tvRechargeProductContent.text = MethodChecker.fromHtml(element.subtitle)

                tvRechargeProductOriPrice.text = MethodChecker.fromHtml(element.label1)
                tvRechargeProductOriPrice.paintFlags = tvRechargeProductDiscountPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                tvRechargeProductDiscountPrice.text = MethodChecker.fromHtml(element.label2)

                ivRechargeProductImage.loadImageRounded(element.mediaUrl)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ivRechargeProductImage.clipToOutline = true
                }

                if (element.label3.isNotEmpty()) {
                    tvRechargeProductTag.text = element.label3
                    tvRechargeProductTag.show()
                } else {
                    tvRechargeProductTag.hide()
                }

                root.setOnClickListener {
                    listener.onRechargeSectionItemClicked(element)
                }
            }
        }
    }

    companion object {
        val PRODUCT_CARD_LAYOUT = R.layout.view_recharge_home_product_card_custom_banner_item
    }
}
