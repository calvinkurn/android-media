package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardliststate

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.unifycomponents.UnifyButton

class ErrorLoadViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private lateinit var errorLoadViewModel: ErrorLoadViewModel
    private var errorReLoadState: EmptyStateUnify? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        errorLoadViewModel = discoveryBaseViewModel as ErrorLoadViewModel
        with(itemView.context) {
            if(this is DiscoveryActivity) {
                this.discoveryComponent.provideSubComponent()
                        .inject(errorLoadViewModel)
            }
        }
        init()
    }

    private fun init() {
        errorReLoadState = itemView.findViewById(R.id.viewEmptyState)
        errorReLoadState?.run {
            setTitle(context?.getString(R.string.discovery_product_empty_state_title).orEmpty())
            setDescription(context?.getString(R.string.discovery_product_empty_state_description).orEmpty())
            setImageDrawable(resources.getDrawable(com.tokopedia.globalerror.R.drawable.unify_globalerrors_500, null))
            setPrimaryCTAText(context?.getString(com.tokopedia.globalerror.R.string.error500Action).orEmpty())
            setPrimaryCTAClickListener {
                errorLoadViewModel.reloadComponentData()
            }
            emptyStateCTAID.buttonVariant = UnifyButton.Variant.GHOST
        }
    }


    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { lifecycle ->
            errorLoadViewModel.getSyncPageLiveData().observe(lifecycle, {
                if (it) (fragment as DiscoveryFragment).reSync()
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            errorLoadViewModel.getSyncPageLiveData().removeObservers(it)
        }
    }
}