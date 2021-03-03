package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofItemDataModel
import com.tokopedia.product.detail.view.adapter.MiniSocialProofAdapter
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.item_hierarchycal_social_proof.view.*
import kotlinx.android.synthetic.main.shimmering_social_proof.view.*

/**
 * Created by Yehezkiel on 18/05/20
 */
class ProductMiniSocialProofViewHolder(private val view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductMiniSocialProofDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_hierarchycal_social_proof
    }

    init {
        initRecyclerView()
        initAdapter()
    }

    private var miniSocialProofAdapter: MiniSocialProofAdapter? = null
    private var miniSocialProofRecyclerView: RecyclerView? = null

    override fun bind(element: ProductMiniSocialProofDataModel) {
        if (!element.shouldRenderSocialProof) {
            setupLoading(element.shouldShowSingleViewSocialProof())
            showLoading()
        } else {
            val availableData = element.getSocialProofData()
            view.run {
                if (availableData.isEmpty()) {
                    layoutParams.height = 0
                    return@run
                } else {
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                }
                hideLoading()
                setAdapterData(element)
            }
        }
    }

    private fun showLoading() = with(view) {
        miniSocialProofRecyclerView?.hide()
        pdp_shimmering_social_proof.show()
    }

    private fun hideLoading() = with(view) {
        miniSocialProofRecyclerView?.show()
        pdp_shimmering_social_proof.hide()
    }

    private fun setupLoading(shouldShowSingleSocialProof: Boolean) = with(view) {
        if (shouldShowSingleSocialProof)
            pdp_shimmering_social_proof.setPadding(16.toPx(), 0, 16.toPx(), 16.toPx())
        else
            pdp_shimmering_social_proof.setPadding(16.toPx(), 8.toPx(), 16.toPx(), 20.toPx())
    }

    private fun getComponentTrackData(element: ProductMiniSocialProofDataModel) = ComponentTrackDataModel(element.type, element.name, adapterPosition + 1)

    private fun initRecyclerView() {
        miniSocialProofRecyclerView = view.findViewById(R.id.mini_social_proof_recycler_view)
        miniSocialProofRecyclerView?.setHasFixedSize(true)
    }

    private fun initAdapter() {
        miniSocialProofAdapter = MiniSocialProofAdapter(listener)
        miniSocialProofRecyclerView?.apply {
            adapter = miniSocialProofAdapter
            layoutManager = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)
        }
    }

    private fun setAdapterData(element: ProductMiniSocialProofDataModel) {
        miniSocialProofAdapter?.setData(element.getSocialProofData().toMutableList(), getComponentTrackData(element))
    }
}