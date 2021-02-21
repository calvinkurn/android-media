package com.tokopedia.localizationchooseaddress.ui.bottomsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.localizationchooseaddress.R
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressList
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressListVisitable
import com.tokopedia.localizationchooseaddress.domain.model.OtherAddressModel
import com.tokopedia.localizationchooseaddress.ui.preference.ChooseAddressSharePref
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifyprinciples.Typography

class AddressListItemAdapter(private val listener: AddressListItemAdapterListener): RecyclerView.Adapter<AddressListItemAdapter.BaseViewHolder<*>>() {

    var addressList = mutableListOf<ChosenAddressListVisitable>()
    private var selectedPosition = RecyclerView.NO_POSITION

    interface AddressListItemAdapterListener {
        fun onItemClicked(address: ChosenAddressList)
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

    inner class AddressListViewHolder(itemView: View, private val listener: AddressListItemAdapterListener): AddressListItemAdapter.BaseViewHolder<ChosenAddressListVisitable>(itemView){
        val addressTitle = itemView.findViewById<Typography>(R.id.address_title)
        val receiverName = itemView.findViewById<Typography>(R.id.receiver_name)
        val addressPhone = itemView.findViewById<Typography>(R.id.address_phone)
        val addressDetail = itemView.findViewById<Typography>(R.id.address_detail)
        val addressDistrict = itemView.findViewById<Typography>(R.id.address_district)
        val cardAddress = itemView.findViewById<CardUnify>(R.id.address_item_card)
        val chooseAddressPref = ChooseAddressSharePref(itemView.context)

        override fun bind(item: ChosenAddressListVisitable, position: Int) {
            if (item is ChosenAddressList) {
                addressTitle.text = item.addressname
                receiverName.text = item.receiverName
                addressPhone.text = item.phone
                addressDetail.text = item.address1
                addressDistrict.text = item.districtName

                setSelectedData(item)
                selectedCardAddress(item)
            }
        }

        private fun setSelectedData(data: ChosenAddressList) {
            val localAddrId = chooseAddressPref.getLocalCacheData()?.address_id

            //temporary, compare || isStateChosenAddress
            if (localAddrId == data.addressId) {
                cardAddress.hasCheckIcon = true
                cardAddress.cardType = CardUnify.TYPE_BORDER_ACTIVE
            } else {
                cardAddress.hasCheckIcon = false
                cardAddress.cardType = CardUnify.TYPE_BORDER
            }
        }

        private fun selectedCardAddress(data: ChosenAddressList) {
            cardAddress.setOnClickListener {
                listener.onItemClicked(data)
            }
        }

    }

    inner class OtherAddressViewHolder(itemView: View) : AddressListItemAdapter.BaseViewHolder<ChosenAddressListVisitable>(itemView) {
        val buttonOther = itemView.findViewById<CardUnify>(R.id.other_address_card)

        override fun bind(item: ChosenAddressListVisitable, position: Int) {

            buttonOther?.setOnClickListener {
                val intent = RouteManager.getIntent(it.context, ApplinkConstInternalLogistic.MANAGE_ADDRESS)
                it.context.startActivity(intent)
            }
        }
    }



}