package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.loadmore

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment

class LoadMoreViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private var loadMoreViewModel: LoadMoreViewModel? = null
    private var parentLayout: ConstraintLayout = itemView.findViewById(R.id.root_layout)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        loadMoreViewModel = discoveryBaseViewModel as LoadMoreViewModel
        loadMoreViewModel?.checkForDarkMode(itemView.context)
        loadMoreViewModel?.let {
            getSubComponent().inject(it)
        }
        setLoaderView()
    }

    private fun setLoaderView() {
        if (loadMoreViewModel?.getViewOrientation() == true) {
            val layoutParams: ViewGroup.LayoutParams = parentLayout.layoutParams
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            parentLayout.layoutParams = layoutParams
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            loadMoreViewModel?.getSyncPageLiveData()?.observe(lifecycleOwner) {
                (fragment as DiscoveryFragment).reSync()
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            loadMoreViewModel?.getSyncPageLiveData()?.removeObservers(it)
        }
    }
}
