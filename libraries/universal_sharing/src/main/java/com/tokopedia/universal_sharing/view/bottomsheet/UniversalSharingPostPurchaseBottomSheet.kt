package com.tokopedia.universal_sharing.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.view.bottomsheet.adapter.UniversalSharingPostPurchaseAdapter
import com.tokopedia.universal_sharing.view.bottomsheet.listener.UniversalSharingPostPurchaseBottomSheetListener
import com.tokopedia.universal_sharing.view.bottomsheet.typefactory.UniversalSharingTypeFactoryImpl
import com.tokopedia.universal_sharing.view.model.UniversalSharingPostPurchaseProductUiModel
import com.tokopedia.universal_sharing.view.model.UniversalSharingPostPurchaseShopTitleUiModel

class UniversalSharingPostPurchaseBottomSheet :
    BottomSheetUnify(),
    UniversalSharingPostPurchaseBottomSheetListener {

    private var loaderLayout: ConstraintLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheetConfig()
        setBottomSheetChildView()
    }

    private fun setupBottomSheetConfig() {
        showHeader = true // show title
        showCloseIcon = true // show close button
        clearContentPadding = true // remove default margin
        isDragable = false // should be not draggable
    }

    private fun setBottomSheetChildView() {
        val view = LayoutInflater.from(context).inflate(
            R.layout.universal_sharing_post_purchase_bottomsheet,
            null,
            false
        )
        loaderLayout = view.findViewById(R.id.universal_sharing_layout_loading)
        setChild(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomSheetTitle()
        setRecyclerView(view)
    }

    private fun setBottomSheetTitle() {
        setTitle(getString(R.string.universal_sharing_post_purchase_bottomsheet_title))
    }

    private fun setRecyclerView(view: View) {
        val typeFactory = UniversalSharingTypeFactoryImpl(this)
        val adapter = UniversalSharingPostPurchaseAdapter(typeFactory)
        val recyclerView: RecyclerView? = view.findViewById(R.id.universal_sharing_post_purchase_rv)
        recyclerView?.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(context)
            this.setHasFixedSize(true)
        }
        adapter.addElement(
            UniversalSharingPostPurchaseShopTitleUiModel(
                iconUrl = "https://images.tokopedia.net/img/official_store/badge_os.png",
                name = "Xiaomi Official Store"
            )
        )
        for (i in 1..20) {
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
    }

    override fun onClickShare(productId: String) {
        if (productId.isNotBlank()) {
            loaderLayout?.show()
        } else {
            // todo: Handle
        }
    }
}
