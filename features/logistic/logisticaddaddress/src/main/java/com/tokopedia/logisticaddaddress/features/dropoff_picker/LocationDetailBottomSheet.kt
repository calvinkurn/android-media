package com.tokopedia.logisticaddaddress.features.dropoff_picker

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import com.tokopedia.logisticaddaddress.R

class LocationDetailBottomSheet : ConstraintLayout {

    constructor(context: Context?) : super(context) {
        setupView(context)
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        setupView(context)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setupView(context)
    }

    private fun setupView(context: Context?) {
        View.inflate(context, R.layout.bottomsheet_dropoff_detail, this)
    }
}