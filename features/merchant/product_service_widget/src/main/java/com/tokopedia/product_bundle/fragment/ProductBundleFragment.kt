package com.tokopedia.product_bundle.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.product_bundle.bottomsheet.ProductBundleBottomSheet
import com.tokopedia.product_service_widget.R

class ProductBundleFragment: Fragment() {

    companion object {
        fun newInstance(): ProductBundleFragment =  ProductBundleFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_entrypoint, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProductBundleBottomSheet()
    }

    private fun showProductBundleBottomSheet() {
        val bottomSheet = ProductBundleBottomSheet.newInstance()
        bottomSheet.setOnDismissListener {
            activity?.finish()
        }
        bottomSheet.show(childFragmentManager)
    }
}