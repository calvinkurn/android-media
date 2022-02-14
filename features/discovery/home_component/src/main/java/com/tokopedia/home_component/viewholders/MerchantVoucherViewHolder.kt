package com.tokopedia.home_component.viewholders

import android.view.Gravity
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.databinding.GlobalDcMerchantVoucherBinding
import com.tokopedia.home_component.decoration.SimpleHorizontalLinearLayoutDecoration
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMerchantVoucherDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselSeeMorePdpDataModel
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactoryImpl
import com.tokopedia.home_component.productcardgridcarousel.viewHolder.CarouselViewAllCardViewHolder
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.util.GravitySnapHelper
import com.tokopedia.home_component.viewholders.adapter.MerchantVoucherAdapter
import com.tokopedia.home_component.visitable.MerchantVoucherDataModel
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MerchantVoucherViewHolder(
    itemView: View
) : AbstractViewHolder<MerchantVoucherDataModel>(itemView), CommonProductCardCarouselListener,
    CoroutineScope {
    private var binding: GlobalDcMerchantVoucherBinding? by viewBinding()
    private var adapter: MerchantVoucherAdapter? = null
    private val startSnapHelper: GravitySnapHelper by lazy { GravitySnapHelper(Gravity.START) }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.global_dc_merchant_voucher
        val RECYCLER_VIEW_ID = R.id.recycleList
        private const val FPM_MIX_LEFT = "home_merchant_voucher"
    }

    override fun bind(element: MerchantVoucherDataModel) {
        setHeaderComponent(element = element)
        setChannelDivider(element)
        mappingView(element.channelModel)
    }

    private fun mappingView(channel: ChannelModel) {
        val visitables: MutableList<Visitable<*>> = mappingVisitablesFromChannel(channel)
        binding?.recycleList?.setHasFixedSize(true)
        valuateRecyclerViewDecoration()
//
//        mappingHeader(channel)
//        mappingCtaButton(channel.channelBanner.cta)
        mappingItem(channel, visitables)
    }

    private fun mappingVisitablesFromChannel(channel: ChannelModel): MutableList<Visitable<*>> {
        val visitables: MutableList<Visitable<*>> = mutableListOf()
        val channelMerchantVoucherData = convertDataToMerchantVoucherData(channel)
        setRecyclerViewAndCardHeight()
        visitables.addAll(channelMerchantVoucherData)
        if(channel.channelGrids.size > 1 && channel.channelHeader.applink.isNotEmpty()) {
            if(channel.channelViewAllCard.id != CarouselViewAllCardViewHolder.DEFAULT_VIEW_ALL_ID && channel.channelViewAllCard.contentType.isNotBlank() && channel.channelViewAllCard.contentType != CarouselViewAllCardViewHolder.CONTENT_DEFAULT) {
                visitables.add(
                    CarouselMerchantVoucherDataModel()
                )
            }
            else {
                visitables.add(CarouselSeeMorePdpDataModel(channel.channelHeader.applink, channel.channelHeader.backImage, this))
            }
        }
        return visitables
    }

    private fun mappingItem(channel: ChannelModel, visitables: MutableList<Visitable<*>>) {
        startSnapHelper.attachToRecyclerView(binding?.recycleList)
        val typeFactoryImpl = CommonCarouselProductCardTypeFactoryImpl(channel)
        adapter = MerchantVoucherAdapter(visitables, typeFactoryImpl)
        binding?.recycleList?.adapter = adapter
    }

    private fun convertDataToMerchantVoucherData(channel: ChannelModel): List<CarouselMerchantVoucherDataModel> {
        val list :MutableList<CarouselMerchantVoucherDataModel> = mutableListOf()
        for (element in channel.channelGrids) {
            list.add(CarouselMerchantVoucherDataModel())
        }
        return list
    }

    private fun valuateRecyclerViewDecoration() {
        if (binding?.recycleList?.itemDecorationCount == 0) binding?.recycleList?.addItemDecoration(
            SimpleHorizontalLinearLayoutDecoration()
        )
        binding?.recycleList?.layoutManager = LinearLayoutManager(
            itemView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        /**
         * Attach startSnapHelper to recyclerView
         */
        startSnapHelper.attachToRecyclerView(binding?.recycleList)
    }

    private fun setChannelDivider(element: MerchantVoucherDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = binding?.homeComponentDividerHeader,
            dividerBottom = binding?.homeComponentDividerFooter
        )
    }

    private fun setHeaderComponent(element: MerchantVoucherDataModel) {
        binding?.homeComponentHeaderView?.setChannel(element.channelModel, object : HeaderListener {
            override fun onSeeAllClick(link: String) {

            }

            override fun onChannelExpired(channelModel: ChannelModel) {

            }
        })
    }

    private fun setRecyclerViewAndCardHeight() {
        launch {
            try {
                binding?.recycleList?.setHeightBasedMerchantVoucherCardHeight()
            }
            catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

    private fun RecyclerView.setHeightBasedMerchantVoucherCardHeight() {
        val carouselLayoutParams = this.layoutParams
        carouselLayoutParams?.height = this.resources.getDimensionPixelOffset(com.tokopedia.home_component.R.dimen.home_merchant_voucher_height)
        this.layoutParams = carouselLayoutParams
    }

    override fun onProductCardImpressed(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun onProductCardClicked(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int,
        applink: String
    ) {
        TODO("Not yet implemented")
    }

    override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {
        TODO("Not yet implemented")
    }

    override fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int) {
        TODO("Not yet implemented")
    }

    private val masterJob = SupervisorJob()

    override val coroutineContext = masterJob + Dispatchers.Main
}