package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs

import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.unifyprinciples.Typography as RTypography

class TabsItemImageViewHolder(
    itemView: View,
    fragment: Fragment
) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private val tabIconView: ImageView = itemView.findViewById(R.id.imgIcon)
    private var tabTitleView: RTypography = itemView.findViewById(R.id.tvTitle)
    private var viewModel: TabsItemImageViewModel? = null
    private var itemData: DataItem? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as TabsItemImageViewModel
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
        val isImageUrlAvailable = item.tabActiveImageUrl?.isNotEmpty() == true
            && item.tabInactiveImageUrl?.isNotEmpty() == true

        if (isImageUrlAvailable) {
            setTabIcon(item)
        } else {
            item.name?.let { name ->
                tabIconView.hide()
                setTabText(name)
                setFontColor(item)
            }
        }
    }

    private fun setTabIcon(item: DataItem) {
        tabTitleView.hide()

        if (item.isSelected) {
            tabIconView.loadImage(item.tabActiveImageUrl)
        } else {
            tabIconView.loadImage(item.tabInactiveImageUrl)
        }
        tabIconView.show()
    }

    private fun setTabText(name: String) {
        tabTitleView.setTextAndCheckShow(name)
    }

    private fun setFontColor(item: DataItem) {
        try {
            if (item.isSelected) {
                tabTitleView.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        unifyprinciplesR.color.Unify_GN500
                    )
                )
            } else {
                tabTitleView.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        unifyprinciplesR.color.Unify_NN400
                    )
                )
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }
    //endregion
}
