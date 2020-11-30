package com.tokopedia.vouchercreation.common.bottmsheet.description

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import kotlinx.android.synthetic.main.bottomsheet_mvc_description.*

/**
 * Created By @ilhamsuaib on 07/05/20
 */

class DescriptionBottomSheet : BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(context: Context,
                           title: String): DescriptionBottomSheet {
            return DescriptionBottomSheet().apply {
                context.run {
                    val inflater = LayoutInflater.from(this)
                    val child = inflater.inflate(R.layout.bottomsheet_mvc_description, LinearLayout(this), false)
                    setChild(child)
                    setTitle(title)
                    setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
                }
            }
        }

        const val TAG = "DescriptionBottomSheet"
    }

    fun show(content: String, fm: FragmentManager) {
        tvMvcDescription?.text = content.parseAsHtml()
        show(fm, TAG)
    }
}