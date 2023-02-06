package com.tokopedia.play.broadcaster.shorts.view.custom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.broadcaster.databinding.ItemDynamicPreparationMenuBinding
import com.tokopedia.play.broadcaster.shorts.util.animateGone
import com.tokopedia.play.broadcaster.shorts.util.animateShow
import com.tokopedia.unifycomponents.R as unifyR
import com.tokopedia.content.common.R as contentCommonR

/**
 * Created By : Jonathan Darwin on November 09, 2022
 */
internal class DynamicPreparationMenuViewHolder(
    private val binding: ItemDynamicPreparationMenuBinding,
    private val onClick: (DynamicPreparationMenu) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val context = itemView.context

    fun bind(item: DynamicPreparationMenuAdapter.Item) {
        val data = item.data

        with(binding) {
            if (item.isShow) {
                tvMenuTitle.animateShow()
            } else {
                tvMenuTitle.animateGone()
            }

            val stateColor = ContextCompat.getColor(
                context,
                if (data.isEnabled) {
                    unifyR.color.Unify_Static_White
                } else {
                    contentCommonR.color.content_dms_white_disable
                }
            )

            icMenu.setImage(data.iconId, newLightEnable = stateColor, newDarkEnable = stateColor)
            tvMenuTitle.setTextColor(stateColor)
            tvMenuTitle.text = context.getString(data.textResId)
            icMenuChecked.showWithCondition(data.isChecked)

            root.setOnClickListener {
                if (data.isEnabled) onClick(data)
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
