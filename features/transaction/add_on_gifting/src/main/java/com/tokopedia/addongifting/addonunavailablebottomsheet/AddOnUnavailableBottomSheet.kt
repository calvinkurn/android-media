package com.tokopedia.addongifting.addonunavailablebottomsheet

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.addongifting.R
import com.tokopedia.addongifting.databinding.LayoutAddOnUnavailableBottomSheetBinding
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnProductData
import com.tokopedia.unifycomponents.BottomSheetUnify

class AddOnUnavailableBottomSheet : BottomSheetUnify() {

    private var adapter: AddOnUnavailableAdapter? = null
    private var viewBinding: LayoutAddOnUnavailableBottomSheetBinding? = null

    internal var addOnProductData: AddOnProductData = AddOnProductData()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewBinding = initializeView()
        this.viewBinding = viewBinding
        renderData()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initializeView(): LayoutAddOnUnavailableBottomSheetBinding {
        val viewBinding = LayoutAddOnUnavailableBottomSheetBinding.inflate(LayoutInflater.from(context))
        initializeBottomSheet(viewBinding)
        initializeRecyclerView(viewBinding)
        return viewBinding
    }

    private fun initializeBottomSheet(viewBinding: LayoutAddOnUnavailableBottomSheetBinding) {
        var bottomSheetTitle = addOnProductData.bottomSheetTitle
        if (bottomSheetTitle.isEmpty()) {
            bottomSheetTitle = getString(R.string.add_on_bottomsheet_title)
        }
        setTitle(bottomSheetTitle)
        showCloseIcon = true
        showHeader = true
        isDragable = true
        isHideable = true
        clearContentPadding = true
        customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / 2
        setChild(viewBinding.root)
    }

    private fun initializeRecyclerView(viewBinding: LayoutAddOnUnavailableBottomSheetBinding) {
        adapter = AddOnUnavailableAdapter()
        viewBinding.rvUnavailableProduct.adapter = adapter
        viewBinding.rvUnavailableProduct.layoutManager = LinearLayoutManager(viewBinding.root.context, LinearLayoutManager.VERTICAL, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun renderData() {
        adapter?.products = addOnProductData.unavailableBottomSheetData.unavailableProducts.map {
            AddOnUnavailableProductUiModel().apply {
                productName = it.productName
                productImageUrl = it.productImageUrl
            }
        }

        // View holder state is static, so it's ok to use notifyDataSetChanged
        adapter?.notifyDataSetChanged()

        viewBinding?.labelDescription?.text = MethodChecker.fromHtml(addOnProductData.unavailableBottomSheetData.description)
        viewBinding?.tickerInformation?.setHtmlDescription(addOnProductData.unavailableBottomSheetData.tickerMessage)
    }

    override fun onDismiss(dialog: DialogInterface) {
        viewBinding = null
        activity?.finish()
        activity?.overridePendingTransition(android.R.anim.fade_in, R.anim.add_on_selection_push_down)
        super.onDismiss(dialog)
    }

}
