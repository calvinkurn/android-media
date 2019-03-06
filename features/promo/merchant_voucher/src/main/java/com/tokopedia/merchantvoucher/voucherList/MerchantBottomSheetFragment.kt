package com.tokopedia.merchantvoucher.voucherList

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.ToasterError
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.di.DaggerMerchantVoucherComponent
import com.tokopedia.merchantvoucher.common.gql.data.MessageTitleErrorException
import com.tokopedia.merchantvoucher.common.gql.data.UseMerchantVoucherQueryResult
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherList.presenter.MerchantVoucherListPresenter
import com.tokopedia.merchantvoucher.voucherList.presenter.MerchantVoucherListView
import com.tokopedia.merchantvoucher.voucherList.widget.MerchantVoucherListWidget
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import com.tokopedia.shop.common.di.ShopCommonModule
import javax.inject.Inject

/**
 * Created by fwidjaja on 03/03/19.
 */

open class MerchantBottomSheetFragment : BottomSheetDialogFragment(), MerchantVoucherListView {
    lateinit var voucherShopId: String
    private var mTitle: String? = null
    private var merchantVoucherListWidget: MerchantVoucherListWidget? = null
    @Suppress("DEPRECATION")
    private var loadingUseMerchantVoucher: ProgressDialog? = null

    @Inject
    lateinit var merchantVoucherListPresenter: MerchantVoucherListPresenter

    companion object {
        @JvmStatic
        fun newInstance(shopId: String): MerchantBottomSheetFragment {
            return MerchantBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MerchantVoucherListFragment.EXTRA_SHOP_ID, shopId)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        voucherShopId = arguments!!.getString(MerchantVoucherListActivity.SHOP_ID)
        super.onCreate(savedInstanceState)
        if (activity != null) {
            initInjector()
            mTitle = activity!!.getString(R.string.merchant_bottomsheet_title)
        }
    }

    fun initInjector() {
        activity?.run {
            DaggerMerchantVoucherComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .shopCommonModule(ShopCommonModule())
                    .build()
                    .inject(this@MerchantBottomSheetFragment)
            merchantVoucherListPresenter.attachView(this@MerchantBottomSheetFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        merchantVoucherListPresenter.clearCache()
        loadPromo()
    }

    fun loadPromo() {
        // hardcode shopId & numVoucher
        merchantVoucherListPresenter.getVoucherList(voucherShopId, 1)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.widget_bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val container = view.findViewById<FrameLayout>(R.id.bottomsheet_container)
        View.inflate(context, R.layout.bottomsheet_merchant_voucher, container)

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
            return
        }
        merchantVoucherListWidget?.setData(merchantVoucherViewModelList)
    }

    override fun onErrorGetMerchantVoucherList(e: Throwable) {
        merchantVoucherListWidget?.setData(null)
    }

    private fun hideUseMerchantVoucherLoading() {
        if (loadingUseMerchantVoucher != null) {
            loadingUseMerchantVoucher!!.dismiss()
        }
    }
}