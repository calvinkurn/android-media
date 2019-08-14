package com.tokopedia.product.detail.view.widget

import android.app.Dialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneAddedProductViewModel
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationViewModel
import com.tokopedia.product.detail.view.adapter.AddToCartDoneAdapter
import com.tokopedia.product.detail.view.adapter.AddToCartDoneTypeFactory

class AddToCartDoneBottomSheet: BottomSheets() {
    lateinit var rv: RecyclerView

    override fun getLayoutResourceId(): Int {
        return R.layout.add_to_cart_done_bottomsheet
    }

    override fun initView(view: View?) {
        view?.let{

            rv =it.findViewById(R.id.recycler_view_add_to_cart_top_ads)
        }

    }

    override fun setupDialog(dialog: Dialog?, style: Int) {
        super.setupDialog(dialog, style)
        val factory = AddToCartDoneTypeFactory()
        val atcDoneAdapter =  AddToCartDoneAdapter(factory)
        val linearLayoutManager = LinearLayoutManager(context)
        rv.layoutManager = linearLayoutManager
        rv.adapter = atcDoneAdapter
        val md1 = AddToCartDoneAddedProductViewModel(
                "pr",
                "rqwe"
        )
        atcDoneAdapter.addElement(md1)
        val md2 = AddToCartDoneRecommendationViewModel(
                "pr",
                "rqwe"
        )
        atcDoneAdapter.addElement(md2)
        val md3 = AddToCartDoneRecommendationViewModel(
                "pr",
                "rqwe"
        )
        atcDoneAdapter.addElement(md3)
        atcDoneAdapter.notifyDataSetChanged()
    }

    override fun state(): BottomSheetsState {
        return BottomSheetsState.FLEXIBLE
    }
}