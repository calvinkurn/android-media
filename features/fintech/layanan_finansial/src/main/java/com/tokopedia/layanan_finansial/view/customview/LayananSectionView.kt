package com.tokopedia.layanan_finansial.view.customview

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import com.tokopedia.layanan_finansial.R

class LayananSectionView: RelativeLayout {

    constructor(context: Context?) : this(context,null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context,attrs,defStyleAttr){
        initView()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes){
        initView()
    }

    private fun initView() {
      View.inflate(context, R.layout.layanan_section_view,this)
    }

}