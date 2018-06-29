package com.tokopedia.topads.dashboard.view.widget

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.widget.Checkable
import android.widget.LinearLayout

class TopAdsSubtitledRadioButton: LinearLayout, Checkable {
    private var isChecked = false

    override fun isChecked(): Boolean {
        return isChecked
    }

    override fun toggle() {
        isChecked != isChecked
    }

    override fun setChecked(isChecked: Boolean) {
        this.isChecked = isChecked
    }

    constructor(context: Context): super(context){

    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int):
            super(context, attrs, defStyleAttr, defStyleRes)
}