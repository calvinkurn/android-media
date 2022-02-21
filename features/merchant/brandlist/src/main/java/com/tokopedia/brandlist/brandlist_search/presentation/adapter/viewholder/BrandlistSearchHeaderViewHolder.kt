package com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchHeaderUiModel
import com.tokopedia.brandlist.databinding.BrandlistAllBrandHeaderBinding
import com.tokopedia.utils.view.binding.viewBinding
import java.text.NumberFormat
import java.util.*

class BrandlistSearchHeaderViewHolder(view: View) : AbstractViewHolder<BrandlistSearchHeaderUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.brandlist_all_brand_header
    }
    private var binding: BrandlistAllBrandHeaderBinding? by viewBinding()

    override fun bind(element: BrandlistSearchHeaderUiModel?) {
        binding?.tvHeader?.text = element?.headerText
        binding?.tvTotalBrand?.visibility = View.GONE
        element?.totalBrand?.let{
            binding?.tvTotalBrand?.text = StringBuilder().append(
                    NumberFormat.getNumberInstance(Locale.US).format(element?.totalBrand).toString())
                    .append(" ")
                    .append(getString(R.string.brandlist_brand_label))
                    .toString().replace(",",".")
            binding?.tvTotalBrand?.visibility = View.VISIBLE
        }
    }
}