package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofDataModel
import com.tokopedia.product.detail.databinding.ItemHierarchycalSocialProofBinding
import com.tokopedia.product.detail.view.adapter.MiniSocialProofAdapter
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifycomponents.toPx

/**
 * Created by Yehezkiel on 18/05/20
 */
class ProductMiniSocialProofViewHolder(private val view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductMiniSocialProofDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_hierarchycal_social_proof
    }

    private val binding = ItemHierarchycalSocialProofBinding.bind(view)
    private val shimmeringBinding = binding.socialProofShimmering

    init {
        initRecyclerView()
        initAdapter()
    }

    private var miniSocialProofAdapter: MiniSocialProofAdapter? = null

    override fun bind(element: ProductMiniSocialProofDataModel) {
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
                addOnImpressionListener(element.impressHolder) {
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
        if (shouldShowSingleSocialProof)
            root.setPadding(8.toPx(), 0, 16.toPx(), 16.toPx())
        else
            root.setPadding(8.toPx(), 8.toPx(), 16.toPx(), 20.toPx())
    }

    private fun getComponentTrackData(element: ProductMiniSocialProofDataModel) = ComponentTrackDataModel(element.type, element.name, adapterPosition + 1)

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

    private fun setAdapterData(element: ProductMiniSocialProofDataModel) {
        miniSocialProofAdapter?.setData(element.socialProofData.toMutableList(), getComponentTrackData(element))
    }
}