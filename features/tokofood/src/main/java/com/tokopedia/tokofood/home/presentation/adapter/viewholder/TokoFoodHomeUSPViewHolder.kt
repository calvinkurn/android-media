package com.tokopedia.tokofood.home.presentation.adapter.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeLayoutState
import com.tokopedia.tokofood.home.domain.data.USPResponse
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeUSPUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class TokoFoodHomeUSPViewHolder(
    itemView: View,
    private val listener: TokoFoodUSPListener? = null
): AbstractViewHolder<TokoFoodHomeUSPUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_usp

        private const val USP_SIZE = 3
        private const val GO_FOOD_IMAGE = "https://images.tokopedia.net/img/ic_tokofood_gofood.png"
    }

    private var imgFirstUsp: ImageUnify? = null
    private var imgSecondUsp: ImageUnify? = null
    private var imgThirdUsp: ImageUnify? = null
    private var imgGoFood: ImageView? = null
    private var tgFirstUsp: Typography? = null
    private var tgSecondUsp: Typography? = null
    private var tgThirdUsp: Typography? = null
    private var shimmeringLayout: View? = null

    override fun bind(element: TokoFoodHomeUSPUiModel) {
        shimmeringLayout = itemView.findViewById(R.id.usp_load_layout)
        imgFirstUsp = itemView.findViewById(R.id.img_first_usp)
        imgSecondUsp = itemView.findViewById(R.id.img_second_usp)
        imgThirdUsp = itemView.findViewById(R.id.img_third_usp)
        imgGoFood = itemView.findViewById(R.id.img_gofood)
        tgFirstUsp = itemView.findViewById(R.id.tg_first_usp)
        tgSecondUsp = itemView.findViewById(R.id.tg_second_usp)
        tgThirdUsp = itemView.findViewById(R.id.tg_third_usp)

        when(element.state){
            TokoFoodHomeLayoutState.SHOW -> onShowLayout(element)
            TokoFoodHomeLayoutState.LOADING -> onLoadLayout()
        }
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
                imgGoFood?.loadImage(GO_FOOD_IMAGE)
                tgFirstUsp?.text = it.get(0).title
                tgSecondUsp?.text = it.get(1).title
                tgThirdUsp?.text = it.get(2).title
            }
        }

        itemView?.setOnClickListener {
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