package com.tokopedia.entertainment.pdp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.databinding.ItemCheckoutEventAdditionalItemFilledBinding
import com.tokopedia.entertainment.pdp.common.util.EventConst.ELEMENT_LIST
import com.tokopedia.entertainment.pdp.data.Form

class EventCheckoutAdditonalFilledAdapter : RecyclerView.Adapter<EventCheckoutAdditonalFilledAdapter.EventCheckoutAdditionalFilledViewHolder>() {

    private var listForm = emptyList<Form>()

    inner class EventCheckoutAdditionalFilledViewHolder(val binding: ItemCheckoutEventAdditionalItemFilledBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(form: Form) {
            with(binding) {
                eventItemTitle.text = root.context.resources.getString(R.string.ent_checkout_data_tambahan_item_title, form.title)
                eventItemValue.text = root.context.resources.getString(R.string.ent_checkout_data_tambahan_item_value, if (!form.elementType.equals(ELEMENT_LIST)) form.value
                else form.valueList)
            }
        }
    }

    override fun onBindViewHolder(holder: EventCheckoutAdditionalFilledViewHolder, position: Int) {
        holder.bind(listForm[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventCheckoutAdditionalFilledViewHolder {
        val binding = ItemCheckoutEventAdditionalItemFilledBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EventCheckoutAdditionalFilledViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listForm.size
    }

    fun setList(list: List<Form>) {
        listForm = list
        notifyDataSetChanged()
    }
}
