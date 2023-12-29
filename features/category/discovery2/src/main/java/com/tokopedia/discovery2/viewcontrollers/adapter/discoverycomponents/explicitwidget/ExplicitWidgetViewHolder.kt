package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.explicitwidget

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.analytics.EMPTY_STRING
import com.tokopedia.discovery2.databinding.ExplicitWidgetLayoutBinding
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.observeOnce
import com.tokopedia.usercomponents.explicit.view.ExplicitData
import com.tokopedia.usercomponents.explicit.view.ExplicitView

class ExplicitWidgetViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private val binding: ExplicitWidgetLayoutBinding = ExplicitWidgetLayoutBinding.bind(itemView)
    private var explicitView: ExplicitView? = null
    private var mExplicitWidgetViewModel: ExplicitWidgetViewModel? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mExplicitWidgetViewModel = discoveryBaseViewModel as ExplicitWidgetViewModel
        mExplicitWidgetViewModel?.let {
            getSubComponent().inject(it)
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mExplicitWidgetViewModel?.getComponentData()?.observeOnce(
                it,
                Observer { componentItem ->
                    if (explicitView != null && !componentItem.isExplicitWidgetHidden) {
                        explicitView = ExplicitView(
                            itemView.context,
                            null
                        )

                        // data model required to pass in setupView
                        val explicitData = ExplicitData(
                            templateName = componentItem.data?.firstOrNull()?.templateName
                                ?: "",
                            pageName = (fragment as DiscoveryFragment).arguments?.getString(DiscoveryActivity.SOURCE) ?: "",
                            pagePath = removeDashPagePath(componentItem.pagePath),
                            pageType = componentItem.pageType
                        )

                        mExplicitWidgetViewModel?.explicitViewContract?.let { it1 -> explicitView?.setupView(it1, explicitData) }
                        val param: ViewGroup.LayoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                        binding.parentExplicit.setPadding(
                            itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
                            itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
                            itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
                            itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
                        )
                        explicitView?.layoutParams = param
                        binding.parentExplicit.removeAllViews()
                        binding.parentExplicit.addView(explicitView)
                        explicitView?.setOnWidgetDismissListener {
                            mExplicitWidgetViewModel?.setWidgetHiddenState(true)
                        }
                    }
                }
            )
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
            mExplicitWidgetViewModel?.getComponentData()?.removeObservers(it)
        }
    }
}
