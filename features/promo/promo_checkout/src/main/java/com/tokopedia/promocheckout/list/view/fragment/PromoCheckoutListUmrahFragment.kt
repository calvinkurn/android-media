package com.tokopedia.promocheckout.list.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.tokopedia.promocheckout.common.util.EXTRA_PROMO_DATA
import com.tokopedia.promocheckout.common.util.mapToStatePromoCheckout
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.list.di.PromoCheckoutListComponent
import com.tokopedia.promocheckout.list.view.activity.PromoCheckoutListUmrahActivity.Companion.EXTRA_TOTAL_PRICE
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListContract
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListUmrahPresenter
import kotlinx.android.synthetic.main.fragment_promo_checkout_list.*
import javax.inject.Inject

open class PromoCheckoutListUmrahFragment : BasePromoCheckoutListFragment(), PromoCheckoutListContract.View {

    var totalPrice = 0

    @Inject
    lateinit var promoCheckoutListUmrahPresenter: PromoCheckoutListUmrahPresenter

    override var serviceId: String = ""

    override fun onPromoCodeUse(promoCode: String) {
        if (promoCode.isNotEmpty()) promoCheckoutListUmrahPresenter.checkPromo(promoCode, totalPrice)
    }

    override fun onSuccessCheckPromo(data: DataUiModel) {
        val intent = Intent()
        val promoData = PromoData(data.isCoupon, textInputCoupon.textFieldInput.text.toString(),
                data.message.text, data.titleDescription, state = data.message.state.mapToStatePromoCheckout())
        intent.putExtra(EXTRA_PROMO_DATA, promoData)

        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun loadData(page: Int) {
        hideLoading()
        showEmpty()
    }

    override fun onDestroyView() {
        promoCheckoutListUmrahPresenter.detachView()
        super.onDestroyView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        promoCode = arguments?.getString(EXTRA_PROMO_CODE) ?: ""
        totalPrice = arguments?.getInt(EXTRA_TOTAL_PRICE) ?: 0

        promoCheckoutListUmrahPresenter.attachView(this)
    }

    override fun initInjector() {
        getComponent(PromoCheckoutListComponent::class.java).inject(this)
    }

    companion object {
        fun createInstance(promoCode: String?, totalPrice: Int?): PromoCheckoutListUmrahFragment {
            val promoCheckoutListUmrahFragment = PromoCheckoutListUmrahFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_PROMO_CODE, promoCode ?: "")
            bundle.putInt(EXTRA_TOTAL_PRICE, totalPrice ?: 0)
            promoCheckoutListUmrahFragment.arguments = bundle
            return promoCheckoutListUmrahFragment
        }
    }
}