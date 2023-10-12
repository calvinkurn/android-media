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
import com.tokopedia.utils.resources.isDarkMode
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created by @ilhamsuaib on 09/02/23.
 */

class PersonaTypeViewHolder(
    private val listener: Listener, itemView: View
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
            imgSpSellerTypeAvatar.loadImage(element.avatarImage)
            containerSpItemPersonaType.setBackgroundResource(R.drawable.sp_bg_seller_type)

            showList(element)
            showBackground(element)
            handleClickState(element)
        }
    }

    private fun handleClickState(element: PersonaUiModel) {
        with(binding) {
            radioSpPersonaType.isChecked = element.isSelected
            radioSpPersonaType.setOnClickListener {
                containerSpItemPersonaType.performClick()
            }

            containerSpItemPersonaType.setOnClickListener {
                if (element.isSelected) return@setOnClickListener

                element.isSelected = true
                radioSpPersonaType.isChecked = true
                showBackground(element)
                listener.onItemClickListener(element)
            }
        }
    }

    private fun showBackground(element: PersonaUiModel) {
        with(binding) {
            var labelTextColor = unifyR.color.Unify_NN600
            var personaTextColor = unifyR.color.Unify_NN950
            val sectionTextColor = when {
                element.isSelected && root.context.isDarkMode() -> {
                    labelTextColor = unifyR.color.Unify_Static_Black_68
                    personaTextColor = unifyR.color.Unify_Static_Black_96
                    unifyR.color.Unify_Static_Black_96
                }
                element.isSelected -> {
                    labelTextColor = unifyR.color.Unify_Static_Black_68
                    unifyR.color.Unify_NN950
                }
                else -> {
                    unifyR.color.Unify_NN600
                }
            }
            val textColor = root.context.getResColor(sectionTextColor)
            tvSpSellerTypeLblInfo.setTextColor(textColor)
            tvSpPersonaType.setTextColor(root.context.getResColor(personaTextColor))
            tvSpSellerTypeLbl.setTextColor(root.context.getResColor(labelTextColor))

            val subTitleTextColor = if (element.isSelected) {
                root.context.getResColor(unifyR.color.Unify_GN500)
            } else {
                root.context.getResColor(unifyR.color.Unify_NN600)
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
            optionAdapter.isSelectedMode = element.isSelected
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

            optionAdapter.setItems(element.itemList)
            optionAdapter.isSelectedMode = element.isSelected
            optionAdapter.notifyAdapter()
        }
    }

    interface Listener {
        fun onItemClickListener(item: PersonaUiModel)
    }
}