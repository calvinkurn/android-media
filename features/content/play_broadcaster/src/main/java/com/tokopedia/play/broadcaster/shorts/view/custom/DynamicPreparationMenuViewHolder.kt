package com.tokopedia.play.broadcaster.shorts.view.custom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.broadcaster.databinding.ItemDynamicPreparationMenuBinding
import com.tokopedia.unifycomponents.R as unifyR
import com.tokopedia.play.broadcaster.R

/**
 * Created By : Jonathan Darwin on November 09, 2022
 */
internal class DynamicPreparationMenuViewHolder(
    private val binding: ItemDynamicPreparationMenuBinding,
    private val onClick: (DynamicPreparationMenu) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val context = itemView.context

    fun bind(item: DynamicPreparationMenu) {
        with(binding) {
            val stateColor = ContextCompat.getColor(
                context,
                if (item.isEnabled) unifyR.color.Unify_Static_White
                else R.color.content_dms_white_disable
            )

            icMenu.setImage(item.iconId, newLightEnable = stateColor, newDarkEnable = stateColor)
            tvMenuTitle.setTextColor(stateColor)
            tvMenuTitle.text = context.getString(item.textResId)
            icMenuChecked.showWithCondition(item.isChecked)

            root.setOnClickListener {
                if(item.isEnabled) onClick(item)
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onClick: (DynamicPreparationMenu) -> Unit
        ): DynamicPreparationMenuViewHolder {
            return DynamicPreparationMenuViewHolder(
                ItemDynamicPreparationMenuBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onClick
            )
        }
    }
}
