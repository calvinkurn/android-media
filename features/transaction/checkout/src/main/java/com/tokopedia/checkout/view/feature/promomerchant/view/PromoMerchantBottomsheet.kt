package com.tokopedia.checkout.view.feature.promomerchant.view

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.promomerchant.di.PromoMerchantModule
import com.tokopedia.design.component.BottomSheets

class PromoMerchantBottomsheet : BottomSheets(), PromoMerchantContract.View, PromoMerchantAdapterListener {
    private var rvPromo: RecyclerView? = null

    // @Inject
    // internal var promoMerchantAdapter: PromoMerchantAdapter? = null

    companion object {
        @JvmStatic
        fun newInstance(): PromoMerchantBottomsheet {
            val promoMerchantBottomsheet = PromoMerchantBottomsheet()
            return promoMerchantBottomsheet
        }
    }

    private fun initializeInjector() {
        /*val component = DaggerPromoMerchantComponent.builder()
                .promoMerchantModule(PromoMerchantModule())
                .build()

        component.inject(this)*/
    }

    override fun onPromoMerchantShown() {
        println("++ PROMO MERCHANT IS SHOWN!")
        this.showsDialog = true
        updateHeight()
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_promo_merchant_choice
    }

    override fun initView(view: View) {
        rvPromo = view.findViewById<RecyclerView>(R.id.rv_promo)
        // initializeInjector()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false)
        rvPromo?.setLayoutManager(linearLayoutManager)
        // rvPromo?.setAdapter(promoMerchantAdapter)
    }

    /*override val activity: Activity
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
*/
    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showErrorPage(message: String) {
    }

    override fun showData() {
    }

    override fun stopTrace() {
    }
}