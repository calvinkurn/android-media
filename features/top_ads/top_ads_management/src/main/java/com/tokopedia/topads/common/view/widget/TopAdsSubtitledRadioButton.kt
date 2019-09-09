package com.tokopedia.topads.common.view.widget

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.topads.R
import com.tokopedia.topads.common.view.listener.RadioCheckable
import kotlinx.android.synthetic.main.widget_top_ads_subtitled_radio_button.view.*

class TopAdsSubtitledRadioButton: LinearLayout, RadioCheckable {

    private var isChecked = false
    private var title: String? = null
    private var subtitle: String? = null
    private var radioId = View.NO_ID
    private val onCheckedChangeListeners = arrayListOf<RadioCheckable.OnCheckedChangeListener>()

    constructor(context: Context): super(context){
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init(attrs)
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr){
        init(attrs)
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int):
            super(context, attrs, defStyleAttr, defStyleRes){
        init(attrs)
    }

    override fun getOrientation(): Int {
        return VERTICAL
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        radioButton.isChecked = isChecked
        radioTitle.text = title
        radioSubtitle.text = subtitle
        invalidate()
        requestLayout()
    }

    private fun init(){
        val view = View.inflate(context, R.layout.widget_top_ads_subtitled_radio_button, this)
        radioButton.id = radioId
        radioButton.setOnClickListener{ setChecked(true) }
        view.setOnClickListener { setChecked(true) }
        orientation = VERTICAL
    }

    private fun init(attrs: AttributeSet){
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TopAdsSubtitledRadioButton)
        try {
            title = styledAttributes.getString(R.styleable.TopAdsSubtitledRadioButton_title_radio)
            subtitle = styledAttributes.getString(R.styleable.TopAdsSubtitledRadioButton_subtitle_radio)
            isChecked = styledAttributes.getBoolean(R.styleable.TopAdsSubtitledRadioButton_checked, false)
            radioId = styledAttributes.getResourceId(R.styleable.TopAdsSubtitledRadioButton_radio_id, View.NO_ID)
        } finally {
            styledAttributes.recycle()
        }
        init()
    }

    override fun setChecked(b: Boolean){
        if (this.isChecked != b){
            isChecked = b
            radioButton.isChecked = b
            if (!onCheckedChangeListeners.isEmpty()){
                onCheckedChangeListeners.forEach { it.onCheckedChanged(this@TopAdsSubtitledRadioButton, b) }
            }
        }


    }

    override fun isChecked(): Boolean {
        return isChecked
    }

    override fun toggle() {
        setChecked(!isChecked)
    }

    override fun addOnCheckChangeListener(listener: RadioCheckable.OnCheckedChangeListener) {
        onCheckedChangeListeners.add(listener)
    }

    override fun removeOnCheckChangeListener(listener: RadioCheckable.OnCheckedChangeListener) {
        onCheckedChangeListeners.remove(listener)
    }
}