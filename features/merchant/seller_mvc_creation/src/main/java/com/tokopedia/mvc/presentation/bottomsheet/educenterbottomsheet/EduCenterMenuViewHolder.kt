package com.tokopedia.mvc.presentation.bottomsheet.educenterbottomsheet

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcItemEduCenterBottomSheetBinding
import com.tokopedia.mvc.presentation.bottomsheet.educenterbottomsheet.model.EduCenterMenuModel
import com.tokopedia.utils.view.binding.viewBinding


class EduCenterMenuViewHolder(itemView: View, private val listener: EduCenterClickListener) : RecyclerView.ViewHolder(itemView)  {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.smvc_item_edu_center_bottom_sheet
    }

    private val binding by viewBinding<SmvcItemEduCenterBottomSheetBinding>()

    fun bind(menu: EduCenterMenuModel) {
        binding?.tvEduCenterTitle?.text = menu.title
        itemView.setOnClickListener {
            listener.onMenuClicked(menu)
        }
    }

}
