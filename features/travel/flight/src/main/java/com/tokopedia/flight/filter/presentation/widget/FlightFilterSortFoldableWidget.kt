package com.tokopedia.flight.filter.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.flight.R
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_flight_filter_sort_foldable.view.*

/**
 * @author by jessica on 2020-02-19
 */

class FlightFilterSortFoldableWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    var titleText: String = ""
    var isFoldAble: Boolean = true
    var hasShowMore: Boolean = true
    var listener: ActionListener? = null
    var maxItemCount: Int = 5

    init {
        View.inflate(context, R.layout.widget_flight_filter_sort_foldable, this)
    }

    /**
     *  the buildView() function is expected to be called after all params set.
     */
    fun buildView() {
        tv_title.text = titleText

        if (isFoldAble) {
            ic_arrow_up.show()
            ic_arrow_up.setOnClickListener {
                if (layout_child.isVisible) layout_child.hide() else layout_child.show()
            }
        } else ic_arrow_up.hide()

        if (hasShowMore) {
            tv_show_more.show()
            tv_show_more.setOnClickListener {
                listener?.onClickShowMore()
            }
        } else tv_show_more.hide()
    }

    interface ActionListener {
        fun onClickShowMore()
    }

}