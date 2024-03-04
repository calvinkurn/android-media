package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.utils.extensions.addOnImpressionListener
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofStockDataModel
import com.tokopedia.product.detail.databinding.ItemHierarchycalSocialProofStockBinding
import com.tokopedia.product.detail.view.adapter.MiniSocialProofAdapter
import com.tokopedia.product.detail.view.listener.ProductDetailListener
import com.tokopedia.unifycomponents.toPx

class ProductMiniSocialProofStockViewHolder(
    private val view: View,
    private val listener: ProductDetailListener
) : AbstractViewHolder<ProductMiniSocialProofStockDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_hierarchycal_social_proof_stock
    }

    private val binding = ItemHierarchycalSocialProofStockBinding.bind(view)
    private val shimmeringBinding = binding.socialProofStockShimmering

    init {
        initRecyclerView()
        initAdapter()
    }

    private var miniSocialProofAdapter: MiniSocialProofAdapter? = null

    override fun bind(element: ProductMiniSocialProofStockDataModel) {
        if (!element.shouldRenderSocialProof) {
            setupLoading(element.shouldShowSingleViewSocialProof())
            showLoading()
        } else {
            val availableData = element.socialProofData
            view.run {
                if (availableData.isEmpty()) {
                    layoutParams.height = 0
                    return@run
                } else {
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                }
                hideLoading()
                setAdapterData(element)
                addOnImpressionListener(
                    holder = element.impressHolder,
                    holders = listener.getImpressionHolders(),
                    name = element.name,
                    useHolders = listener.isRemoteCacheableActive()
                ) {
                    listener.onImpressComponent(getComponentTrackData(element))
                }
            }
        }
    }

    private fun showLoading() = with(binding) {
        miniSocialProofRecyclerView.hide()
        shimmeringBinding.root.show()
    }

    private fun hideLoading() = with(binding) {
        miniSocialProofRecyclerView.show()
        shimmeringBinding.root.hide()
    }

    private fun setupLoading(shouldShowSingleSocialProof: Boolean) = with(shimmeringBinding) {
        if (shouldShowSingleSocialProof) {
            root.setPadding(8.toPx(), 0, 16.toPx(), 14.toPx())
        } else {
            root.setPadding(8.toPx(), 8.toPx(), 16.toPx(), 18.toPx())
        }
    }

    private fun getComponentTrackData(element: ProductMiniSocialProofStockDataModel) = ComponentTrackDataModel(element.type, element.name, adapterPosition + 1)

    private fun initRecyclerView() {
        binding.miniSocialProofRecyclerView.setHasFixedSize(true)
    }

    private fun initAdapter() {
        miniSocialProofAdapter = MiniSocialProofAdapter(listener)
        binding.miniSocialProofRecyclerView.apply {
            adapter = miniSocialProofAdapter
            layoutManager = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)
        }
    }

    private fun setAdapterData(element: ProductMiniSocialProofStockDataModel) {
        miniSocialProofAdapter?.setData(element.socialProofData.toMutableList(), getComponentTrackData(element))
    }
}
