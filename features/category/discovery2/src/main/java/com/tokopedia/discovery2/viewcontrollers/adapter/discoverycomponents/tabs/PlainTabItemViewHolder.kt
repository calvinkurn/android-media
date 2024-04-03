package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.crashlytics.FirebaseCrashlytics
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
    private var itemData: DataItem? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as PlainTabItemViewModel
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.let {
            viewModel?.getComponentData()?.observe(
                lifecycleOwner
            ) { componentsItem ->
                componentsItem.data?.firstOrNull()?.let {
                    itemData = it
                    renderTabItem(it)
                }
            }
            viewModel?.getSelectionChangeLiveData()?.observe(lifecycleOwner) {
                itemData?.let { dataItem ->
                    renderTabItem(dataItem)
                }
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            viewModel?.getComponentData()?.removeObservers(lifecycleOwner)
            viewModel?.getSelectionChangeLiveData()?.removeObservers(lifecycleOwner)
        }
    }

    //region private methods
    private fun renderTabItem(item: DataItem) {
        val isImageUrlAvailable = item.tabActiveImageUrl?.isNotEmpty() == true &&
            item.tabInactiveImageUrl?.isNotEmpty() == true

        if (isImageUrlAvailable) {
            setTabIcon(item)
        } else {
            item.name?.let { name ->
                tabIconView.hide()
                setTabText(name)
                setFontColor(item)
                setFontWeight(item.isSelected)
            }
        }
    }

    private fun setTabIcon(item: DataItem) {
        tabTitleView.hide()

        val imageUrl = item.getImageUrl()
        tabIconView.loadImageWithoutPlaceholder(imageUrl)

        redefineImageDimension(imageUrl)

        tintImage(item)

        tabIconView.show()
    }

    private fun tintImage(item: DataItem) {
        val tintColor = item.getDynamicColor()

        if (!tintColor.isNullOrEmpty()) {
            tabIconView.backgroundTintList = ColorStateList.valueOf(
                Color.parseColor(tintColor)
            )
        }
    }

    private fun DataItem.getDynamicColor(): String? {
        return if (isSelected) {
            fontColor
        } else {
            inactiveFontColor
        }
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

    private fun setFontColor(item: DataItem) {
        try {
            val textColor = if (!item.getDynamicColor().isNullOrEmpty()) {
                Color.parseColor(Utils.getValidHexCode(itemView.context, item.getDynamicColor()))
            } else {
                ContextCompat.getColor(itemView.context, item.getDefaultFontColor())
            }

            tabTitleView.setTextColor(textColor)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
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
