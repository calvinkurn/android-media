package com.tokopedia.catalog.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.catalog.databinding.ItemProductSelectionComparisonBinding
import com.tokopedia.catalog.ui.fragment.CatalogSwitchingComparisonFragment.Companion.LIMIT_SELECT_PRODUCT
import com.tokopedia.catalog.ui.model.CatalogComparisonProductsUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class CatalogSelectionAdapter(
    var itemList: MutableList<CatalogComparisonProductsUiModel.CatalogComparisonUIModel> = mutableListOf(),
    val listener: CatalogSelectionListener
) :
    RecyclerView.Adapter<CatalogSelectionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductSelectionComparisonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemUiModel = itemList.getOrNull(position)
        if (itemUiModel != null){
            holder.bindToView(itemUiModel, position)
        }else{
            holder.showEmptySelection()
        }
    }

    override fun getItemCount(): Int {
        if (itemList.size == Int.ZERO){
            return Int.ZERO
        }
        return LIMIT_SELECT_PRODUCT
    }

    inner class ViewHolder(itemView: ItemProductSelectionComparisonBinding) :
        RecyclerView.ViewHolder(itemView.root) {

        private val CURRENT_CATALOG = Int.ZERO

        private val ivImage = itemView.ivProduct
        private val tfProductName = itemView.tfProductName
        private val tfProductPrice = itemView.tfProductPrice
        private val iconProductAction = itemView.iconProductAction
        private val cardProductAction = itemView.cardProductAction
        private val cgContent = itemView.cgContent
        private val emptyContent = itemView.emptyCatalog

        fun bindToView(itemUiModel: CatalogComparisonProductsUiModel.CatalogComparisonUIModel, position: Int) {
            ivImage.loadImage(itemUiModel.catalogImage)
            tfProductName.text = itemUiModel.name
            tfProductPrice.text = itemUiModel.price
            if (position == CURRENT_CATALOG) {
                val colorGray =
                    MethodChecker.getColor(itemView.context, unifyprinciplesR.color.Unify_NN500)
                iconProductAction.setImage(IconUnify.PUSH_PIN_FILLED, colorGray)
            } else {
                val colorRed =
                    MethodChecker.getColor(itemView.context, unifyprinciplesR.color.Unify_RN500)
                iconProductAction.setImage(IconUnify.REMOVE_CIRCLE, colorRed)

                cardProductAction.setOnClickListener {
                    listener.onActionListener(itemUiModel.id.orEmpty())
                }
            }
            emptyContent.gone()
            cgContent.visible()
        }

        fun showEmptySelection(){
            emptyContent.visible()
            cgContent.gone()
        }

    }
}

interface CatalogSelectionListener {
    fun onActionListener(id:String)
}
