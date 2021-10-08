package com.tokopedia.shop.home.view.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.adapter.ShopHomeFlashSaleTncAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify

class FlashSaleTncBottomSheet : BottomSheetUnify() {

    private var flashSaleTncView: RecyclerView? = null
    private var flashSaleTncAdapter: ShopHomeFlashSaleTncAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setChild(inflater.inflate(R.layout.fragment_shop_campaign_tnc_bottom_sheet, container, false))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheetTitle.text = context?.getString(R.string.shop_page_label_purchase_tnc)
        initView(view)
        flashSaleTncAdapter?.setTncDescriptions(getFlashSaleTermsAndConditions(context))
    }

    private fun initView(rootView: View) {
        flashSaleTncView = rootView.findViewById(R.id.rv_flash_sale_tnc)
        flashSaleTncView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        flashSaleTncAdapter = ShopHomeFlashSaleTncAdapter()
        flashSaleTncView?.adapter = flashSaleTncAdapter
    }

    private fun getFlashSaleTermsAndConditions(context: Context?): List<String> {
        val tncDescriptions : MutableList<String> = mutableListOf()
        context?.run {
            tncDescriptions.add(context.getString(R.string.shop_page_flash_sale_tnc_number_one))
            tncDescriptions.add(context.getString(R.string.shop_page_flash_sale_tnc_number_two))
            tncDescriptions.add(context.getString(R.string.shop_page_flash_sale_tnc_number_three))
            tncDescriptions.add(context.getString(R.string.shop_page_flash_sale_tnc_number_four))
            tncDescriptions.add(context.getString(R.string.shop_page_flash_sale_tnc_number_five))
        }
        return tncDescriptions
    }
}