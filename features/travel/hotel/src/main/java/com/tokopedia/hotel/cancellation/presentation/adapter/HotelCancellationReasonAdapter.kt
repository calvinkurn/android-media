package com.tokopedia.hotel.cancellation.presentation.adapter

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.cancellation.data.HotelCancellationModel
import com.tokopedia.hotel.databinding.LayoutHotelCancellationReasonItemBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * @author by jessica on 05/05/20
 */

class HotelCancellationReasonAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var items: List<HotelCancellationModel.Reason> = listOf()
    var selectedId = ""
    var freeText = ""
    var onClickItemListener: OnClickItemListener? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_HEADER -> HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_hotel_cancellation_reason_title,parent,false))
            else -> ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_hotel_cancellation_reason_item, parent, false))
        }
    }

    override fun getItemCount(): Int = items.size + 1

    override fun getItemViewType(position: Int): Int {
        return if(position == 0) TYPE_HEADER else TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position > 0) {
            (holder as ViewHolder).bind(items[position - 1],position == items.size,
                    onClickItemListener, selectedId)
        }
    }

    fun updateItems(reasons: List<HotelCancellationModel.Reason>) {
        items = reasons
        notifyDataSetChanged()
    }

    fun onClickItem(selectedId: String) {
        this.selectedId = selectedId
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return if (position == 0) TYPE_HEADER.toLong() else (position + 1).toLong()
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val binding = LayoutHotelCancellationReasonItemBinding.bind(view)

        fun bind(reason: HotelCancellationModel.Reason, isLastItem: Boolean,
                 listener: OnClickItemListener?, selectedId: String) {
            with(binding) {
                hotelCancellationReasonHotelDescription.text = reason.title

                hotelCancellationReasonRadioButton.isChecked = reason.id == selectedId
                if (!hotelCancellationReasonRadioButton.isChecked) hotelCancellationReasonFreeTextTf.hide()

                binding.root.setOnClickListener { setUpFreeText(reason, listener) }
                hotelCancellationReasonRadioButton.setOnClickListener { setUpFreeText(reason, listener) }

                if (reason.freeText) setUpFreeTextTextField(listener, reason.id)
                if (isLastItem) hotelCancellationSeperator.hide()
            }
        }

        private fun setUpFreeText(reason: HotelCancellationModel.Reason, listener: OnClickItemListener?) {
            with(binding) {
                if (reason.freeText) hotelCancellationReasonFreeTextTf.show()
                val isValid = !reason.freeText || hotelCancellationReasonFreeTextTf.getEditableValue().length >= 10
                listener?.onClick(reason.id, isValid)
            }
        }

        private fun setUpFreeTextTextField(listener: OnClickItemListener?, id: String) {
            with(binding) {
                hotelCancellationReasonFreeTextTf.textFieldInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                hotelCancellationReasonFreeTextTf.textFiedlLabelText.setTextColor(ContextCompat.getColor(root.context, unifyprinciplesR.color.Unify_NN950_68))
                hotelCancellationReasonFreeTextTf.textFieldInput.addTextChangedListener(object : TextWatcher{
                    override fun afterTextChanged(s: Editable?) {
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(TYPE_EDITTEXT_DELAY_MS.toLong())
                            if (s.toString().trim().length >= MIN_EDITTEXT_CHAR) {
                                hotelCancellationReasonFreeTextTf.setError(false)
                                hotelCancellationReasonFreeTextTf.setMessage("")
                                listener?.onTypeFreeTextAndMoreThan10Words(true, s.toString())
                            }
                            else {
                                hotelCancellationReasonFreeTextTf.setError(true)
                                hotelCancellationReasonFreeTextTf.setMessage(root.resources.getString(R.string.hotel_cancellation_reason_free_text_minimal_10_char))
                                listener?.onTypeFreeTextAndMoreThan10Words(false)
                            }
                        }
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { /* do nothing */ }
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { /* do nothing */ }
                })
            }
        }
    }

    class HeaderViewHolder(view: View): RecyclerView.ViewHolder(view)

    interface OnClickItemListener{
        fun onClick(selectedId: String, valid: Boolean = true)
        fun onTypeFreeTextAndMoreThan10Words(valid: Boolean, content: String = "")
    }

    companion object {
        const val TYPE_HEADER = 99990
        const val TYPE_ITEM = 20

        const val TYPE_EDITTEXT_DELAY_MS = 300
        const val MIN_EDITTEXT_CHAR = 10
    }
}
