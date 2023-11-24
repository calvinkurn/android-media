package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.viewallcard

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.databinding.DiscoveryCarouselViewAllCardBinding
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.viewallcard.ViewAllCard

class ViewAllCarouselViewHolder(
    itemView: View,
    val fragment: Fragment
) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private var viewModel: ViewAllCarouselViewModel? = null

    private var binding: DiscoveryCarouselViewAllCardBinding? by viewBinding()

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as ViewAllCarouselViewModel

        viewModel?.let {
            getSubComponent().inject(it)
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)

        viewModel?.getData()?.observe(fragment.viewLifecycleOwner) { data ->
            renderCard(data)
        }
    }

    private fun renderCard(data: DataItem) {
        binding?.viewAllCarouselCard?.apply {
            isTitleNumberStyle = true
            title = data.title.orEmpty()
            mode = ViewAllCard.MODE_NORMAL
            setCta(data.action.orEmpty())

            handleClickListener(data.applinks)
        }
    }

    private fun ViewAllCard.handleClickListener(appLinks: String?) {
        setOnClickListener {
            RouteManager.route(fragment.context, appLinks)
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        viewModel?.getData()?.removeObservers(fragment.viewLifecycleOwner)
        super.removeObservers(lifecycleOwner)
    }
}
