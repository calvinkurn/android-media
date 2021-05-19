package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.loadmore

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify

class LoadMoreViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private lateinit var loadMoreViewModel: LoadMoreViewModel
    private var parentLayout: ConstraintLayout = itemView.findViewById(R.id.root_layout)
    private var loaderUnify: LoaderUnify = itemView.findViewById(R.id.progressBar)
//    private var horizontalErrorLoadMore: ImageUnify = itemView.findViewById(R.id.horizontalErrorLoadMore)
//    private var verticalErrorLoadMore: ImageUnify = itemView.findViewById(R.id.verticalErrorLoadMore)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        loadMoreViewModel = discoveryBaseViewModel as LoadMoreViewModel
        with(itemView.context) {
            if(this is DiscoveryActivity) {
                this.discoveryComponent.provideSubComponent()
                        .inject(loadMoreViewModel)
            }
        }
        setLoaderView()
    }

    private fun setLoaderView() {
        if(loadMoreViewModel.getViewOrientation()){
            // For horizontal product list

            if(loadMoreViewModel.getHorizontalProductFailState()){

            }else{

            }

            val layoutParams: ViewGroup.LayoutParams = parentLayout.layoutParams
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            parentLayout.layoutParams = layoutParams
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            loadMoreViewModel.getSyncPageLiveData().observe(lifecycleOwner, {
                (fragment as DiscoveryFragment).reSync()
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            loadMoreViewModel.getSyncPageLiveData().removeObservers(it)
        }
    }
}