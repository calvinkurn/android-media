package com.tokopedia.product.detail.postatc.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.product.detail.databinding.PostAtcBottomSheetBinding
import com.tokopedia.product.detail.postatc.base.PostAtcAdapter
import com.tokopedia.product.detail.postatc.base.PostAtcLayoutManager
import com.tokopedia.product.detail.postatc.component.productinfo.ProductInfoUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify

class PostAtcBottomSheet : BottomSheetUnify() {

    companion object {

        const val TAG = "post_atc_bs"

        fun instance() = PostAtcBottomSheet()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        val binding = PostAtcBottomSheetBinding.inflate(inflater, container, false)
        initView(binding)
        setChild(binding.root)
    }

    private fun initView(binding: PostAtcBottomSheetBinding) = binding.apply {
        postAtcRv.layoutManager = PostAtcLayoutManager()

        val adapter = PostAtcAdapter()
        postAtcRv.adapter = adapter

        val productInfo = ProductInfoUiModel(
            "Produk berhasil ditambahkan",
            "Apple iPhone 13 128GB | 256GB | 512GB...",
            "",
            "Lihat Keranjang"
        )

        adapter.addItem(productInfo)
        adapter.addItem(productInfo)
        adapter.addItem(productInfo)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.finish()
    }

}
