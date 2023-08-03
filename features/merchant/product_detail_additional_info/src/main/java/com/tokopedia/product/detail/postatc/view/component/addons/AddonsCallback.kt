package com.tokopedia.product.detail.postatc.view.component.addons

import android.view.animation.AccelerateDecelerateInterpolator
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.addon.presentation.uimodel.AddOnGroupUIModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.postatc.base.PostAtcBottomSheetDelegate
import com.tokopedia.product.detail.postatc.view.PostAtcBottomSheet
import com.tokopedia.unifycomponents.Toaster

interface AddonsCallback {
    fun onLoadingSaveAddons()
    fun onSuccessSaveAddons(itemCount: Int)
    fun onFailedSaveAddons(message: String)
    fun onClickAddonsItem(indexChild: Int, addonsData: AddOnGroupUIModel)
    fun onClickAddonsInfo(indexChild: Int, addonsData: AddOnGroupUIModel)
    fun onImpressAddonsItem(indexChild: Int, addonsData: AddOnGroupUIModel)
}

class AddonsCallbackImpl(
    fragment: PostAtcBottomSheet
) : AddonsCallback, PostAtcBottomSheetDelegate by fragment {

    private val transition: AutoTransition by lazy {
        AutoTransition().apply {
            duration = 150
            interpolator = AccelerateDecelerateInterpolator()
        }
    }

    private var latestInfo = ""
    override fun onLoadingSaveAddons() {
        footer?.apply {
            val context = getContext() ?: return
            val loadingText = context.getString(R.string.pdp_post_atc_footer_info_loading)
            postAtcFooterInfo.text = loadingText
            TransitionManager.beginDelayedTransition(root, transition)
            postAtcFooterInfo.show()
        }
    }

    override fun onSuccessSaveAddons(itemCount: Int) {
        footer?.apply {
            if (itemCount == 0) {
                latestInfo = ""
                TransitionManager.beginDelayedTransition(root, transition)
                postAtcFooterInfo.hide()
            } else {
                val context = getContext() ?: return
                context.getString(
                    R.string.pdp_post_atc_footer_info_added,
                    itemCount.toString()
                ).let {
                    latestInfo = it
                    postAtcFooterInfo.text = it
                }
                postAtcFooterInfo.show()
            }
        }
    }

    override fun onFailedSaveAddons(message: String) {
        val latestInfo = latestInfo
        footer?.postAtcFooterInfo?.showIfWithBlock(latestInfo.isNotEmpty()) {
            text = latestInfo
        }
        val view = binding?.root?.rootView ?: return
        Toaster.build(view, message, Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
    }

    override fun onClickAddonsItem(
        indexChild: Int,
        addonsData: AddOnGroupUIModel
    ) {
        val addonsChild = addonsData.addon.getOrNull(indexChild) ?: return
        val item = AddonsTracker.AddonsItem(
            isChecked = addonsChild.isSelected,
            subtitle = addonsChild.name,
            position = indexChild,
            title = addonsData.title
        )

        AddonsTracking.onClickAddonsItem(
            info = viewModel.postAtcInfo,
            item = item,
            userId = userSession.userId,
            trackingQueue = trackingQueue
        )
    }

    override fun onImpressAddonsItem(
        indexChild: Int,
        addonsData: AddOnGroupUIModel
    ) {
        val addonsChild = addonsData.addon.getOrNull(indexChild) ?: return
        val item = AddonsTracker.AddonsItem(
            isChecked = addonsChild.isSelected,
            subtitle = addonsChild.name,
            position = indexChild,
            title = addonsData.title
        )

        AddonsTracking.onImpressAddonsItem(
            info = viewModel.postAtcInfo,
            item = item,
            userId = userSession.userId,
            trackingQueue = trackingQueue
        )
    }

    override fun onClickAddonsInfo(
        indexChild: Int,
        addonsData: AddOnGroupUIModel
    ) {
        val addonsChild = addonsData.addon.getOrNull(indexChild) ?: return
        val item = AddonsTracker.AddonsItem(
            isChecked = addonsChild.isSelected,
            subtitle = addonsChild.name,
            position = indexChild,
            title = addonsData.title
        )

        AddonsTracking.onClickAddonsInfo(
            info = viewModel.postAtcInfo,
            item = item,
            userId = userSession.userId,
            trackingQueue = trackingQueue
        )
    }
}
