package com.tkpd.atcvariant.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tkpd.atcvariant.R
import com.tkpd.atcvariant.data.uidata.VariantComponentDataModel
import com.tkpd.atcvariant.view.adapter.variantitem.AtcVariantContainerAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Yehezkiel on 07/05/21
 */
class AtcVariantComponentViewHolder(private val view: View, listener: AtcVariantListener) : AbstractViewHolder<VariantComponentDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.atc_variant_viewholder
    }

    private var variantContainerAdapter: AtcVariantContainerAdapter? = AtcVariantContainerAdapter(listener)
    private var rvVariant: RecyclerView? = view.findViewById(R.id.rv_variant_viewholder)
    private var txtEmptyStock: Typography? = view.findViewById(R.id.txt_variant_empty_stock)

    override fun bind(element: VariantComponentDataModel) {
        element.listOfVariantCategory?.let {
            rvVariant?.adapter = variantContainerAdapter
            rvVariant?.itemAnimator = null
            renderStockWording(element)
            variantContainerAdapter?.setData(it)
        }
    }

    override fun bind(element: VariantComponentDataModel, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        element.listOfVariantCategory?.let {
            renderStockWording(element)
            variantContainerAdapter?.variantContainerData = it
            variantContainerAdapter?.notifyItemRangeChanged(0, it.size, 1)
        }
    }

    private fun renderStockWording(element: VariantComponentDataModel) = with(view) {
        txtEmptyStock?.text = HtmlLinkHelper(context, element.emptyOrInnactiveCopy).spannedString
        txtEmptyStock?.showWithCondition(element.emptyOrInnactiveCopy.isNotEmpty())
    }

    private fun getHexColor(resColor: Int): String {
        val hexColor = Integer.toHexString(itemView.context.getResColor(resColor))
        val startIndex = 2
        return if (hexColor.length > startIndex) {
            "#" + hexColor.substring(startIndex)
        } else {
            "#$hexColor"
        }
    }

}