package com.tokopedia.product_bundle.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.product_bundle.fragment.EntrypointFragment
import com.tokopedia.product_service_widget.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class ProductBundleBottomSheet : BottomSheetUnify(){
    companion object {
        private val TAG = ProductBundleBottomSheet::class.simpleName

        fun newInstance(): ProductBundleBottomSheet {
            return ProductBundleBottomSheet()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottomsheet_product_bundle, container, false)
        initView(view)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val entryPoint = EntrypointFragment()
        val ft = childFragmentManager.beginTransaction()
        ft.replace(R.id.parent_view, entryPoint)
        ft.commit()
    }

    private fun initView(view: View) {
        setChild(view)
        setTitle(getString(R.string.product_bundle_page_title))
        isFullpage = true
        clearContentPadding = true
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }
}