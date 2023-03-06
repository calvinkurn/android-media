package com.tokopedia.digital_checkout.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.DimenRes
import androidx.fragment.app.FragmentManager
import com.tokopedia.digital_checkout.R
import com.tokopedia.digital_checkout.presentation.widget.DigitalPlusMoreInfoItemWidget
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlin.math.min

class DigitalPlusMoreInfoBottomSheet : BottomSheetUnify() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    private fun initView() {
        isFullpage = false
        isDragable = false
        showCloseIcon = true
        clearContentPadding = true

        context?.let {
            val linearLayout = LinearLayout(it)
            linearLayout.orientation = LinearLayout.VERTICAL
            linearLayout.setPadding(
                getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
                Int.ZERO,
                getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
                getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_24)
            )

            val moreInfoTitles = it.resources?.getStringArray(R.array.subscription_plus_more_info_bottomsheet_description_title) ?: emptyArray()
            val moreInfoDescs = it.resources?.getStringArray(R.array.subscription_plus_more_info_bottomsheet_description_desc) ?: emptyArray()

            val indexesCount = min(moreInfoTitles.size, moreInfoDescs.size)
            for (idx in Int.ZERO until indexesCount) {
                val moreInfoItemWidget = DigitalPlusMoreInfoItemWidget(it).apply {
                    setContent(idx + Int.ONE, moreInfoTitles[idx], moreInfoDescs[idx])
                    setPadding(
                        Int.ZERO,
                        getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
                        Int.ZERO,
                        Int.ZERO
                    )
                }
                linearLayout.addView(moreInfoItemWidget)
            }

            setTitle(getString(R.string.subscription_plus_more_info_bottomsheet_title))
            setChild(linearLayout)
            setCloseClickListener { dismiss() }
        }
    }

    fun show(manager: FragmentManager) {
        show(manager, TAG)
    }

    private fun getDimensionPixelSize(@DimenRes id: Int): Int {
        return context?.resources?.getDimensionPixelSize(id) ?: Int.ZERO
    }

    companion object {
        private const val TAG = "PLUS_MORE_INFO_BOTTOM_SHEET"
    }
}
