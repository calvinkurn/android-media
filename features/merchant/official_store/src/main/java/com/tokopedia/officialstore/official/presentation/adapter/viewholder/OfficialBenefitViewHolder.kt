package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.data.model.Benefit
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.OfficialBenefitViewModel
import com.tokopedia.officialstore.official.presentation.widget.BenefitAdapter
import com.tokopedia.officialstore.official.presentation.widget.GridSpacingItemDecoration

class OfficialBenefitViewHolder(view: View?): AbstractViewHolder<OfficialBenefitViewModel>(view){

    private var recyclerView: RecyclerView? = null
    private var container: View? = null

    private var adapter: BenefitAdapter? = null

    init {
        recyclerView = view?.findViewById(R.id.recyclerview_official_benefit)
        container = view?.findViewById(R.id.container_benefit)

        view?.context?.let {
            adapter = BenefitAdapter(it)
            recyclerView?.layoutManager = GridLayoutManager(it, 3)
            recyclerView?.addItemDecoration(GridSpacingItemDecoration(3, 0))
            recyclerView?.adapter = adapter
        }
    }

    override fun bind(element: OfficialBenefitViewModel?) {
        element?.benefit?.let {
            adapter?.benefitList = it
            adapter?.notifyDataSetChanged()

            adapter?.onItemClickListener = object: BenefitAdapter.OnItemClickListener {
                override fun onItemClick(context: Context, p0: Int, item: Benefit) {
                    RouteManager.route(context,
                            "${ApplinkConst.WEBVIEW}?url=${item.redirectUrl}")

                }
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.viewmodel_official_benefit
    }

}