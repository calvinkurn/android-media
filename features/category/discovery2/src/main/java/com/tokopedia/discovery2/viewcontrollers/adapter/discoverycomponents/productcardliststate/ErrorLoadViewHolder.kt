package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardliststate

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.provider.Settings
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.UnifyButton

class ErrorLoadViewHolder(itemView: View, private val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private var errorLoadViewModel: ErrorLoadViewModel? = null
    private var errorReLoadState: EmptyStateUnify = itemView.findViewById(R.id.viewEmptyState)
    private var progressLoader: LoaderUnify = itemView.findViewById(R.id.progressLoader)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        errorLoadViewModel = discoveryBaseViewModel as ErrorLoadViewModel
        errorLoadViewModel?.let {
            getSubComponent().inject(it)
        }
        init()
    }

    private fun init() {
        errorReLoadState.visibility = View.VISIBLE
        progressLoader.visibility = View.GONE
        when (errorLoadViewModel?.components?.name) {
            ComponentNames.ProductListNetworkErrorLoad.componentName ->
                setupNetworkErrorView()
            else ->
                setupDefaultErrorView()
        }
    }

    private fun setupNetworkErrorView() {
        errorReLoadState.run {
            setTitle(context?.getString(R.string.discovery_product_network_state_title).orEmpty())
            setDescription(
                context?.getString(R.string.discovery_product_network_state_description).orEmpty()
            )
            getAppCompatDrawable(
                context,
                com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection
            )?.let {
                setImageDrawable(it)
            }
            setPrimaryCTAText(context?.getString(R.string.discovery_error_500_action).orEmpty())
            setPrimaryCTAClickListener {
                errorLoadViewModel?.reloadComponentData()
            }
            setSecondaryCTAText(context?.getString(R.string.discovery_open_network_setting).orEmpty())
            setSecondaryCTAClickListener {
                openNetworkSettings()
            }
        }
    }

    private fun openNetworkSettings() {
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
        try {
            fragment.startActivity(intent)
        } catch (_: Exception) {
        }
    }

    private fun setupDefaultErrorView() {
        errorReLoadState.run {
            setTitle(context?.getString(R.string.discovery_product_empty_state_title).orEmpty())
            if (errorLoadViewModel?.components?.parentComponentName == ComponentNames.MerchantVoucherList.componentName) {
                setDescription(
                    context?.getString(R.string.discovery_mvc_list_empty_state_description).orEmpty()
                )
            } else {
                setDescription(
                    context?.getString(R.string.discovery_product_empty_state_description).orEmpty()
                )
            }
            getAppCompatDrawable(
                context,
                com.tokopedia.globalerror.R.drawable.unify_globalerrors_500
            )?.let {
                setImageDrawable(it)
            }
            setPrimaryCTAText(context?.getString(R.string.discovery_error_500_action).orEmpty())
            setPrimaryCTAClickListener {
                errorLoadViewModel?.reloadComponentData()
            }
            emptyStateCTAID.buttonVariant = UnifyButton.Variant.GHOST
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { lifecycle ->
            errorLoadViewModel?.getSyncPageLiveData()?.observe(lifecycle) {
                if (it) (fragment as DiscoveryFragment).reSync()
            }
            errorLoadViewModel?.getShowLoaderStatus()?.observe(lifecycleOwner) {
                if (it) {
                    errorReLoadState.visibility = View.GONE
                    progressLoader.visibility = View.VISIBLE
                } else {
                    errorReLoadState.visibility = View.VISIBLE
                    progressLoader.visibility = View.GONE
                }
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            errorLoadViewModel?.getSyncPageLiveData()?.removeObservers(it)
            errorLoadViewModel?.getShowLoaderStatus()?.removeObservers(it)
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
