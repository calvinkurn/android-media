package com.tokopedia.localizationchooseaddress.ui.bottomsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.R
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressList
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressListVisitable
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.model.OtherAddressModel
import com.tokopedia.localizationchooseaddress.ui.preference.ChooseAddressSharePref
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

class AddressListItemAdapter(private val listener: AddressListItemAdapterListener): RecyclerView.Adapter<AddressListItemAdapter.BaseViewHolder<*>>() {

    var addressList = mutableListOf<ChosenAddressListVisitable>()

    interface AddressListItemAdapterListener {
        fun onItemClicked(address: ChosenAddressList)
        fun onOtherAddressClicked()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.item_choose_address -> AddressListViewHolder(view, listener)
            else -> OtherAddressViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val data = addressList[position]
        when (holder) {
            is AddressListViewHolder -> {
                holder.itemView.isClickable = true
                holder.bind(data, holder.adapterPosition)
            }

            is OtherAddressViewHolder -> {
                holder.bind(data, holder.adapterPosition)
            }
        }

    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    override fun getItemViewType(position: Int): Int = when (addressList[position]) {
        is ChosenAddressList -> R.layout.item_choose_address
        is OtherAddressModel -> R.layout.item_other_address
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T, position: Int)
    }

    fun updateData(data: List<ChosenAddressList>) {
        addressList.clear()
        addressList.addAll(data)
        addressList.add(data.size, OtherAddressModel())
        notifyDataSetChanged()
    }

    internal fun containsChosenAddress(): Boolean {
        return addressList.firstOrNull { it is ChosenAddressList } != null
    }

    inner class AddressListViewHolder(itemView: View, private val listener: AddressListItemAdapterListener): AddressListItemAdapter.BaseViewHolder<ChosenAddressListVisitable>(itemView){
        val addressTitle = itemView.findViewById<Typography>(R.id.address_title)
        val receiverName = itemView.findViewById<Typography>(R.id.receiver_name)
        val addressPhone = itemView.findViewById<Typography>(R.id.address_phone)
        val addressDetail = itemView.findViewById<Typography>(R.id.address_detail)
        val addressDistrict = itemView.findViewById<Typography>(R.id.address_district)
        val cardAddress = itemView.findViewById<CardUnify>(R.id.address_item_card)
        var labelUtama = itemView.findViewById<Label>(R.id.lbl_main_address)
        val chooseAddressPref = ChooseAddressSharePref(itemView.context)

        override fun bind(item: ChosenAddressListVisitable, position: Int) {
            if (item is ChosenAddressList) {
                addressTitle.text = item.addressname
                receiverName.text = item.receiverName
                addressPhone.text = item.phone
                addressDetail.text = item.address1
                addressDistrict.text = item.districtName

                if (item.status == 2) labelUtama.visible() else labelUtama.gone()

                setSelectedData(item)
            }
        }

        private fun setSelectedData(data: ChosenAddressList) {
            val localAddr = chooseAddressPref.getLocalCacheData()

            if (dataIsSame(localAddr, data)) {
                cardAddress.hasCheckIcon = true
                cardAddress.cardType = CardUnify.TYPE_SHADOW_ACTIVE
                cardAddress.setPadding(0, 0, 10.toPx(), 0)
                cardAddress.setOnClickListener(null)
            } else {
                cardAddress.hasCheckIcon = false
                cardAddress.cardType = CardUnify.TYPE_SHADOW
                cardAddress.setOnClickListener { listener.onItemClicked(data) }
            }
        }

    }

    private fun dataIsSame(localCacheModel: LocalCacheModel?, data: ChosenAddressList): Boolean {
        var validate = true
        if (localCacheModel?.address_id != data.addressId) validate = false
        if (localCacheModel?.city_id != data.cityId) validate = false
        if (localCacheModel?.district_id != data.districtId) validate = false
        if (localCacheModel?.postal_code != data.postalCode) validate = false
        return validate
    }

    inner class OtherAddressViewHolder(itemView: View) : AddressListItemAdapter.BaseViewHolder<ChosenAddressListVisitable>(itemView) {
        val buttonOther = itemView.findViewById<CardUnify>(R.id.other_address_card)

        override fun bind(item: ChosenAddressListVisitable, position: Int) {

            buttonOther?.setOnClickListener {
                listener.onOtherAddressClicked()
            }
        }
    }



}