package com.tokopedia.editshipping.ui.customproductlogistic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.editshipping.databinding.ItemShippingEditorCardBinding
import com.tokopedia.logisticCommon.data.model.CPLProductModel
import com.tokopedia.logisticCommon.data.model.ShipperCPLModel

class CPLItemAdapter(private val listener: CPLItemAdapterListener) :
    RecyclerView.Adapter<CPLItemViewHolder>() {

    var cplItem = mutableListOf<ShipperCPLModel>()

    interface CPLItemAdapterListener {
        fun onCheckboxItemClicked()
    }

    var shipperServices = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CPLItemViewHolder {
        val binding = ItemShippingEditorCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CPLItemViewHolder.getViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: CPLItemViewHolder, position: Int) {
        holder.bindData(cplItem[position])
    }

    override fun getItemCount(): Int {
        return cplItem.size
    }

    fun addData(data: List<ShipperCPLModel>) {
        cplItem.clear()
        cplItem.addAll(data)
        notifyDataSetChanged()
    }

    fun setProductIdsActivated(data: CPLProductModel) {
        cplItem.forEach { courier ->
            data.shipperServices.forEach {
                val cplItemModel = courier.shipperProduct.find { data ->
                    data.shipperProductId == it
                }
                if (cplItemModel?.shipperProductId == it) {
                    cplItemModel.isActive = true
                }
            }
        }
        notifyDataSetChanged()
    }

    fun setAllProductIdsActivated() {
        cplItem.forEach { courier ->
            courier.isActive = true
            courier.shipperProduct.forEach { data ->
                data.isActive = true
            }
        }
    }

    fun getActivateSpIds(): List<Int> {
        val activatedListIds = mutableListOf<Int>()
        cplItem.forEach { courier ->
            courier.shipperProduct.forEach { product ->
                if (product.isActive) {
                    activatedListIds.add(product.shipperProductId.toInt())
                }
            }
        }
        return activatedListIds
    }

    fun checkActivatedSpIds(): List<Int> {
        val activatedListIds = mutableListOf<Int>()
        cplItem.forEach { courier ->
            courier.shipperProduct.forEach { product ->
                if (product.isActive) {
                    activatedListIds.add(product.shipperProductId.toInt())
                }
            }
        }
        return activatedListIds
    }

    fun getShownShippers(): List<Int> {
        val listShipperShown = mutableListOf<Int>()
        cplItem.forEach { courier ->
            courier.shipperProduct.forEach { product ->
                if (!product.uiHidden) {
                    listShipperShown.add(product.shipperProductId.toInt())
                }
            }
        }
        return listShipperShown
    }

}