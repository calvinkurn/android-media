package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.databinding.ViewmodelOfficialBenefitBinding
import com.tokopedia.officialstore.official.data.model.Benefit
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialBenefitDataModel
import com.tokopedia.officialstore.official.presentation.widget.BenefitAdapter
import com.tokopedia.utils.view.binding.viewBinding

class OfficialBenefitViewHolder(view: View) : AbstractViewHolder<OfficialBenefitDataModel>(view) {

    private var adapter: BenefitAdapter? = null
    private var binding: ViewmodelOfficialBenefitBinding? by viewBinding()

    override fun bind(element: OfficialBenefitDataModel) {
        if(adapter == null){
            adapter = BenefitAdapter(itemView.context)
            binding?.recyclerviewOfficialBenefit?.adapter = adapter
        }
        element.benefit.let {
            adapter?.benefitList = it
            adapter?.notifyDataSetChanged()

            adapter?.onItemClickListener = object: BenefitAdapter.OnItemClickListener {
                override fun onItemClick(position: Int, item: Benefit) {
                    RouteManager.route(itemView.context, "${ApplinkConst.WEBVIEW}?url=${item.redirectUrl}")
                }
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.viewmodel_official_benefit
    }

}