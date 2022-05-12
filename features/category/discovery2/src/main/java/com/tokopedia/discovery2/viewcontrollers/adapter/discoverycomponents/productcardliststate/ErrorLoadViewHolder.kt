package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardliststate

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.UnifyButton

class ErrorLoadViewHolder(itemView: View, private val fragment: Fragment) :
        AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private lateinit var errorLoadViewModel: ErrorLoadViewModel
    private var errorReLoadState: EmptyStateUnify = itemView.findViewById(R.id.viewEmptyState)
    private var progressLoader: LoaderUnify = itemView.findViewById(R.id.progressLoader)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        errorLoadViewModel = discoveryBaseViewModel as ErrorLoadViewModel
        with(itemView.context) {
            if (this is DiscoveryActivity) {
                this.discoveryComponent.provideSubComponent()
                        .inject(errorLoadViewModel)
            }
        }
        init()
    }

    private fun init() {
        errorReLoadState.visibility = View.VISIBLE
        progressLoader.visibility = View.GONE

        errorReLoadState.run {
            setTitle(context?.getString(R.string.discovery_product_empty_state_title).orEmpty())
            setDescription(context?.getString(R.string.discovery_product_empty_state_description).orEmpty())
            getAppCompatDrawable(
                context,
                com.tokopedia.globalerror.R.drawable.unify_globalerrors_500
            )?.let {
                setImageDrawable(it)
            }
            setPrimaryCTAText(context?.getString(R.string.discovery_error_500_action).orEmpty())
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
            errorLoadViewModel.getShowLoaderStatus().observe(lifecycleOwner, {
                if (it) {
                    errorReLoadState.visibility = View.GONE
                    progressLoader.visibility = View.VISIBLE
                } else {
                    errorReLoadState.visibility = View.VISIBLE
                    progressLoader.visibility = View.GONE
                }
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            errorLoadViewModel.getSyncPageLiveData().removeObservers(it)
            errorLoadViewModel.getShowLoaderStatus().removeObservers(it)
        }
    }

    private fun getAppCompatDrawable(context: Context?, resID: Int): Drawable? {
        return try {
            context?.let {
                AppCompatResources.getDrawable(
                    context,
                    resID
                )
            }
        } catch (e: Exception) {
            null
        }
    }
}