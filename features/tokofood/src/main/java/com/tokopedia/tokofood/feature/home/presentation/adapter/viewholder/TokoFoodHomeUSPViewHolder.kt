package com.tokopedia.tokofood.feature.home.presentation.adapter.viewholder

import com.tokopedia.imageassets.TokopediaImageUrl

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodUspBinding
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutState
import com.tokopedia.tokofood.feature.home.domain.data.USPResponse
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeUSPUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.resources.isDarkMode
import com.tokopedia.utils.view.binding.viewBinding

class TokoFoodHomeUSPViewHolder(
    itemView: View,
    private val listener: TokoFoodUSPListener? = null
): AbstractViewHolder<TokoFoodHomeUSPUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_usp

        private const val USP_SIZE = 3
        private const val GO_FOOD_IMAGE_LIGHT = TokopediaImageUrl.GO_FOOD_IMAGE_LIGHT
        private const val GO_FOOD_IMAGE_DARK = TokopediaImageUrl.GO_FOOD_IMAGE_DARK
    }

    private var binding: ItemTokofoodUspBinding? by viewBinding()
    private var imgFirstUsp: ImageUnify? = null
    private var imgSecondUsp: ImageUnify? = null
    private var imgThirdUsp: ImageUnify? = null
    private var imgGoFood: ImageView? = null
    private var tgFirstUsp: Typography? = null
    private var tgSecondUsp: Typography? = null
    private var tgThirdUsp: Typography? = null
    private var shimmeringLayout: View? = null

    init {
        setCardBackgroundColor()
        setUspBackgroundColor()
    }

    override fun bind(element: TokoFoodHomeUSPUiModel) {
        shimmeringLayout = binding?.uspLoadLayout?.root
        imgFirstUsp = binding?.imgFirstUsp
        imgSecondUsp = binding?.imgSecondUsp
        imgThirdUsp = binding?.imgThirdUsp
        imgGoFood = binding?.imgGofood
        tgFirstUsp = binding?.tgFirstUsp
        tgSecondUsp = binding?.tgSecondUsp
        tgThirdUsp = binding?.tgThirdUsp

        when(element.state){
            TokoFoodLayoutState.SHOW -> onShowLayout(element)
            TokoFoodLayoutState.LOADING -> onLoadLayout()
        }
    }

    private fun setCardBackgroundColor() {
        binding?.run {
            mainLayout.setCardBackgroundColor(
                MethodChecker.getColor(
                    root.context, com.tokopedia.tokofood.R.color.food_cardview_background_dms_color
                )
            )
        }
    }

    private fun setUspBackgroundColor() {
        binding?.viewUsp?.setBackgroundResource(R.drawable.background_tokofood_usp_chevron)
    }

    private fun onLoadLayout(){
        hideLayout()
        showShimmering()
    }

    private fun onShowLayout(element: TokoFoodHomeUSPUiModel) {
        showLayout()
        hideShimmering()
        element.uspModel?.response?.list?.let {
            if (it.size == USP_SIZE) {
                imgFirstUsp?.loadImage(it.get(0).iconUrl)
                imgSecondUsp?.loadImage(it.get(1).iconUrl)
                imgThirdUsp?.loadImage(it.get(2).iconUrl)
                tgFirstUsp?.text = it.get(0).title
                tgSecondUsp?.text = it.get(1).title
                tgThirdUsp?.text = it.get(2).title

                if (itemView.context.isDarkMode()) {
                    imgGoFood?.loadImage(GO_FOOD_IMAGE_DARK)
                } else {
                    imgGoFood?.loadImage(GO_FOOD_IMAGE_LIGHT)
                }
            }
        }

        binding?.root?.setOnClickListener {
            element.uspModel?.response?.let {
                listener?.onUSPClicked(it)
            }
        }
    }

    private fun showLayout() {
        imgFirstUsp?.show()
        imgSecondUsp?.show()
        imgThirdUsp?.show()
        imgGoFood?.show()
        tgFirstUsp?.show()
        tgSecondUsp?.show()
        tgThirdUsp?.show()
    }

    private fun showShimmering() {
        shimmeringLayout?.show()
    }

    private fun hideLayout() {
        imgFirstUsp?.hide()
        imgSecondUsp?.hide()
        imgThirdUsp?.hide()
        imgGoFood?.hide()
        tgFirstUsp?.hide()
        tgSecondUsp?.hide()
        tgThirdUsp?.hide()
    }

    private fun hideShimmering() {
        shimmeringLayout?.hide()
    }

    interface TokoFoodUSPListener {
        fun onUSPClicked(uspResponse: USPResponse)
    }
}
