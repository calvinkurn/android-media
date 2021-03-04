package com.tokopedia.buyerorder.detail.view.customview

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.TouchDelegate
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.buyerorder.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography

class CopyableDetailItemView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BaseCustomView(context, attrs, defStyleAttr) {

    var titleTextView: Typography
    var descTextView: Typography
    var copyIcon: ImageView

    var listener: Listener? = null
    set(value) {
        field = value
        if (value != null) {
            copyIcon.setOnClickListener { value.onCopyValue() }
        }
    }

    init {
        val view = View.inflate(context, getLayout(), this)
        titleTextView = view.findViewById(R.id.detail_item_title)
        descTextView = view.findViewById(R.id.detail_item_desc)
        copyIcon = view.findViewById(R.id.detail_item_copy_icon)

        // Enlarge copy icon touch area
        val descSection: ConstraintLayout = view.findViewById(R.id.detail_item_desc_section)
        descSection.post {
            val delegateArea = Rect()
            copyIcon.getHitRect(delegateArea)

            delegateArea.top -= COPY_TOUCH_AREA_SIZE_PX
            delegateArea.left -= COPY_TOUCH_AREA_SIZE_PX
            delegateArea.bottom += COPY_TOUCH_AREA_SIZE_PX
            delegateArea.right += COPY_TOUCH_AREA_SIZE_PX

            descSection.touchDelegate = TouchDelegate(delegateArea, copyIcon)
        }
    }

    fun setTitle(title: String) {
        titleTextView.text = title
    }

    fun setDescription(desc: String) {
        descTextView.text = desc
    }

    fun getLayout(): Int {
        return R.layout.order_list_detail_item_button
    }

    interface Listener {
        fun onCopyValue()
    }

    companion object {
        const val COPY_TOUCH_AREA_SIZE_PX = 20
    }
}