package com.tokopedia.editshipping.ui.shippingeditor.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.databinding.ItemShipperProductNameBinding
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperProductModel
import com.tokopedia.kotlin.extensions.view.gone

class ShipperProductItemAdapter(private var listener: ShipperProductItemListener) :
    RecyclerView.Adapter<ShipperProductItemAdapter.ShipperProductOnDemandViewHolder>() {

    interface ShipperProductItemListener

    interface ShipperProductUncheckedListener {
        fun uncheckedProduct()
    }

    fun setupUncheckedListener(listener: ShipperProductUncheckedListener) {
        this.shipperProductUncheckedListener = listener
    }

    private var shipperProductUncheckedListener: ShipperProductUncheckedListener? = null

    private var shipperProduct = mutableListOf<ShipperProductModel>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShipperProductOnDemandViewHolder {
        return ShipperProductOnDemandViewHolder(
            ItemShipperProductNameBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return shipperProduct.size
    }

    override fun onBindViewHolder(holder: ShipperProductOnDemandViewHolder, position: Int) {
        holder.bindData(shipperProduct[position])
    }

    override fun onViewRecycled(holder: ShipperProductOnDemandViewHolder) {
        super.onViewRecycled(holder)
        holder.setCheckBoxListener(null)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(data: List<ShipperProductModel>) {
        shipperProduct.clear()
        shipperProduct.addAll(data)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateChecked(checked: Boolean) {
        shipperProduct.filter { it.isAvailable }.forEach {
            it.isActive = checked
        }
        notifyDataSetChanged()
    }

    inner class ShipperProductOnDemandViewHolder(private val binding: ItemShipperProductNameBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: ShipperProductModel) {
            setItemData(data)
        }

        fun setCheckBoxListener(listener: CompoundButton.OnCheckedChangeListener?) {
            binding.shipperProductCb.setOnCheckedChangeListener(listener)
        }

        private fun setItemData(data: ShipperProductModel) {
            setShipperProductName(data)
            setShipperProductEnableState(data)
            setCheckBoxCheckedState(data)
            setDivider(data)
            setCheckBoxListener(data)
        }

        private fun setCheckBoxListener(data: ShipperProductModel) {
            binding.shipperProductCb.setOnCheckedChangeListener { _, isChecked ->
                data.isActive = isChecked
                activeProductChecker(isChecked)
            }
        }

        private fun setShipperProductName(data: ShipperProductModel) {
            binding.shipperProductName.text = data.shipperProductName
        }

        private fun setDivider(data: ShipperProductModel) {
            val lastItem = shipperProduct.last()
            if (data == lastItem) {
                binding.dividerShipment.gone()
            }
        }

        private fun setCheckBoxCheckedState(data: ShipperProductModel) {
            if (data.isAvailable) {
                binding.shipperProductCb.isEnabled = true
                binding.shipperProductCb.isChecked = data.isActive
            } else {
                binding.shipperProductCb.isEnabled = false
            }
        }

        private fun setShipperProductEnableState(data: ShipperProductModel) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (data.isAvailable) {
                    binding.root.foreground =
                        MethodChecker.getDrawable(itemView.context, R.drawable.fg_enabled_item_log)
                } else {
                    binding.root.foreground =
                        MethodChecker.getDrawable(itemView.context, R.drawable.fg_disabled_item_log)
                }
            }
        }

        private fun activeProductChecker(isChecked: Boolean) {
            if (!isChecked) {
                val hasActiveItem = shipperProduct.find { it.isActive }
                if (hasActiveItem == null) {
                    shipperProductUncheckedListener?.uncheckedProduct()
                }
            }
        }
    }
}
