package com.tokopedia.picker.widget.drawerselector.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.picker.common.R
import com.tokopedia.picker.common.databinding.ViewItemSelectionPlaceholderDebugBinding
import com.tokopedia.utils.view.binding.viewBinding

class DebugPlaceholderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding: ViewItemSelectionPlaceholderDebugBinding? by viewBinding()

    fun bind(bgColor: Int, @DrawableRes placeholder: Int) {
        binding?.imageView?.setBackgroundColor(bgColor)

        binding?.imageViewPlaceholder?.setImageDrawable(
            MethodChecker.getDrawable(
                binding?.imageViewPlaceholder?.context,
                placeholder
            )
        )
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.view_item_selection_placeholder_debug

        fun create(viewGroup: ViewGroup): DebugPlaceholderViewHolder {
            val view = LayoutInflater
                .from(viewGroup.context)
                .inflate(LAYOUT, viewGroup, false)

            return DebugPlaceholderViewHolder(view)
        }
    }

}