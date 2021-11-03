package com.tokopedia.common.topupbills.view.typefactory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsContactBinding
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsContactNotFoundBinding
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsContactPermissionBinding
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsSavedNumberEmptyStateBinding
import com.tokopedia.common.topupbills.view.adapter.TopupBillsContactListAdapter
import com.tokopedia.common.topupbills.view.model.contact.TopupBillsContactDataView
import com.tokopedia.common.topupbills.view.model.contact.TopupBillsContactEmptyDataView
import com.tokopedia.common.topupbills.view.model.contact.TopupBillsContactNotFoundDataView
import com.tokopedia.common.topupbills.view.model.contact.TopupBillsContactPermissionDataView
import com.tokopedia.common.topupbills.view.viewholder.ContactListEmptyViewHolder
import com.tokopedia.common.topupbills.view.viewholder.ContactListNotFoundViewHolder
import com.tokopedia.common.topupbills.view.viewholder.ContactListPermissionViewHolder
import com.tokopedia.common.topupbills.view.viewholder.ContactListViewHolder

@Suppress("UNCHECKED_CAST")
class ContactListTypeFactoryImpl(
    private val contactNumberClickListener: TopupBillsContactListAdapter.ContactNumberClickListener,
    private val contactPermissionListener: TopupBillsContactListAdapter.ContactPermissionListener
): ContactListTypeFactory {

    override fun type(contactDataView: TopupBillsContactDataView): Int =
        ContactListViewHolder.LAYOUT

    override fun type(permissionDataView: TopupBillsContactPermissionDataView): Int =
        ContactListPermissionViewHolder.LAYOUT

    override fun type(emptyStateDataView: TopupBillsContactEmptyDataView): Int =
        ContactListEmptyViewHolder.LAYOUT

    override fun type(notFoundStateDataView: TopupBillsContactNotFoundDataView): Int =
        ContactListNotFoundViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<Visitable<*>> {
        return when (type) {
            ContactListViewHolder.LAYOUT -> createContactNumberViewHolder(view)
            ContactListPermissionViewHolder.LAYOUT -> createContactNumberPermissionViewHolder(view)
            ContactListNotFoundViewHolder.LAYOUT -> createContactNumberNotFoundViewHolder(view)
            ContactListEmptyViewHolder.LAYOUT -> createContactNumberEmptyViewHolder(view)
            else -> throw TypeNotSupportedException.create("Layout not supported")
        }
    }

    private fun createContactNumberViewHolder(view: View): AbstractViewHolder<Visitable<*>> {
        val binding = ItemTopupBillsContactBinding.inflate(
            LayoutInflater.from(view.context), view as ViewGroup, false
        )
        return ContactListViewHolder(
            binding,
            contactNumberClickListener
        ) as AbstractViewHolder<Visitable<*>>
    }

    private fun createContactNumberPermissionViewHolder(view: View): AbstractViewHolder<Visitable<*>> {
        val binding = ItemTopupBillsContactPermissionBinding.inflate(
            LayoutInflater.from(view.context), view as ViewGroup, false
        )
        return ContactListPermissionViewHolder(
            binding,
            contactPermissionListener
        ) as AbstractViewHolder<Visitable<*>>
    }

    private fun createContactNumberEmptyViewHolder(view: View): AbstractViewHolder<Visitable<*>> {
        val binding = ItemTopupBillsSavedNumberEmptyStateBinding.inflate(
            LayoutInflater.from(view.context), view as ViewGroup, false
        )
        return ContactListEmptyViewHolder(
            binding
        ) as AbstractViewHolder<Visitable<*>>
    }

    private fun createContactNumberNotFoundViewHolder(view: View): AbstractViewHolder<Visitable<*>> {
        val binding = ItemTopupBillsContactNotFoundBinding.inflate(
            LayoutInflater.from(view.context), view as ViewGroup, false
        )
        return ContactListNotFoundViewHolder(
            binding
        ) as AbstractViewHolder<Visitable<*>>
    }
}