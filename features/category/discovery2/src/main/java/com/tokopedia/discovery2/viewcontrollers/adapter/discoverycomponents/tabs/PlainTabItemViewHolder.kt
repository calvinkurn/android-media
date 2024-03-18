package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.unifyprinciples.Typography as RTypography

class PlainTabItemViewHolder(
    itemView: View,
    fragment: Fragment
) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private val tabIconView: ImageUnify = itemView.findViewById(R.id.imgIcon)
    private var tabTitleView: RTypography = itemView.findViewById(R.id.tvTitle)
    private var viewModel: PlainTabItemViewModel? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as PlainTabItemViewModel
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.let {
            viewModel?.getSelectedLiveData()?.observe(lifecycleOwner) {
                val activeColor = it.fontColor
                it.render(activeColor)
            }

            viewModel?.getUnselectedLiveData()?.observe(lifecycleOwner) { (dataItem, hexColor) ->
                dataItem.render(hexColor)
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            viewModel?.getSelectedLiveData()?.removeObservers(lifecycleOwner)
            viewModel?.getUnselectedLiveData()?.removeObservers(lifecycleOwner)
        }
    }

    //region private methods
    private fun DataItem.render(hexColor: String?) {
        val isImageUrlAvailable = tabActiveImageUrl?.isNotEmpty() == true &&
            tabInactiveImageUrl?.isNotEmpty() == true

        if (isImageUrlAvailable) {
            setTabIcon()
            tintImage(hexColor, getDefaultFontColor())
        } else {
            name?.run {
                tabIconView.hide()
                setTabText(this)

                setFontColor(hexColor, getDefaultFontColor())
                setFontWeight(isSelected)
            }
        }
    }

    private fun DataItem.setTabIcon() {
        tabTitleView.hide()

        val imageUrl = getImageUrl()
        tabIconView.loadImageWithoutPlaceholder(imageUrl)

        redefineImageDimension(imageUrl)

        tabIconView.show()
    }

    private fun tintImage(hexColor: String?, defaultColor: Int) {
        val inactiveColor = if (hexColor.isNullOrEmpty()) {
            ContextCompat.getColor(itemView.context, defaultColor)
        } else {
            Color.parseColor(hexColor)
        }

        tabIconView.setColorFilter(inactiveColor)
    }

    private fun redefineImageDimension(imageUrl: String?) {
        val lp = tabIconView.layoutParams
        val heightParamValue = Utils.extractDimension(imageUrl, HEIGHT_QUERY_PARAM)
        val widthParamValue = Utils.extractDimension(imageUrl, WIDTH_QUERY_PARAM)
        if (widthParamValue != null && heightParamValue != null) {
            val aspectRatio = widthParamValue.toFloat() / heightParamValue.toFloat()
            itemView.context?.resources?.let {
                val dimenHeight = R.dimen.dp_22
                val height = it.getDimensionPixelOffset(dimenHeight)
                lp.height = height
                lp.width = (aspectRatio * height).toInt()
            }
        }
        tabIconView.layoutParams = lp
    }

    private fun DataItem.getImageUrl(): String? {
        return if (isSelected) {
            tabActiveImageUrl
        } else {
            tabInactiveImageUrl
        }
    }

    private fun setTabText(name: String) {
        tabTitleView.setTextAndCheckShow(name)
    }

    private fun setFontColor(hexColor: String?, defaultColor: Int) {
        val textColor = if (!hexColor.isNullOrBlank()) {
            Color.parseColor(Utils.getValidHexCode(itemView.context, hexColor))
        } else {
            ContextCompat.getColor(itemView.context, defaultColor)
        }

        tabTitleView.setTextColor(textColor)
    }

    private fun DataItem.getDefaultFontColor(): Int {
        return if (isSelected) {
            unifyprinciplesR.color.Unify_GN500
        } else {
            unifyprinciplesR.color.Unify_NN950
        }
    }

    private fun setFontWeight(isSelected: Boolean) {
        val type = if (isSelected) {
            Typography.BOLD
        } else {
            Typography.REGULAR
        }

        tabTitleView.setWeight(type)
    }
    //endregion

    companion object {
        private const val HEIGHT_QUERY_PARAM = "height"
        private const val WIDTH_QUERY_PARAM = "width"
    }
}
