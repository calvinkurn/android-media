package com.tokopedia.content.product.preview.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.FragmentManager
import com.tokopedia.content.common.report_content.ThreeDotsPage
import com.tokopedia.content.common.report_content.model.ContentMenuIdentifier
import com.tokopedia.content.common.report_content.model.ContentMenuItem
import com.tokopedia.content.product.preview.R
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewMenuStatus
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * @author by astidhiyaa on 01/12/23
 */
class MenuBottomSheet : BottomSheetUnify() {
    private var mListener: Listener? = null

    private var status: ReviewMenuStatus? = null

    private val menus: List<ContentMenuItem> by lazyThreadSafetyNone {
        buildList {
            add(
                ContentMenuItem(
                    name = R.string.product_preview_watch_menu,
                    iconUnify = IconUnify.VISIBILITY,
                    type = ContentMenuIdentifier.WatchMode
                )
            )
            if (status?.isReportable.orFalse()) {
                add(
                    ContentMenuItem(
                        name = R.string.product_preview_report_menu,
                        iconUnify = IconUnify.WARNING,
                        type = ContentMenuIdentifier.Report
                    )
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val composeView = ComposeView(requireContext()).apply {
            setContent {
                ThreeDotsPage(menuList = menus) { mListener?.onOptionClicked(it) }
            }
        }
        setChild(composeView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun setMenu(status: ReviewMenuStatus) {
        this.status = status
    }

    fun show(fg: FragmentManager) {
        if (isAdded) return
        super.show(fg, TAG)
    }

    override fun dismiss() {
        if (!isAdded) return
        super.dismiss()
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface Listener {
        fun onOptionClicked(menu: ContentMenuItem)
    }

    companion object {
        const val TAG = "MenuBottomSheet"

        fun get(fragmentManager: FragmentManager): MenuBottomSheet? {
            return fragmentManager.findFragmentByTag(TAG) as? MenuBottomSheet
        }

        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): MenuBottomSheet {
            return get(fragmentManager) ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                MenuBottomSheet::class.java.name
            ) as MenuBottomSheet
        }
    }
}
