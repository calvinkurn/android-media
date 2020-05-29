package com.tokopedia.vouchercreation.common.bottmsheet.description

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import kotlinx.android.synthetic.main.bottomsheet_mvc_description.view.*

/**
 * Created By @ilhamsuaib on 07/05/20
 */

class DescriptionBottomSheet(private val mContext: Context) : BottomSheetUnify() {

    fun show(title: String, content: String, fm: FragmentManager) {
        val inflater = LayoutInflater.from(mContext)
        val child = inflater.inflate(R.layout.bottomsheet_mvc_description, LinearLayout(mContext), false)
        setChild(child)
        setTitle(title)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        child.tvMvcDescription.text = content.parseAsHtml()
        show(fm, this::class.java.simpleName)
    }
}