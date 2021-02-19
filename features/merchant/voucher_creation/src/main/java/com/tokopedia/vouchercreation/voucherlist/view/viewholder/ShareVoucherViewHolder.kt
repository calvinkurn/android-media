package com.tokopedia.vouchercreation.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.consts.VoucherCreationConst.ELIGIBLE_STATUS
import com.tokopedia.vouchercreation.voucherlist.model.ui.ShareVoucherUiModel
import kotlinx.android.synthetic.main.item_mvc_share_voucher.view.*

/**
 * Created By @ilhamsuaib on 28/04/20
 */

class ShareVoucherViewHolder(
        itemView: View?,
        private val onClick: (ShareVoucherUiModel) -> Unit
) : AbstractViewHolder<ShareVoucherUiModel>(itemView) {

    private var quotaView: Typography? = null
    private var freeLabelView: AppCompatImageView? = null

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_share_voucher
    }

    init {
        quotaView = itemView?.findViewById(R.id.tv_mvc_quota)
        freeLabelView = itemView?.findViewById(R.id.iv_mvc_free)
    }

    override fun bind(element: ShareVoucherUiModel) {
        with(itemView) {
            tvMvcSocmed.text = element.socmedName
            icMvcSocmed.loadImageDrawable(element.icon)

            if (element.status == ELIGIBLE_STATUS && element.quota > 0) {
                // replace %s wit the number of quota
                val quotaText = String.format(
                        itemView.context.getString(R.string.mvc_broadcast_chat_quota).toBlankOrString(),
                        element.quota.toString()
                )
                quotaView?.text = quotaText
                freeLabelView?.show()
                quotaView?.show()
            }

            setOnClickListener {
                onClick(element)
            }
        }
    }
}