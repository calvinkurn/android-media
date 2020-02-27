package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.address

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.Preference
import kotlinx.android.synthetic.main.card_address_list.view.*

class AddressListItemAdapter : ListAdapter<Preference, AddressListItemAdapter.AddressListViewHolder>(DIFF_CALLBACK) {

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

    var lastCheckedPosition = -1
    private val listAddressList = listOf(Preference(), Preference(), Preference(), Preference(), Preference())
    private val listener: OnAddressSelected? = null

    interface OnAddressSelected{
        fun onSeelct(selection: Preference)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressListViewHolder {
        return AddressListViewHolder(parent.inflateLayout(R.layout.card_address_list))
    }

    override fun getItemCount(): Int = 5

    override fun onBindViewHolder(holder: AddressListViewHolder, position: Int) {
        holder.bind(listAddressList[position])
    }

    inner class AddressListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: Preference) {
            with(itemView){
                item_address_radio.isChecked = lastCheckedPosition == adapterPosition

                card_address_list.setOnClickListener {
                    lastCheckedPosition = adapterPosition
                    notifyDataSetChanged()

                    listener?.onSeelct(data)
                    item_address_radio.isChecked = !item_address_radio.isChecked

                }
            }
        }
    }

}