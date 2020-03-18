package com.tokopedia.layanan_finansial.view.customview

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.layanan_finansial.R
import com.tokopedia.layanan_finansial.view.adapter.LayananAdapter
import com.tokopedia.layanan_finansial.view.models.LayananSectionModel
import kotlinx.android.synthetic.main.layanan_section_view.view.*

class LayananSectionView : RelativeLayout {

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView()
    }

    private fun initView() {
        View.inflate(context, R.layout.layanan_section_view, this)
    }

    fun setData(it: LayananSectionModel) {
        title.text = it.title
        subTitle.text = it.subtitle
        if (it.type.equals("verticle")) {
            recycler_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        } else {
            recycler_view.layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        }
        recycler_view.adapter = LayananAdapter(it.list ?: mutableListOf())
    }

}