package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.explicitwidget

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.analytics.EMPTY_STRING
import com.tokopedia.discovery2.databinding.ExplicitWidgetLayoutBinding
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.observeOnce
import com.tokopedia.usercomponents.explicit.view.ExplicitView

class ExplicitWidgetViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private val binding: ExplicitWidgetLayoutBinding = ExplicitWidgetLayoutBinding.bind(itemView)
    private lateinit var explicitView : ExplicitView
    private lateinit var mExplicitWidgetViewModel: ExplicitWidgetViewModel


    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mExplicitWidgetViewModel = discoveryBaseViewModel as ExplicitWidgetViewModel
        getSubComponent().inject(mExplicitWidgetViewModel)
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mExplicitWidgetViewModel.getComponentData().observeOnce(it, Observer { componentItem ->
                if(!this::explicitView.isInitialized && !componentItem.isExplicitWidgetHidden) {
                    explicitView = ExplicitView(
                            itemView.context,
                            null,
                            templateName = componentItem.data?.firstOrNull()?.templateName
                                    ?: "",
                            pageName = componentItem.name ?:"",
                            pagePath = removeDashPagePath(componentItem.pagePath),
                            pageType = componentItem.pageType
                    )
                    binding.parentExplicit.removeAllViews()
                    val param: ViewGroup.LayoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                    explicitView.layoutParams = param
                    binding.parentExplicit.addView(explicitView)
                    explicitView.setOnWidgetDismissListener {
                        mExplicitWidgetViewModel.setWidgetHiddenState(true)
                    }
                }
            })
        }
    }

    private fun removeDashPagePath(identifier: String): String {
        if (identifier.isNotEmpty()) {
            return identifier.replace("-", " ")
        }
        return EMPTY_STRING
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mExplicitWidgetViewModel.getComponentData().removeObservers(it)
        }
    }


}