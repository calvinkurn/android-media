package com.tokopedia.common.topupbills.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsContactBinding
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsContactEmptyBinding
import com.tokopedia.common.topupbills.view.fragment.TopupBillsContactListFragment
import com.tokopedia.common.topupbills.view.model.contact.TopupBillsContactDataView
import com.tokopedia.common.topupbills.view.model.contact.TopupBillsContactEmptyDataView
import com.tokopedia.common.topupbills.view.model.contact.TopupBillsContactPermissionDataView
import com.tokopedia.common.topupbills.view.model.favorite.TopupBillsFavNumberEmptyDataView
import com.tokopedia.common.topupbills.view.typefactory.ContactListTypeFactory
import com.tokopedia.promocheckout.common.util.EMPTY

class TopupBillsContactListAdapter(
    var visitables: List<Visitable<ContactListTypeFactory>>,
    private val typeFactory: ContactListTypeFactory
): RecyclerView.Adapter<AbstractViewHolder<Visitable<*>>>()  {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder<Visitable<*>> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int) {
        holder.bind(visitables[position])
    }

    override fun getItemViewType(position: Int): Int {
        return visitables[position].type(typeFactory)
    }

    override fun getItemCount(): Int {
        return visitables.size
    }

    fun setContacts(contacts: List<TopupBillsContactDataView>) {
        this.visitables = contacts
        notifyDataSetChanged()
    }

    fun setEmptyState() {
        this.visitables = listOf(TopupBillsContactEmptyDataView())
        notifyDataSetChanged()
    }

    fun setPermissionState() {
        this.visitables = listOf(TopupBillsContactPermissionDataView())
        notifyDataSetChanged()
    }

    interface ContactNumberClickListener {
        fun onContactNumberClick(name: String, number: String)
    }

    interface ContactPermissionListener {
        fun onSettingButtonClick()
    }
}