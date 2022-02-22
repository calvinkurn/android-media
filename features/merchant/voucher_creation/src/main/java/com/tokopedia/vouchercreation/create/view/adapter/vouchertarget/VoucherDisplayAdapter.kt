package com.tokopedia.vouchercreation.create.view.adapter.vouchertarget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking.sendCreateVoucherImpressionTracking
import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherTargetType
import com.tokopedia.vouchercreation.create.view.enums.VoucherCreationStep
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertarget.vouchertips.VoucherDisplayUiModel
import kotlinx.android.synthetic.main.mvc_voucher_display_view.view.*

class VoucherDisplayAdapter(private val itemList: List<VoucherDisplayUiModel>,
                            @VoucherTargetType private val targetType: Int,
                            private val userId: String) : RecyclerView.Adapter<VoucherDisplayAdapter.VoucherDisplayViewHolder>() {

    class VoucherDisplayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {
        private const val CARD_WIDTH_MULTIPLIER = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoucherDisplayViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.mvc_voucher_display_view, parent, false)
        return VoucherDisplayViewHolder(view)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: VoucherDisplayViewHolder, position: Int) {
        holder.itemView.run {
            val cardWidth = getScreenWidth() - context.resources.getDimension(R.dimen.mvc_create_voucher_display_recycler_view_decoration).toInt() * CARD_WIDTH_MULTIPLIER
            voucherDisplayImage?.layoutParams?.width = cardWidth
            itemList[position].let { uiModel ->
                voucherDisplayImage?.run {
                    Glide.with(context)
                            .load(uiModel.imageUrl)
                            .into(this)
                }
                val displayText = resources?.getString(uiModel.displayTextRes).toBlankOrString()
                voucherDisplayText?.text = displayText
                voucherDisplayImage?.addOnImpressionListener(uiModel.impressHolder) {
                    sendCreateVoucherImpressionTracking(
                            step = VoucherCreationStep.TARGET,
                            action =
                                    when(targetType) {
                                        VoucherTargetType.PUBLIC -> VoucherCreationAnalyticConstant.EventAction.Impression.VOUCHER_DETAIL_DISPLAY_PUBLIC
                                        VoucherTargetType.PRIVATE -> VoucherCreationAnalyticConstant.EventAction.Impression.VOUCHER_DETAIL_DISPLAY_PRIVATE
                                        else -> ""
                                    },
                            label = displayText,
                            userId = userId
                    )
                }
            }
        }
    }
}