package com.tokopedia.layanan_finansial.view.customview

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.layanan_finansial.databinding.LayananSectionViewBinding
import com.tokopedia.layanan_finansial.view.adapter.LayananAdapter
import com.tokopedia.layanan_finansial.view.models.LayananSectionModel

class LayananSectionView : RelativeLayout {

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val binding = LayananSectionViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun setData(it: LayananSectionModel) {
        try {
            if (!it.backgroundColor.isNullOrEmpty()) {
                val list = it.backgroundColor.split("-")
                val gradient = GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    intArrayOf(Color.parseColor(list[0]), Color.parseColor(list[1]))
                )
                gradient.setCornerRadius(0f)
                setBackgroundDrawable(gradient)
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }

        with(binding) {
            title.text = it.title
            subTitle.text = it.subtitle
            if (it.type.equals("vertical")) {
                recyclerView.layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
            } else {
                recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
            recyclerView.adapter = LayananAdapter(it.list ?: mutableListOf())
        }
    }
}
