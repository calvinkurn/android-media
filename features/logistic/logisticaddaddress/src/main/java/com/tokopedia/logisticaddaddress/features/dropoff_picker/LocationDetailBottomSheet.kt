package com.tokopedia.logisticaddaddress.features.dropoff_picker

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticdata.data.entity.address.LocationDataModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class LocationDetailBottomSheet : ConstraintLayout {

    var tvOpening: Typography
    var tvTitle: Typography
    var tvDetail: Typography
    var buttonOk: UnifyButton
    var buttonCancel: UnifyButton
    private var mData: LocationDataModel? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.bottomsheet_dropoff_detail, this)
        tvOpening = findViewById(R.id.tv_opening)
        tvTitle = findViewById(R.id.tv_title)
        tvDetail = findViewById(R.id.tv_detail)
        buttonOk = findViewById(R.id.button_choose)
        buttonCancel = findViewById(R.id.button_cancel)
    }

    fun setStore(datum: LocationDataModel) {
        mData = datum
        tvOpening.text = datum.openingHours
        tvTitle.text = datum.addrName
        tvDetail.text = datum.address1
        invalidate()
        requestLayout()
    }

    fun setOnOkClickListener(listener: (view: View, data: LocationDataModel?) -> Unit) {
        buttonOk.setOnClickListener { listener(it, mData) }
    }

    fun setOnCancelClickListener(listener: (view: View) -> Unit) {
        buttonCancel.setOnClickListener(listener)
    }

}