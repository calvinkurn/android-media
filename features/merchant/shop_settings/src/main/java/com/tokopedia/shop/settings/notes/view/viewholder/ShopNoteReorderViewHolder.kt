package com.tokopedia.shop.settings.notes.view.viewholder

import android.annotation.SuppressLint
import androidx.core.view.MotionEventCompat
import android.view.MotionEvent
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.util.*
import com.tokopedia.shop.settings.notes.data.ShopNoteUiModel
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by hendry on 16/08/18.
 */
class ShopNoteReorderViewHolder(itemView: View,
                                private var onStartDragListener: OnStartDragListener?) : AbstractViewHolder<ShopNoteUiModel>(itemView) {

    private var tvNoteName: Typography? = null
    private var tvLastUpdate: Typography? = null
    private var ivReorder: View? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun bind(shopNoteUiModel: ShopNoteUiModel) {
        itemView.apply {
            tvNoteName = findViewById(R.id.tvNoteName)
            tvLastUpdate = findViewById(R.id.tvLastUpdate)
            ivReorder = findViewById(R.id.ivReorder)

            tvNoteName?.text = shopNoteUiModel.title
            tvLastUpdate?.text = toReadableString(FORMAT_DATE_TIME, shopNoteUiModel.updateTimeUTC)

            ivReorder?.setOnTouchListener { _, event ->
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    onStartDragListener?.onStartDrag(this@ShopNoteReorderViewHolder)
                }
                false
            }
            ivReorder?.visibility = View.VISIBLE
        }
    }

    companion object {
        val LAYOUT = R.layout.item_shop_note_reorder
    }

}
