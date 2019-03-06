package com.tokopedia.checkout.view.feature.promostacking

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.checkout.R
import com.tokopedia.core.util.GlobalConfig
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.ToasterError
import com.tokopedia.merchantvoucher.common.gql.data.MessageTitleErrorException
import com.tokopedia.merchantvoucher.common.gql.data.UseMerchantVoucherQueryResult
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherList.presenter.MerchantVoucherListPresenter
import com.tokopedia.merchantvoucher.voucherList.presenter.MerchantVoucherListView
import com.tokopedia.merchantvoucher.voucherList.widget.MerchantVoucherListWidget
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import com.tokopedia.tkpdpdp.customview.PromoWidgetView
import kotlinx.android.synthetic.main.item_promo_merchant.*
import kotlinx.android.synthetic.main.widget_bottomsheet.*
import javax.inject.Inject

/**
 * Created by fwidjaja on 03/03/19.
 */

open class MerchantPromoBottomSheetFragment : BottomSheetDialogFragment(), MerchantVoucherListView {
    private var mTitle: String? = null
    private var merchantVoucherListWidget: MerchantVoucherListWidget? = null
    @Suppress("DEPRECATION")
    private var loadingUseMerchantVoucher: ProgressDialog? = null
    private var promoWidgetView: PromoWidgetView? = null

    @Inject
    lateinit var merchantVoucherListPresenter: MerchantVoucherListPresenter

    companion object {
        @JvmStatic
        fun newInstance(): MerchantPromoBottomSheetFragment {
            return MerchantPromoBottomSheetFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity != null) {
            mTitle = activity!!.getString(R.string.label_promo_merchant)
        }
    }

    override fun onResume() {
        super.onResume()
        merchantVoucherListPresenter.clearCache()
        loadPromo()
    }

    fun loadPromo() {
        // hardcode shopId & numVoucher
        merchantVoucherListPresenter.getVoucherList("3385304", 1)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.widget_bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val container = view.findViewById<FrameLayout>(R.id.bottomsheet_container)
        View.inflate(context, R.layout.item_promo_merchant, container)

        val textViewTitle = view.findViewById<TextView>(com.tokopedia.design.R.id.tv_title)
        textViewTitle.text = mTitle

        /*view.findViewById<View>(R.id.button_bottom_sheet_cod).setOnClickListener { view1 ->
            dismiss()
        }*/

        merchantVoucherListWidget = view.findViewById(R.id.merchantVoucherListWidget)

        val layoutTitle = view.findViewById<View>(com.tokopedia.design.R.id.layout_title)
        layoutTitle.setOnClickListener {
            dismiss()
        }
    }

    override fun onSuccessGetShopInfo(shopInfo: ShopInfo) {
        // no op
    }

    override fun onErrorGetShopInfo(e: Throwable) {
        // no op
    }

    override fun onSuccessUseVoucher(useMerchantVoucherQueryResult: UseMerchantVoucherQueryResult) {
        hideUseMerchantVoucherLoading()
        activity?.let {
            Dialog(it, Dialog.Type.PROMINANCE).apply {
                setTitle(useMerchantVoucherQueryResult.errorMessageTitle)
                setDesc(useMerchantVoucherQueryResult.errorMessage)
                setBtnOk(getString(com.tokopedia.merchantvoucher.R.string.label_close))
                setOnOkClickListener {
                    dismiss()
                }
                show()
            }

            merchantVoucherListPresenter.clearCache()
            // loadPromo()

            it.setResult(Activity.RESULT_OK)
        }
    }

    override fun onErrorUseVoucher(e: Throwable) {
        hideUseMerchantVoucherLoading()
        if (e is MessageTitleErrorException) {
            activity?.let {
                Dialog(it, Dialog.Type.PROMINANCE).apply {
                    setTitle(e.errorMessageTitle)
                    setDesc(e.message)
                    setBtnOk(getString(com.tokopedia.merchantvoucher.R.string.label_close))
                    setOnOkClickListener {
                        dismiss()
                    }
                    show()
                }
            }
        } else {
            activity?.let {
                ToasterError.showClose(it, ErrorHandler.getErrorMessage(it, e))
            }
        }
    }

    override fun onSuccessGetMerchantVoucherList(merchantVoucherViewModelList: ArrayList<MerchantVoucherViewModel>) {
        if (merchantVoucherViewModelList.size == 0) {
            merchantVoucherListWidget?.setData(null)
            promoWidgetView?.visibility = View.GONE
            promoContainer.visibility = View.GONE
            return
        }
        merchantVoucherListWidget?.setData(merchantVoucherViewModelList)
        promoWidgetView?.visibility = View.GONE
        promoContainer.visibility = View.VISIBLE
    }

    override fun onErrorGetMerchantVoucherList(e: Throwable) {
        merchantVoucherListWidget?.setData(null)
        promoWidgetView?.visibility = View.GONE
        promoContainer.visibility = View.GONE
    }

    private fun hideUseMerchantVoucherLoading() {
        if (loadingUseMerchantVoucher != null) {
            loadingUseMerchantVoucher!!.dismiss()
        }
    }
}