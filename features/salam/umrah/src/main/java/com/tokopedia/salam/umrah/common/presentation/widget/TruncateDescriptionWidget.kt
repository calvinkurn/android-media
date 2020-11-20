package com.tokopedia.salam.umrah.common.presentation.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.salam.umrah.R
import kotlinx.android.synthetic.main.widget_truncate_desc.view.*

class TruncateDescriptionWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BaseCustomView(context, attrs, defStyleAttr) {
    var descriptionLineCount = INFO_DESC_DEFAULT_LINE_COUNT
    var truncateDescription = SHOW_DESC
    lateinit var trackingListener: TruncateDescriptionTrackingListener

    var truncateDescriptionListener: TruncateDescriptionListener? = null
        set(value) {
            field = value
            truncateDescriptionListener?.let { truncateDescriptionListener ->
                tg_load_more.setOnClickListener {
                    trackingListener.onClicked()
                    truncateDescriptionListener.onMoreClicked(truncateDescription)
                }
            }
        }

    init {
        View.inflate(context, R.layout.widget_truncate_desc, this)
        attrs?.let {
            val styledAttribute = context.obtainStyledAttributes(it, R.styleable.TruncateDescription)

            try {
                tg_desc.text = styledAttribute.getString(R.styleable.TruncateDescription_descriptionText) ?: ""
                truncateDescription = styledAttribute.getBoolean(R.styleable.TruncateDescription_truncateDescription, true)

                if (truncateDescription) {
                    tg_desc.post {
                        buildTruncate()
                    }
                } else {
                    resetMaxLineCount()
                }

            } finally {
                styledAttribute.recycle()
            }

            truncateDescriptionListener = object : TruncateDescriptionListener{
                override fun onMoreClicked(toogle: Boolean) {
                    if(!toogle) resetMaxLineCount()
                    else buildTruncate()
                    invalidate()
                }
            }
        }
    }

    fun setDescription(desc: CharSequence){
        tg_desc.text = desc
    }

    fun buildView(listener: TruncateDescriptionTrackingListener){
        trackingListener = listener
        visibility = View.VISIBLE
        if(truncateDescription){
            buildTruncate()
        }
    }

    private fun buildTruncate(){
        if(tg_desc.lineCount>descriptionLineCount){
            tg_desc.ellipsize = TextUtils.TruncateAt.END
            tg_desc.maxLines = descriptionLineCount
            tg_load_more.visibility = View.VISIBLE
            tg_load_more.text = resources.getString(R.string.umrah_travel_read_more)
            truncateDescription = HIDE_DESC
        }
    }

    private fun resetMaxLineCount(){
        tg_desc.ellipsize = null
        tg_desc.maxLines = Integer.MAX_VALUE
        tg_load_more.text = resources.getString(R.string.umrah_travel_hide_more)
        if(tg_desc.lineCount>descriptionLineCount) {
            tg_load_more.visibility = View.VISIBLE
            truncateDescription = SHOW_DESC
        }
    }


    interface TruncateDescriptionListener {
        fun onMoreClicked(toogle: Boolean)
    }

    interface TruncateDescriptionTrackingListener{
        fun onClicked()
    }

    companion object {
        const val INFO_DESC_DEFAULT_LINE_COUNT = 3
        const val SHOW_DESC = true
        const val HIDE_DESC = false
    }

}