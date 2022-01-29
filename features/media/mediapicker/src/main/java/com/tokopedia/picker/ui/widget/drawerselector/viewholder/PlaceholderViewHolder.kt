package com.tokopedia.picker.ui.widget.drawerselector.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.picker.R
import com.tokopedia.picker.databinding.ViewItemSelectionPlaceholderBinding
import com.tokopedia.utils.view.binding.viewBinding

class PlaceholderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding: ViewItemSelectionPlaceholderBinding? by viewBinding()

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
        @LayoutRes val LAYOUT = R.layout.view_item_selection_placeholder

        fun create(viewGroup: ViewGroup): PlaceholderViewHolder {
            val view = LayoutInflater
                .from(viewGroup.context)
                .inflate(LAYOUT, viewGroup, false)

            return PlaceholderViewHolder(view)
        }
    }

}