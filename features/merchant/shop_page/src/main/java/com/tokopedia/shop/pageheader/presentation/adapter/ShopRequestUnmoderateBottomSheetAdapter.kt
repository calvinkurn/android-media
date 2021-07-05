package com.tokopedia.shop.pageheader.presentation.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.shop.R
import kotlinx.android.synthetic.main.item_shop_request_unmoderate.view.*

class ShopRequestUnmoderateBottomSheetAdapter(
        context: Context?,
        private val bottomsheetViewHolderListener: ShopRequestUnmoderateBottomsheetViewHolderListener
) : RecyclerView.Adapter<ShopRequestUnmoderateBottomSheetAdapter.ShopRequestUnmoderateBottomSheetViewHolder>() {

    companion object {
        @LayoutRes
        private val VIEWHOLDER_LAYOUT = R.layout.item_shop_request_unmoderate
    }

    private val requestUnmoderateOptionList = listOf(
            ShopUnmoderateOption(
                    context?.getString(R.string.shop_page_header_request_unmoderate_option_1_text),
                    context?.getString(R.string.shop_page_header_request_unmoderate_option_1_value)
            ),
            ShopUnmoderateOption(
                    context?.getString(R.string.shop_page_header_request_unmoderate_option_2_text),
                    context?.getString(R.string.shop_page_header_request_unmoderate_option_2_value)
            )
    )
    private var lastSelectedRadioPosition = 0

    init {
        // set initial radio picker value
        if(requestUnmoderateOptionList.isNotEmpty()) {
            bottomsheetViewHolderListener.setOptionValue(requestUnmoderateOptionList[0].optionValue)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopRequestUnmoderateBottomSheetViewHolder {
        return ShopRequestUnmoderateBottomSheetViewHolder(
                parent.inflateLayout(VIEWHOLDER_LAYOUT)
        )
    }

    override fun getItemCount(): Int {
        return requestUnmoderateOptionList.size
    }

    override fun onBindViewHolder(holder: ShopRequestUnmoderateBottomSheetViewHolder, position: Int) {
        holder.bind(requestUnmoderateOptionList[position])
        holder.itemView.btn_radio_unmoderate_option?.isChecked = (lastSelectedRadioPosition == position)
    }


    /**
     * Adapter view holder inner class
     */
    inner class ShopRequestUnmoderateBottomSheetViewHolder(
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ShopUnmoderateOption) {
            itemView.tv_option_name?.text = item.optionText
            itemView.setOnClickListener {
                lastSelectedRadioPosition = adapterPosition
                notifyDataSetChanged()
                bottomsheetViewHolderListener.setOptionValue(item.optionValue)
            }
        }

    }

}

interface ShopRequestUnmoderateBottomsheetViewHolderListener {
    fun setOptionValue(optionValue : String?)
}

/**
 * Shop unmoderate option ui model
 */
data class ShopUnmoderateOption(
        val optionText: String? = "",
        val optionValue: String? = ""
)