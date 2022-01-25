package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.Gravity
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeProductCardCustomBannerV2Binding
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.model.RechargeProductCardUnifyModel
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.digital.home.presentation.util.ParallaxScrollEffectListener
import com.tokopedia.home_component.util.GravitySnapHelper
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recharge_component.digital_card.presentation.adapter.DigitalUnifyCardAdapterTypeFactory
import com.tokopedia.recharge_component.digital_card.presentation.adapter.viewholder.DigitalUnifyCardViewHolder
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalUnifyModel

class RechargeHomepageProductCardCustomBannerV2ViewHolder(
    val view: View,
    val listener: RechargeHomepageItemListener
): AbstractViewHolder<RechargeProductCardUnifyModel>(view) {

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_product_card_custom_banner_v2
    }

    private lateinit var section: RechargeHomepageSections.Section

    override fun bind(element: RechargeProductCardUnifyModel) {
        val bind = ViewRechargeHomeProductCardCustomBannerV2Binding.bind(itemView)
        section = element.section

        if (section.items.isNotEmpty()){
            setupInitialView(bind, section)
            setupList(bind, element)
            setSnapEffect(bind)
        }else{
            listener.loadRechargeSectionData(element.visitableId())
        }
    }

    private fun setupInitialView(
        bind: ViewRechargeHomeProductCardCustomBannerV2Binding,
        section: RechargeHomepageSections.Section
    ){
        with(bind){
            parallaxImage.loadImage(section.mediaUrl)
            tvSectionTitle.text = section.title
            tvSectionSeeAll.text = section.textLink
        }
    }

    private fun setupList(
        bind: ViewRechargeHomeProductCardCustomBannerV2Binding,
        element: RechargeProductCardUnifyModel
    ){
        val layoutManagers = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        with(bind){
            parallaxImage.alpha = 1f
            with(rvRechargeProduct){
                resetLayout()
                layoutManager = layoutManagers
                adapter = BaseAdapter(
                    DigitalUnifyCardAdapterTypeFactory(digitalUnifyCardListener),
                    element.digitalUnifyItems
                )
                addOnScrollListener(object : ParallaxScrollEffectListener(layoutManagers){
                    override fun translatedX(translatedX: Float) {
                        bind.parallaxView.translationX = translatedX
                    }

                    override fun setAlpha(alpha: Float) {
                        bind.parallaxImage.alpha = alpha
                    }

                    override fun getPixelSize(): Int =
                        itemView.resources.getDimensionPixelSize(com.tokopedia.home_component.R.dimen.product_card_flashsale_width)
                })
            }
        }
    }

    private fun setSnapEffect(bind: ViewRechargeHomeProductCardCustomBannerV2Binding){
        val snapHelper = GravitySnapHelper(Gravity.START)
        snapHelper.attachToRecyclerView(bind.rvRechargeProduct)
    }

    private fun RecyclerView.resetLayout() {
        val carouselLayoutParams = this.layoutParams
        carouselLayoutParams?.height = RecyclerView.LayoutParams.WRAP_CONTENT
        this.layoutParams = carouselLayoutParams
    }

    private val digitalUnifyCardListener =
        object : DigitalUnifyCardViewHolder.DigitalUnifyCardListener {
            override fun onItemClicked(item: DigitalUnifyModel, index: Int) {
                if (section.items.size > index)
                    listener.onRechargeSectionItemClicked(section.items[index])
            }
        }
}