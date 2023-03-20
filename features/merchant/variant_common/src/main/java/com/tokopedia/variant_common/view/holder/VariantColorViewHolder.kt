package com.tokopedia.variant_common.view.holder

import android.view.View
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.common.VariantConstant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.variant_common.R
import com.tokopedia.variant_common.databinding.ItemVariantColorViewHolderBinding

/**
 * Created by Yehezkiel on 08/03/20
 */
class VariantColorViewHolder(val view: View,
                             val listener: AtcVariantListener) : BaseVariantViewHolder<VariantOptionWithAttribute>(view) {

    companion object {
        val LAYOUT = R.layout.item_variant_color_view_holder
    }

    private val binding = ItemVariantColorViewHolderBinding.bind(view)

    override fun bind(element: VariantOptionWithAttribute, payload: Int) {
        setState(element)
    }

    override fun bind(element: VariantOptionWithAttribute) = with(binding) {
        variantColorItem.setInnerColor(element.variantHex)
        setState(element)
    }

    private fun setState(element: VariantOptionWithAttribute) = with(binding) {
        when (element.currentState) {
            VariantConstant.STATE_EMPTY -> {
                variantColorItem.setColorNotAvailable()
                txtVariantColorEmpty.show()
                view.isEnabled = false
            }
            VariantConstant.STATE_SELECTED -> {
                variantColorItem.setColorSelected()
                txtVariantColorEmpty.gone()
                view.isEnabled = false
            }
            VariantConstant.STATE_UNSELECTED -> {
                variantColorItem.setColorAvailable()
                txtVariantColorEmpty.gone()
                view.isEnabled = true
            }
        }

        view.setOnClickListener {
            listener.onVariantClicked(element)
        }
    }
}
