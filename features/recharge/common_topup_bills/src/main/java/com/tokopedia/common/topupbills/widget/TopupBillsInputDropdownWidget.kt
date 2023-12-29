package com.tokopedia.common.topupbills.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.topupbills.databinding.ViewTopupBillsInputDropdownBottomSheetBinding
import com.tokopedia.common.topupbills.databinding.ViewTopupBillsInputDropdownBottomSheetItemBinding
import com.tokopedia.common.topupbills.view.model.TopupBillsInputDropdownData
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import org.jetbrains.annotations.NotNull

/**
 * Created by resakemal on 11/11/19.
 */
class TopupBillsInputDropdownWidget @JvmOverloads constructor(
    @NotNull context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    var listener: OnClickListener? = null,
    val selected: String = ""
) :
    FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ViewTopupBillsInputDropdownBottomSheetBinding.inflate(LayoutInflater.from(context), this, true)

    private var initialData: List<TopupBillsInputDropdownData> = listOf()
    private var displayData: List<TopupBillsInputDropdownData> = listOf()
        set(value) {
            field = value
            with(binding.vgInputDropdownRecyclerView.adapter as TopupBillsInputDropdownAdapter) {
                items = value
                notifyDataSetChanged()
            }
        }

    init {
        binding.vgInputDropdownRecyclerView.adapter = TopupBillsInputDropdownAdapter(displayData)
        binding.vgInputDropdownSearchView.searchBarTextField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                text?.let {
                    displayData = initialData.filter { item -> item.label.contains(it, true) }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        binding.vgInputDropdownSearchView.clearListener = {
            displayData = initialData
        }
        binding.vgInputDropdownSearchView.searchBarTextField.requestFocus()
    }

    fun setData(data: List<TopupBillsInputDropdownData>) {
        initialData = data
        displayData = data
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }

    inner class TopupBillsInputDropdownAdapter(var items: List<TopupBillsInputDropdownData>) : RecyclerView.Adapter<TopupBillsInputDropdownViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopupBillsInputDropdownViewHolder {
            val binding = ViewTopupBillsInputDropdownBottomSheetItemBinding.inflate(LayoutInflater.from(context), parent, false)
            return TopupBillsInputDropdownViewHolder(binding)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: TopupBillsInputDropdownViewHolder, position: Int) {
            holder.bind(items[position])
        }
    }

    inner class TopupBillsInputDropdownViewHolder(
        private val binding: ViewTopupBillsInputDropdownBottomSheetItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(element: TopupBillsInputDropdownData) {
            with(binding) {
                vgInputDropdownLabel.text = element.label
                vgInputDropdownIcon.cornerRadius = 0
                if (element.icon.isNotEmpty()) {
                    vgInputDropdownIcon.loadImage(element.icon)
                    vgInputDropdownIcon.show()
                } else {
                    vgInputDropdownIcon.hide()
                }

                if (selected.isNotEmpty() && element.label == selected) {
                    vgInputDropdownSelected.show()
                } else {
                    vgInputDropdownSelected.hide()
                }
                vgInputDropdownItem.setOnClickListener { listener?.onItemClicked(element) }
            }
        }
    }

    interface OnClickListener {
        fun onItemClicked(item: TopupBillsInputDropdownData)
    }

    companion object {
        const val SHOW_KEYBOARD_DELAY: Long = 200
    }
}
