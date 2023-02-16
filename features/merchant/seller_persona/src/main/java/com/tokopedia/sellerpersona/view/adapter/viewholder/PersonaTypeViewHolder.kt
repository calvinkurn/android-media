package com.tokopedia.sellerpersona.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.common.Constants
import com.tokopedia.sellerpersona.databinding.ItemPersonaTypeBinding
import com.tokopedia.sellerpersona.view.adapter.PersonaSimpleListAdapter
import com.tokopedia.sellerpersona.view.model.PersonaUiModel

/**
 * Created by @ilhamsuaib on 09/02/23.
 */

class PersonaTypeViewHolder(
    private val listener: Listener,
    itemView: View
) : AbstractViewHolder<PersonaUiModel>(itemView) {

    companion object {
        private const val SUB_TITLE_FORMAT = "(%s)"

        @LayoutRes
        val RES_LAYOUT = R.layout.item_persona_type
    }

    private val optionAdapter by lazy { PersonaSimpleListAdapter() }
    private val binding by lazy { ItemPersonaTypeBinding.bind(itemView) }

    override fun bind(element: PersonaUiModel) {
        with(binding) {
            tvSpPersonaType.text = element.headerTitle
            tvSpSellerTypeStatus.text = String.format(SUB_TITLE_FORMAT, element.headerSubTitle)
            radioSpPersonaType.isChecked = element.isSelected
            imgSpSellerTypeAvatar.loadImage(element.avatarImage)
            containerSpItemPersonaType.setBackgroundResource(R.drawable.sp_bg_seller_type)

            showList(element)
            showBackground(element)
            handleClickState(element)
        }
    }

    private fun handleClickState(element: PersonaUiModel) {
        with(binding) {
            radioSpPersonaType.setOnCheckedChangeListener { _, isChecked ->
                element.isSelected = isChecked
                showBackground(element)
                listener.onItemClickListener(element)
            }
            containerSpItemPersonaType.setOnClickListener {
                onItemClicked()
            }
        }
    }

    private fun onItemClicked() {
        with(binding) {
            if (!radioSpPersonaType.isChecked) {
                radioSpPersonaType.isChecked = !radioSpPersonaType.isChecked
            }
        }
    }

    private fun showBackground(element: PersonaUiModel) {
        with(binding) {
            val sectionTextColor = if (element.isSelected) {
                root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_NN950)
            } else {
                root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_NN600)
            }
            tvSpSellerTypeLblInfo.setTextColor(sectionTextColor)

            val subTitleTextColor = if (element.isSelected) {
                root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_GN500)
            } else {
                root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_NN600)
            }
            tvSpSellerTypeStatus.setTextColor(subTitleTextColor)

            runCatching {
                if (element.isSelected) {
                    imgSpItemPersonaType.loadImage(Constants.IMG_PERSONA_TYPE_ACTIVE) {
                        setPlaceHolder(R.drawable.sp_bg_seller_type_inactive)
                    }
                } else {
                    imgSpItemPersonaType.loadImage(R.drawable.sp_bg_seller_type_inactive)
                }
            }
            optionAdapter.isSelected = element.isSelected
            optionAdapter.notifyAdapter()
        }
    }

    private fun showList(element: PersonaUiModel) {
        with(binding.rvSpSelectTypeInfo) {
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollHorizontally(): Boolean = false

                override fun canScrollVertically(): Boolean = false
            }
            adapter = optionAdapter

            optionAdapter.setOnItemClickListener {
                onItemClicked()
            }
            optionAdapter.setItems(element.itemList)
            optionAdapter.isSelected = element.isSelected
            optionAdapter.notifyAdapter()
        }
    }

    interface Listener {
        fun onItemClickListener(item: PersonaUiModel)
    }
}