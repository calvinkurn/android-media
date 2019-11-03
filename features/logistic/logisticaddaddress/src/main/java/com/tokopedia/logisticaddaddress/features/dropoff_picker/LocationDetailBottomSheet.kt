package com.tokopedia.logisticaddaddress.features.dropoff_picker

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.domain.model.dropoff.Data
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class LocationDetailBottomSheet : ConstraintLayout {

    var tvOpening: Typography
    var tvTitle: Typography
    var tvDetail: Typography
    var tvOkButton: UnifyButton
    var tvCancelButton: UnifyButton
    private var mData: Data? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.bottomsheet_dropoff_detail, this)
        tvOpening = findViewById(R.id.tv_opening)
        tvTitle = findViewById(R.id.tv_title)
        tvDetail = findViewById(R.id.tv_detail)
        tvOkButton = findViewById(R.id.button_choose)
        tvCancelButton = findViewById(R.id.button_cancel)
    }

    fun setStore(datum: Data) {
        mData = datum
        tvOpening.text = datum.openingHours
        tvTitle.text = datum.addrName
        tvDetail.text = datum.address1
        invalidate()
        requestLayout()
    }

}