package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.play.R
import com.tokopedia.play.view.type.MerchantVoucherType
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifyprinciples.Typography


/**
 * Created by mzennis on 22/02/21.
 */
class PinnedVoucherViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int,
        private val listener: Listener
) : ViewComponent(container, idRes) {

    private val ivVoucherImage = findViewById<AppCompatImageView>(R.id.iv_pinned_voucher_image)
    private val tvVoucherTitle = findViewById<Typography>(R.id.tv_pinned_voucher_title)
    private val tvVoucherDescription = findViewById<Typography>(R.id.tv_pinned_voucher_description)

    init {
        container.setOnClickListener {
            listener.onVoucherClicked(this)
        }
    }

    fun setVoucher(voucher: MerchantVoucherUiModel) {
        tvVoucherTitle.text = voucher.title
        tvVoucherDescription.text = voucher.description

        ivVoucherImage.setImageResource(
                if (voucher.type == MerchantVoucherType.Shipping) R.drawable.ic_play_shipping_voucher
                else R.drawable.ic_play_discount_voucher
        )
    }

    interface Listener {

        fun onVoucherClicked(view: PinnedVoucherViewComponent)
    }
}