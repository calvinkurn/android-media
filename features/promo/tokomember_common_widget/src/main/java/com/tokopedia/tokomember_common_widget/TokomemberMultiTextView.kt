package com.tokopedia.tokomember_common_widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.LinearLayout
import com.tokopedia.unifyprinciples.Typography

class TokomemberMultiTextView @JvmOverloads constructor(context:Context,
                                                        attrs:AttributeSet?=null,
                                                        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var attrArray : TypedArray? = null
    var descriptionTv: Typography? = null
    var valueTv:Typography? = null

    init{
        inflate(context,R.layout.tm_multi_text_view,this)
        initViews()
        initSetup(context,attrs)
    }

    private fun initViews(){
        descriptionTv = findViewById(R.id.tm_desc_tv)
        valueTv = findViewById(R.id.tm_value_tv)
    }

    private fun initSetup(context: Context,attrs:AttributeSet?){
        attrs.let {
            attrArray = context.obtainStyledAttributes(it,R.styleable.TokomemberMultiTextView)
            descriptionTv?.text= attrArray?.getString(R.styleable.TokomemberMultiTextView_tmDescriptionText) ?: ""
            valueTv?.text= attrArray?.getString(R.styleable.TokomemberMultiTextView_valueText) ?: ""
        }
    }
}
