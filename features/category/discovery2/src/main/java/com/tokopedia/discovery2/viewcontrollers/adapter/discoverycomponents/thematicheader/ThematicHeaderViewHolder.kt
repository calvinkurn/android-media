package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.thematicheader

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifyprinciples.Typography

class ThematicHeaderViewHolder(itemView: View, private val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private var thematicHeaderViewModel: ThematicHeaderViewModel? = null
    private var lihatTitleTextView: Typography = itemView.findViewById(R.id.title)
    private var lihatSubTitleTextView: Typography = itemView.findViewById(R.id.subtitle)
    private var backgroundImageView: ImageView = itemView.findViewById(R.id.background_image)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        thematicHeaderViewModel = discoveryBaseViewModel as ThematicHeaderViewModel
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            thematicHeaderViewModel?.getComponentLiveData()?.observe(it, { componentItem ->
                componentItem.data?.firstOrNull()?.let { dataItem ->
                    setupView(dataItem)
                }
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            thematicHeaderViewModel?.getComponentLiveData()?.removeObservers(it)
        }
    }

    private fun setupView(dataItem: DataItem) {
        with(dataItem) {
            if (!title.isNullOrEmpty()) {
                lihatTitleTextView.text = title
            }
            if (!subtitle.isNullOrEmpty()) {
                lihatSubTitleTextView.text = subtitle
            }
            setupBackground(color, image)
        }
    }

    private fun setupBackground(color: String?, backgroundImageURL: String) {
        try {
            backgroundImageView.show()
            if (!color.isNullOrEmpty())
                backgroundImageView.setBackgroundColor(Color.parseColor(color))
            if (backgroundImageURL.isNotEmpty())
                backgroundImageView.loadImageWithoutPlaceholder(backgroundImageURL)
        } catch (e: Exception) {
            backgroundImageView.hide()
        }
    }
}
