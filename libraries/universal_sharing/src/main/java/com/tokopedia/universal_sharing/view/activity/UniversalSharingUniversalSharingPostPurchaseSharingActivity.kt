package com.tokopedia.universal_sharing.view.activity

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.hideKeyboard
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.view.bottomsheet.adapter.UniversalSharingPostPurchaseAdapter
import com.tokopedia.universal_sharing.view.bottomsheet.listener.UniversalSharingPostPurchaseBottomSheetListener
import com.tokopedia.universal_sharing.view.bottomsheet.typefactory.UniversalSharingTypeFactoryImpl
import com.tokopedia.universal_sharing.view.model.UniversalSharingPostPurchaseProductUiModel
import com.tokopedia.universal_sharing.view.model.UniversalSharingPostPurchaseShopTitleUiModel

class UniversalSharingUniversalSharingPostPurchaseSharingActivity:
    BaseActivity(),
    UniversalSharingPostPurchaseBottomSheetListener {

    private var bottomSheet: BottomSheetUnify? = null
    private var loaderLayout: ConstraintLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBottomSheet()
        showBottomSheet()
    }

    private fun setBottomSheet() {
        bottomSheet = BottomSheetUnify()
        setupBottomSheet()
    }

    private fun showBottomSheet() {
        currentFocus?.hideKeyboard()
        bottomSheet?.show(
            supportFragmentManager, ::UniversalSharingUniversalSharingPostPurchaseSharingActivity.name)
    }

    private fun setupBottomSheet() {
        setupBottomSheetConfig()
        bottomSheet?.setTitle(
            getString(R.string.universal_sharing_post_purchase_bottomsheet_title))
        setBottomSheetChildView()
        setBottomSheetListener()
    }

    private fun setupBottomSheetConfig() {
        bottomSheet?.apply {
            showHeader = true // show title
            showCloseIcon = true // show close button
            clearContentPadding = true // remove default margin
        }
    }

    private fun setBottomSheetChildView() {
        val view = View.inflate(
            this,
            R.layout.universal_sharing_post_purchase_bottomsheet,
            null
        )
        loaderLayout = view.findViewById(R.id.universal_sharing_layout_loading)
        setRecyclerView(view)
        bottomSheet?.setChild(view)
    }

    private fun setBottomSheetListener() {
        bottomSheet?.setCloseClickListener {
            finish()
        }
    }

    private fun setRecyclerView(view: View) {
        val typeFactory = UniversalSharingTypeFactoryImpl(this)
        val adapter = UniversalSharingPostPurchaseAdapter(typeFactory)
        val recyclerView: RecyclerView? = view.findViewById(R.id.universal_sharing_post_purchase_rv)
        recyclerView?.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(context)
        }
        adapter.addElement(
            UniversalSharingPostPurchaseShopTitleUiModel(
                iconUrl = "https://images.tokopedia.net/img/official_store/badge_os.png",
                name = "Xiaomi Official Store"
            )
        )
        adapter.addElement(
            UniversalSharingPostPurchaseProductUiModel(
                productId = "123123",
                name = "Xiaomi 12 Pro 256 GB - Pink",
                price = "300.000",
                imageUrl = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/6/29/e6e99027-a32b-4cc9-b9ae-0eea6ef2e6f3.png",
                isLoading = false
            )
        )
    }

    override fun onClickShare(productId: String) {
        if (productId.isNotBlank()) {
            loaderLayout?.show()
        } else {
            // todo: Handle
        }
    }
}
