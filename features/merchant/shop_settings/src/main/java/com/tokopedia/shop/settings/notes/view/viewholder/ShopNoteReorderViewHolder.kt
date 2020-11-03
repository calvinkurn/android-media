package com.tokopedia.shop.settings.notes.view.viewholder

import androidx.core.view.MotionEventCompat
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.touchhelper.OnStartDragListener
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.util.*
import com.tokopedia.shop.settings.notes.data.ShopNoteDataModel

/**
 * Created by hendry on 16/08/18.
 */
class ShopNoteReorderViewHolder(itemView: View,
                                private val onStartDragListener: OnStartDragListener?) : AbstractViewHolder<ShopNoteDataModel>(itemView) {

    private val tvNoteName: TextView
    private val tvLastUpdate: TextView
    private val handler: View

    init {
        tvNoteName = itemView.findViewById(R.id.tvNoteName)
        tvLastUpdate = itemView.findViewById(R.id.tvLastUpdate)
        handler = itemView.findViewById(R.id.ivReorder)
    }

    override fun bind(shopNoteDataModel: ShopNoteDataModel) {
        tvNoteName.text = shopNoteDataModel.title
        tvLastUpdate.text = toReadableString(FORMAT_DATE_TIME, shopNoteDataModel.updateTimeUTC)

        if (shopNoteDataModel.terms) {
            handler.visibility = View.GONE
        } else {
            handler.setOnTouchListener { _, event ->
                @Suppress("DEPRECATION")
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    onStartDragListener?.onStartDrag(this@ShopNoteReorderViewHolder)
                }
                false
            }
            handler.visibility = View.VISIBLE
        }
    }

    companion object {

        val LAYOUT = R.layout.item_shop_note_reorder
    }

}
