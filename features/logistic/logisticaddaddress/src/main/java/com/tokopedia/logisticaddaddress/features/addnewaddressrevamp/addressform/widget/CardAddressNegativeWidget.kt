package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.databinding.CardAddressNewLocNegativeBinding
import com.tokopedia.unifycomponents.HtmlLinkHelper

class CardAddressNegativeWidget : ConstraintLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var binding: CardAddressNewLocNegativeBinding? = null

    init {
        binding = CardAddressNewLocNegativeBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun updateView(
        isEdit: Boolean
    ) {
        binding?.apply {
            binding?.icLocation?.setImage(IconUnify.LOCATION)
            binding?.addressDistrict?.text = if (isEdit) {
                context.getString(R.string.tv_pinpoint_defined_edit)
            } else {
                context?.let {
                    HtmlLinkHelper(
                        it,
                        context.getString(R.string.tv_pinpoint_defined)
                    ).spannedString
                }
            }
            binding?.btnChangeNegative?.text =
                context.getString(R.string.change_pinpoint_positive_text)
        }
    }

    fun setupNegativePinpointCard(
        hasPinPoint: Boolean?,
        isEdit: Boolean
    ) {
        binding?.run {
            if (hasPinPoint != true) {
                icLocation.setImage(IconUnify.LOCATION_OFF)
                addressDistrict.text =
                    if (isEdit) {
                        context.getString(R.string.tv_pinpoint_not_defined_edit)
                    } else {
                        context?.let {
                            HtmlLinkHelper(
                                it,
                                it.getString(R.string.tv_pinpoint_not_defined)
                            ).spannedString
                        }
                    }
            } else {
                icLocation.setImage(IconUnify.LOCATION)
                addressDistrict.text =
                    if (isEdit) {
                        context?.getString(R.string.tv_pinpoint_defined_edit)
                    } else {
                        context?.let {
                            HtmlLinkHelper(
                                it,
                                it.getString(R.string.tv_pinpoint_defined)
                            ).spannedString
                        }
                    }
                btnChangeNegative.text =
                    context?.getString(R.string.change_pinpoint_positive_text)
            }
        }
    }

    fun setNotYetPinPoint() {
        binding?.apply {
            icLocation.setImage(IconUnify.LOCATION_OFF)
            addressDistrict.text = context?.let {
                HtmlLinkHelper(
                    it,
                    it.getString(R.string.tv_pinpoint_not_defined)
                ).spannedString
            }
        }
    }

    fun showBtnChangeNegative(
        onClickCardAddressNegativeListener: () -> Unit
    ) {
        binding?.apply {
            cardAddressNegative.run {
                root.setOnClickListener {
                    onClickCardAddressNegativeListener.invoke()
                }
                btnChangeNegative.visible()
                btnArrow.gone()
            }
        }
    }
}
