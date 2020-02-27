package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.shipping

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.Preference
import kotlinx.android.synthetic.main.item_shipping_duration.view.*

//class ShippingDurationItemAdapter : RecyclerView.Adapter<ShippingDurationItemAdapter.ShippingDurationViewHolder>(){

class ShippingDurationItemAdapter : ListAdapter<Preference, ShippingDurationItemAdapter.ShippingDurationViewHolder>(DIFF_CALLBACK){

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Preference>(){
            override fun areItemsTheSame(oldItem: Preference, newItem: Preference): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Preference, newItem: Preference): Boolean {
                return oldItem == newItem
            }

        }
    }

    val context: Context? = null
    var lasCheckedPosition = -1
//    private var inflater: LayoutInflater = LayoutInflater.from(context)
//    private val listShippingDuration = emptyList<Preference>()
    private val listShippingDuration = listOf(Preference(), Preference(), Preference(), Preference(), Preference())
    private val listener: OnShippingMenuSelected? = null

    interface OnShippingMenuSelected {
        fun onSelect(selection: Preference)
    }

    inner class ShippingDurationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: Preference) {
            with(itemView){
                item_shipping_radio.isChecked = lasCheckedPosition == adapterPosition

                item_shipping_list.setOnClickListener {
                    lasCheckedPosition = adapterPosition
                    notifyDataSetChanged()

                    listener?.onSelect(data)
                    item_shipping_radio.isChecked = !item_shipping_radio.isChecked
                }


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShippingDurationViewHolder {
        return ShippingDurationViewHolder(parent.inflateLayout(R.layout.item_shipping_duration))
    }

    override fun getItemCount(): Int = 5

    override fun onBindViewHolder(holder: ShippingDurationViewHolder, position: Int) {
        holder.bind(listShippingDuration[position])
    }

}