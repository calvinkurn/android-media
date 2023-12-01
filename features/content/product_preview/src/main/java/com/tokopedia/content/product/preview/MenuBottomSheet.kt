package com.tokopedia.content.product.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.FragmentManager
import com.tokopedia.content.common.report_content.ThreeDotsPage
import com.tokopedia.content.common.report_content.model.ContentMenuIdentifier
import com.tokopedia.content.common.report_content.model.ContentMenuItem
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * @author by astidhiyaa on 01/12/23
 */
class MenuBottomSheet : BottomSheetUnify() {
    private var mListener: Listener? = null

    //TODO() move to mapper
    private val menus: List<ContentMenuItem> by lazy {
        buildList {
            add(
                ContentMenuItem(
                    name = R.string.product_preview_watch_menu,
                    iconUnify = IconUnify.VISIBILITY,
                    type = ContentMenuIdentifier.WatchMode
                )
            )
            add(
                ContentMenuItem(
                    name = R.string.product_preview_report_menu,
                    iconUnify = IconUnify.WARNING,
                    type = ContentMenuIdentifier.Report
                )
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val composeView = ComposeView(requireContext()).apply {
            setContent {
                ThreeDotsPage(menuList = menus) { mListener?.onMenuClicked(it) }
            }
        }
        setChild(composeView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun show(fg: FragmentManager) {
        if (isAdded) return
        super.show(fg, TAG)
    }

    override fun dismiss() {
        if (!isAdded) return
        super.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mListener = null
    }

    interface Listener {
        fun onMenuClicked(menu: ContentMenuItem)
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
