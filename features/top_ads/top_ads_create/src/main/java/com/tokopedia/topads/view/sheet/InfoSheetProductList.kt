package com.tokopedia.topads.view.sheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.create.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.topads_create_fragment_product_sheet_info.*
import kotlinx.android.synthetic.main.topads_create_fragment_product_sheet_info.view.*

/**
 * Author errysuprayogi on 07,May,2019
 */
class InfoSheetProductList : BottomSheetUnify() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var childView = View.inflate(context, R.layout.topads_create_fragment_product_sheet_info, null)
        setChild(childView)
        setTitle(getString(R.string.tip_memilih_produk))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.imageView5.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_create_ic_checklist))
        view.imageView6.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_create_ic_checklist))
    }

    companion object {

        fun newInstance(): InfoSheetProductList {
            return InfoSheetProductList()
        }
    }
}
