package com.tokopedia.manageaddress.ui.addresschoice.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.manageaddress.databinding.ItemRecipientAddressRbSelectableBinding
import com.tokopedia.manageaddress.ui.addresschoice.RecipientAddressViewHolder
import java.util.ArrayList

/**
 * @author Aghny A. Putra on 26/01/18
 */
class ShipmentAddressListAdapter(private val mActionListener: ActionListener) : RecyclerView.Adapter<RecipientAddressViewHolder>() {
    private val mAddressModelList: MutableList<RecipientAddressModel>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipientAddressViewHolder {
        val binding = ItemRecipientAddressRbSelectableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipientAddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipientAddressViewHolder, position: Int) {
        val address: RecipientAddressModel = mAddressModelList[position]
        holder.bind(address, mActionListener, position)
    }

    override fun getItemCount(): Int {
        return mAddressModelList.size
    }

    fun setAddressList(addressModelList: List<RecipientAddressModel>, selectedId: String?) {
        for (addressModel in addressModelList) {
            addressModel.setSelected(addressModel.id == selectedId)
        }
        mAddressModelList.clear()
        mAddressModelList.addAll(addressModelList)
        updateHeaderAndFooterPosition()
        notifyDataSetChanged()
    }

    fun updateAddressList(addressModelList: List<RecipientAddressModel>) {
        mAddressModelList.addAll(addressModelList)
        updateHeaderAndFooterPosition()
        notifyDataSetChanged()
    }

    fun updateSelected(position: Int) {
        for (i in mAddressModelList.indices) {
            mAddressModelList[i].setSelected(position == i)
        }
        notifyDataSetChanged()
    }

    private fun updateHeaderAndFooterPosition() {
        for (i in mAddressModelList.indices) {
            mAddressModelList[i].setHeader(i == 0)
            mAddressModelList[i].setFooter(i == mAddressModelList.size - 1)
        }
    }

    interface ActionListener {
        fun onAddressContainerClicked(model: RecipientAddressModel?, position: Int)
        fun onEditClick(model: RecipientAddressModel?)
        fun onAddAddressButtonClicked()
    }

    init {
        mAddressModelList = ArrayList<RecipientAddressModel>()
    }
}