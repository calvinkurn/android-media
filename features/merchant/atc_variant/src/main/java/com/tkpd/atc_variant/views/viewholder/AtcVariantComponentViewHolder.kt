package com.tkpd.atc_variant.views.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tkpd.atc_variant.R
import com.tkpd.atc_variant.data.uidata.VariantComponentDataModel
import com.tkpd.atc_variant.views.AtcVariantListener
import com.tkpd.atc_variant.views.adapter.variantitem.AtcVariantContainerAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.common.data.views.VariantItemDecorator
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
    private var txtTokoCabang: Typography? = view.findViewById(R.id.txt_variant_tokocabang)

    override fun bind(element: VariantComponentDataModel) {
        element.listOfVariantCategory?.let {
            rvVariant?.adapter = variantContainerAdapter
            if (rvVariant?.itemDecorationCount == 0) {
                rvVariant?.addItemDecoration(VariantItemDecorator(MethodChecker.getDrawable(view.context, R.drawable.bg_separator_atc_variant)))
            }
            rvVariant?.itemAnimator = null
            renderTxt(element)
            variantContainerAdapter?.setData(it)
        }
    }

    override fun bind(element: VariantComponentDataModel, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        element.listOfVariantCategory?.let {
            renderTxt(element)
            variantContainerAdapter?.variantContainerData = it
            variantContainerAdapter?.notifyItemRangeChanged(0, it.size, 1)
        }
    }

    private fun renderTxt(element: VariantComponentDataModel) = with(view) {
        txtTokoCabang?.showWithCondition(element.isTokoCabang)
        txtEmptyStock?.showWithCondition(element.isEmptyStock)
    }

}