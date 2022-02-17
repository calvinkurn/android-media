package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.anchortabs

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ScrollData
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

const val MIN_CAROUSEL_LIST_SIZE = 5
const val QUADRUPLE_LIST_SIZE = 4

class AnchorTabsItemViewHolder(itemView: View, val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private val anchorImage: ImageUnify = itemView.findViewById(R.id.anchor_icon)
    private val anchorText: Typography = itemView.findViewById(R.id.anchor_text)
    private val anchorParent: ConstraintLayout = itemView.findViewById(R.id.anchor_parent)
    private val anchorCard: CardUnify = itemView.findViewById(R.id.anchor_card)
    private var shouldShowIcons = true
    private var horizontalTab = false
    private var parentListSize = MIN_CAROUSEL_LIST_SIZE
    private lateinit var viewModel: AnchorTabsItemViewModel
    private val displayMetrics = Utils.getDisplayMetric(fragment.context)
    val observer = Observer<ScrollData> { data ->
        if(!horizontalTab) {
            shouldShowIcons = data.dy < 0
            setupImage()
        }
    }

    init {
        itemView.setOnClickListener {
            if (::viewModel.isInitialized) {
                val sectionID = viewModel.getSectionID()
                if (sectionID.isNotEmpty()) {
                    (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackAnchorTabClick(viewModel.components)
                    (fragment as DiscoveryFragment).scrollToSection(
                        sectionID,
                        viewModel.parentPosition()
                    )
                }
            }
        }
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as AnchorTabsItemViewModel
        setupCardType()
        setupView()
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().viewAnchorTabs(viewModel.components)
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            (fragment as DiscoveryFragment).getScrollLiveData().observe(it,observer)
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        (fragment as DiscoveryFragment).getScrollLiveData().removeObserver(observer)
    }

    private fun setupCardType() {
        val listSize = viewModel.components.parentListSize.toZeroIfNull()
        if(listSize <= 0 || listSize == parentListSize) return
        try {
            if (listSize > QUADRUPLE_LIST_SIZE) {
                if (parentListSize <= QUADRUPLE_LIST_SIZE) {
                    setupCarouselView()
                }
            } else if (listSize == QUADRUPLE_LIST_SIZE) {
                setupQuadrupleView(listSize)
            } else {
                setupTripleTypeView(listSize)
            }
        }catch (e:Exception){

        }
        parentListSize = listSize
    }

    private fun setupTripleTypeView(listSize: Int) {
        setupWidthOfItem(listSize)
        if (!horizontalTab) {
            applyHorizontalConstraints()
            horizontalTab = true
            setupImage()
        }
    }

    private fun applyHorizontalConstraints() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(anchorParent)
        constraintSet.connect(
            R.id.anchor_icon,
            ConstraintSet.START,
            R.id.anchor_parent,
            ConstraintSet.START
        )
        constraintSet.connect(
            R.id.anchor_icon,
            ConstraintSet.TOP,
            R.id.anchor_parent,
            ConstraintSet.TOP
        )
        constraintSet.connect(
            R.id.anchor_icon,
            ConstraintSet.END,
            R.id.anchor_text,
            ConstraintSet.START
        )
        constraintSet.connect(
            R.id.anchor_text,
            ConstraintSet.START,
            R.id.anchor_icon,
            ConstraintSet.END
        )
        constraintSet.connect(
            R.id.anchor_text,
            ConstraintSet.END,
            R.id.anchor_parent,
            ConstraintSet.END
        )
        constraintSet.connect(R.id.anchor_text,
            ConstraintSet.TOP,
            R.id.anchor_parent,
            ConstraintSet.TOP)
        constraintSet.applyTo(anchorParent)
    }

    private fun setupQuadrupleView(listSize: Int) {
        setupWidthOfItem(listSize)
    }

    private fun setupCarouselView() {
        val params = anchorParent.layoutParams
        params.width = itemView.context.resources.getDimensionPixelSize(R.dimen.anchor_tab_size)
        anchorParent.layoutParams = params
    }

    private fun setupWidthOfItem(listSize: Int){
        val widthOfItem = (displayMetrics.widthPixels - (2*(itemView.context.resources.getDimensionPixelSize(R.dimen.anchor_tab_padding))) - (2*listSize*(itemView.context.resources.getDimensionPixelSize(R.dimen.anchor_tab_card_padding))))/listSize
        if(widthOfItem > 0) {
            val params = anchorParent.layoutParams
            params.width = widthOfItem
            anchorParent.layoutParams = params
        }
    }

    private fun setupView() {
        anchorText.text = viewModel.getTitle()
        setupImage()
        if (viewModel.isSelected()) {
            anchorCard.cardType = CardUnify.TYPE_BORDER_ACTIVE
        } else {
            anchorCard.cardType = CardUnify.TYPE_SHADOW
        }
    }

    private fun setupImage(){
        if(horizontalTab || shouldShowIcons) {
            val imageUrl = viewModel.getImageUrl()
            if (imageUrl.isNotEmpty()) {
                anchorImage.show()
                anchorImage.loadImage(imageUrl)
            } else {
                anchorImage.hide()
            }
        }else{
            anchorImage.hide()
        }
    }

}