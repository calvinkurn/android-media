package com.tokopedia.vouchercreation.common.bottmsheet.tipstrick.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking.sendVoucherDetailImpressionTracking
import com.tokopedia.vouchercreation.common.bottmsheet.tipstrick.model.TipsTrickCarouselModel
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import kotlinx.android.synthetic.main.item_mvc_tips_trick_carousel.view.*

/**
 * Created By @ilhamsuaib on 08/05/20
 */

class TipsTrickCarouselAdapter(private val userId: String) : RecyclerView.Adapter<TipsTrickCarouselAdapter.ViewHolder>() {

    private var items: List<TipsTrickCarouselModel> = emptyList()

    companion object {
        private const val CARD_WIDTH_MULTIPLIER = 3
    }

    fun setItems(items: List<TipsTrickCarouselModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_mvc_tips_trick_carousel, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: TipsTrickCarouselModel) = with(itemView) {
            addOnImpressionListener(item.impressHolder) {
                sendVoucherDetailImpressionTracking(
                        status = VoucherStatusConst.ONGOING,
                        action = VoucherCreationAnalyticConstant.EventAction.Impression.VOUCHER_DETAIL_DISPLAY,
                        label = item.title,
                        userId = userId
                )
            }

            val dp12 = context.resources.getDimension(R.dimen.mvc_dimen_12dp)
            val cardWidth = getScreenWidth().minus(dp12.toInt() * CARD_WIDTH_MULTIPLIER)
            parentMvcCarousel.layoutParams.width = cardWidth
            parentMvcCarousel.requestLayout()
            imgMvcVoucherCarousel.layoutParams.width = cardWidth.minus(dp12.toInt())
            imgMvcVoucherCarousel.requestLayout()
            ImageHandler.loadImageRounded2(context, imgMvcVoucherCarousel, item.imageUrl, dp12)
            tvMvcVoucherCarousel.text = item.title
        }
    }
}