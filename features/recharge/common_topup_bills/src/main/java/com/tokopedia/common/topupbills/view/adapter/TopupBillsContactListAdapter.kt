package com.tokopedia.common.topupbills.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsContactBinding
import com.tokopedia.common.topupbills.view.fragment.TopupBillsContactListFragment

class TopupBillsContactListAdapter(
    private var contacts: List<TopupBillsContactListFragment.Contact>,
    private var listener: OnContactNumberClickListener
): RecyclerView.Adapter<TopupBillsContactListAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutInflater.from(parent.context)
        return ViewHolder(ItemTopupBillsContactBinding.inflate(binding, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    override fun getItemCount(): Int = contacts.size

    fun setContacts(contacts: List<TopupBillsContactListFragment.Contact>) {
        this.contacts = contacts
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val binding: ItemTopupBillsContactBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: TopupBillsContactListFragment.Contact) {
            binding.run {
                commonTopupBillsContactName.text = contact.name
                commonTopupBillsContactNumber.text = contact.phoneNumber
                commonTopupBillsInitial.text = contact.name[0].toString()
                commonTopupBillsContainerContactNumber.setOnClickListener {
                    listener.onContactNumberClick(contact.name, contact.phoneNumber)
                }
            }
        }
    }

    interface OnContactNumberClickListener {
        fun onContactNumberClick(name: String, number: String)
    }
}