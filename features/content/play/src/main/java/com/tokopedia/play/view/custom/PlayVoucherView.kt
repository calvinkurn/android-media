package com.tokopedia.play.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.play.R
import com.tokopedia.play.databinding.ViewProductVoucherInfoBinding
import com.tokopedia.play.view.type.MerchantVoucherType
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel

/**
 * @author by astidhiyaa on 30/08/22
 */
class PlayVoucherView : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private var mListener: Listener? = null
    private val binding = ViewProductVoucherInfoBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    init {
        setOnClickListener {
            mListener?.onVoucherInfoClicked(this)
        }
    }

    fun setupView(voucher: PlayVoucherUiModel.Merchant, voucherSize: Int) {
        binding.ivPromo.setImage(newIconId = getIcon(voucher.type))
        binding.tvVoucherCount.text = getDescription(voucherSize - 1)
        binding.tvFirstVoucherTitle.text = voucher.title
    }

    private fun getIcon(type: MerchantVoucherType): Int {
        return when (type) {
            MerchantVoucherType.Shipping -> IconUnify.COURIER_FAST
            else -> IconUnify.PROMO
        }
    }

    private fun getDescription(voucherSize: Int): String =
        if (voucherSize == 0) context.getString(R.string.play_product_voucher_header_empty_desc) else context.getString(
            R.string.play_product_voucher_header_desc, voucherSize.toString()
        )

    fun setupListener(listener: Listener?) {
        mListener = listener
    }

    interface Listener {
        fun onVoucherInfoClicked(view: PlayVoucherView)
    }
}
