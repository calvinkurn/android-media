package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.graphics.Color
import android.view.Gravity
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeProductCardCustomBannerV2Binding
import com.tokopedia.digital.home.model.RechargeHomepageProductCardCustomBannerV2Model
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.digital.home.presentation.util.ParallaxScrollEffectListener
import com.tokopedia.home_component.util.GravitySnapHelper
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recharge_component.digital_card.presentation.adapter.DigitalUnifyCardAdapterTypeFactory
import com.tokopedia.recharge_component.digital_card.presentation.adapter.viewholder.DigitalUnifyCardViewHolder
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalUnifyModel

class RechargeHomepageProductCardCustomBannerV2ViewHolder(
    val view: View,
    val listener: RechargeHomepageItemListener
): AbstractViewHolder<RechargeHomepageProductCardCustomBannerV2Model>(view) {

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_product_card_custom_banner_v2
    }

    private lateinit var section: RechargeHomepageSections.Section
    private val rvPool = RecyclerView.RecycledViewPool()

    override fun bind(element: RechargeHomepageProductCardCustomBannerV2Model) {
        val bind = ViewRechargeHomeProductCardCustomBannerV2Binding.bind(itemView)
        section = element.section
        if (section.items.isNotEmpty()){
            hideShimmer(bind)
            setupInitialView(bind, section)
            setupList(bind, element.digitalUnifyItems)
            setSnapEffect(bind)
        }else{
            showShimmer(bind)
            listener.loadRechargeSectionData(element.visitableId())
        }
    }

    private fun setupInitialView(
        bind: ViewRechargeHomeProductCardCustomBannerV2Binding,
        section: RechargeHomepageSections.Section
    ){
        with(bind){
            try {
                contentContainer.setCardBackgroundColor(Color.parseColor(section.label2))
            }catch (e: Throwable){
                e.printStackTrace()
            }
            parallaxImage.loadImage(section.mediaUrl)
            tvSectionTitle.text = section.title
            tvSectionSeeAll.text = section.textLink

            tvSectionSeeAll.setOnClickListener {
                listener.onRechargeBannerAllItemClicked(section)
            }
        }
    }

    private fun setupList(
        bind: ViewRechargeHomeProductCardCustomBannerV2Binding,
        element: List<DigitalUnifyModel>
    ){
        val layoutManagers = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        with(bind){
            parallaxImage.alpha = 1f
            with(rvRechargeProduct){
                resetLayout()
                setRecycledViewPool(rvPool)
                layoutManager = layoutManagers
                adapter = BaseAdapter(
                    DigitalUnifyCardAdapterTypeFactory(digitalUnifyCardListener),
                    element
                )
                addOnScrollListener(object : ParallaxScrollEffectListener(layoutManagers){
                    override fun translatedX(translatedX: Float) {
                        bind.parallaxView.translationX = translatedX
                    }

                    override fun setAlpha(alpha: Float) {
                        bind.parallaxImage.alpha = alpha
                    }

                    override fun getPixelSize(): Int =
                        itemView.resources.getDimensionPixelSize(com.tokopedia.digital.home.R.dimen.product_card_custom_banner_width)
                })
            }
        }
    }

    private fun setSnapEffect(bind: ViewRechargeHomeProductCardCustomBannerV2Binding){
        val snapHelper = GravitySnapHelper(Gravity.START)
        snapHelper.attachToRecyclerView(bind.rvRechargeProduct)
    }

    private fun showShimmer(bind: ViewRechargeHomeProductCardCustomBannerV2Binding){
        with(bind){
            tvSectionSeeAll.hide()
            tvSectionTitle.hide()
            contentContainer.hide()
            viewRechargeHomeProductCardsShimmering.root.visible()
        }
    }

    private fun hideShimmer(bind: ViewRechargeHomeProductCardCustomBannerV2Binding){
        with(bind){
            tvSectionSeeAll.visible()
            tvSectionTitle.visible()
            contentContainer.visible()
            viewRechargeHomeProductCardsShimmering.root.hide()
        }
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

            override fun onItemImpression(item: DigitalUnifyModel, index: Int) {

            }
        }
}