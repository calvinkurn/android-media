package com.tokopedia.expresscheckout.view.variant.viewholder

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantActionListener
import com.tokopedia.expresscheckout.view.variant.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.item_note_detail_product_page.view.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class NoteViewHolder(val view: View, val listener: CheckoutVariantActionListener) : AbstractViewHolder<NoteViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_note_detail_product_page
    }

    override fun bind(element: NoteViewModel?) {
        if (element != null) {
            itemView.et_note.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(element.noteCharMax))
            itemView.tv_note_char_counter.text = String.format(
                    itemView.context.getString(R.string.format_note_counter),
                    element.note.length, element.noteCharMax)

            itemView.et_note.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(note: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    element.note = note.toString()
                    itemView.tv_note_char_counter.text = String.format(
                            itemView.context.getString(R.string.format_note_counter),
                            element.note.length, element.noteCharMax)
                    listener.onChangeNote(element)
                }
            })
        }
    }

}