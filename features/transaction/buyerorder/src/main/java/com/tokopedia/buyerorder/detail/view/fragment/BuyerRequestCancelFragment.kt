package com.tokopedia.buyerorder.detail.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.common.util.BuyerConsts
import com.tokopedia.buyerorder.detail.data.Items
import com.tokopedia.buyerorder.detail.data.getcancellationreason.BuyerGetCancellationReasonData
import com.tokopedia.buyerorder.detail.di.OrderDetailsComponent
import com.tokopedia.buyerorder.detail.view.adapter.BuyerListOfProductsBottomSheetAdapter
import com.tokopedia.buyerorder.detail.view.adapter.GetCancelReasonBottomSheetAdapter
import com.tokopedia.buyerorder.detail.view.adapter.GetCancelSubReasonBottomSheetAdapter
import com.tokopedia.buyerorder.detail.view.viewmodel.BuyerGetCancellationReasonViewModel
import com.tokopedia.buyerorder.list.common.OrderListContants
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.bottomsheet_buyer_request_cancel.view.*
import kotlinx.android.synthetic.main.fragment_buyer_request_cancel.*
import java.io.Serializable
import javax.inject.Inject


/**
 * Created by fwidjaja on 08/06/20.
 */
class BuyerRequestCancelFragment: BaseDaggerFragment(),
        GetCancelReasonBottomSheetAdapter.ActionListener, GetCancelSubReasonBottomSheetAdapter.ActionListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var reasonBottomSheetAdapter: GetCancelReasonBottomSheetAdapter
    private lateinit var subReasonBottomSheetAdapter: GetCancelSubReasonBottomSheetAdapter
    private lateinit var buyerListOfProductsBottomSheetAdapter: BuyerListOfProductsBottomSheetAdapter
    private var shopName = ""
    private var invoiceNum = ""
    private var orderId = ""
    private var uri = ""
    private var listProductsSerializable : Serializable? = null
    private var listProduct = emptyList<Items>()
    private var cancelReasonResponse = BuyerGetCancellationReasonData.Data.GetCancellationReason()
    private var bottomSheet = BottomSheetUnify()
    private var reasonCancel = ""
    private var reasonCode = -1

    private val buyerGetCancellationReasonViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[BuyerGetCancellationReasonViewModel::class.java]
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): BuyerRequestCancelFragment {
            return BuyerRequestCancelFragment().apply {
                arguments = Bundle().apply {
                    putString(BuyerConsts.PARAM_SHOP_NAME, bundle.getString(BuyerConsts.PARAM_SHOP_NAME))
                    putString(BuyerConsts.PARAM_INVOICE, bundle.getString(BuyerConsts.PARAM_INVOICE))
                    putSerializable(BuyerConsts.PARAM_LIST_PRODUCT, bundle.getSerializable(BuyerConsts.PARAM_LIST_PRODUCT))
                    putString(BuyerConsts.PARAM_ORDER_ID, bundle.getString(BuyerConsts.PARAM_ORDER_ID))
                    putString(BuyerConsts.PARAM_URI, bundle.getString(BuyerConsts.PARAM_URI))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            shopName = arguments?.getString(BuyerConsts.PARAM_SHOP_NAME).toString()
            invoiceNum = arguments?.getString(BuyerConsts.PARAM_INVOICE).toString()
            listProductsSerializable = arguments?.getSerializable(BuyerConsts.PARAM_LIST_PRODUCT)
            listProduct = listProductsSerializable as List<Items>
            orderId = arguments?.getString(BuyerConsts.PARAM_ORDER_ID).toString()
            uri = arguments?.getString(BuyerConsts.PARAM_URI).toString()
        }
        getCancelReasons()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_buyer_request_cancel, container, false)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(OrderDetailsComponent::class.java).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observingCancelReasons()

        reasonBottomSheetAdapter = GetCancelReasonBottomSheetAdapter(this)
        label_shop_name?.text = shopName
        label_invoice?.text = invoiceNum

        if (listProduct.isNotEmpty()) {
            label_product_name?.text = listProduct.first().title
            label_price?.text = listProduct.first().price
            iv_product?.loadImage(listProduct.first().imageUrl)

            if (listProduct.size > 1) {
                label_see_all_products?.visible()
                label_see_all_products?.setOnClickListener { showProductsBottomSheet() }
            } else {
                label_see_all_products?.gone()
            }
        }

        tf_choose_reason?.textFieldInput?.isFocusable = false
        tf_choose_reason?.textFieldInput?.isClickable = true

        setListeners()
    }

    private fun setListeners() {
        tf_choose_reason?.setOnClickListener {
            showReasonBottomSheet()
        }
        tf_choose_reason?.textFieldInput?.setOnClickListener {
            showReasonBottomSheet()
        }
        tf_choose_reason?.textFieldIcon1?.setOnClickListener {
            showReasonBottomSheet()
        }

        btn_req_cancel?.setOnClickListener {
            if (reasonCancel.isNotBlank() && reasonCode != -1) {
                submitResultReason()
            }
        }
    }

    private fun showReasonBottomSheet() {
        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_buyer_request_cancel, null).apply {
            rv_cancel?.apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                adapter = reasonBottomSheetAdapter
            }
        }

        bottomSheet = BottomSheetUnify().apply {
            setChild(viewBottomSheet)
            setTitle(BuyerConsts.TITLE_CANCEL_REASON_BOTTOMSHEET)
            showCloseIcon = true
            setCloseClickListener { dismiss() }
        }

        fragmentManager?.let { bottomSheet.show(it, getString(R.string.show_bottomsheet)) }
    }

    private fun showSubReasonBottomSheet() {
        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_buyer_request_cancel, null).apply {
            rv_cancel?.apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                adapter = subReasonBottomSheetAdapter
            }
        }

        bottomSheet = BottomSheetUnify().apply {
            setChild(viewBottomSheet)
            setTitle(BuyerConsts.TITLE_CANCEL_REASON_BOTTOMSHEET)
            showCloseIcon = true
            setCloseClickListener { dismiss() }
        }

        fragmentManager?.let { bottomSheet.show(it, getString(R.string.show_bottomsheet)) }
    }

    private fun showProductsBottomSheet() {
        buyerListOfProductsBottomSheetAdapter = BuyerListOfProductsBottomSheetAdapter().apply {
            listProducts = listProduct
            notifyDataSetChanged()
        }

        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_buyer_request_cancel, null).apply {
            rv_cancel?.apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                adapter = buyerListOfProductsBottomSheetAdapter
            }
        }

        val bottomSheet = BottomSheetUnify().apply {
            setChild(viewBottomSheet)
            setTitle(BuyerConsts.TITLE_LIST_OF_PRODUCT_BOTTOMSHEET)
            showCloseIcon = true
            setCloseClickListener { dismiss() }
        }

        fragmentManager?.let { bottomSheet.show(it, getString(R.string.show_bottomsheet)) }
    }

    private fun getCancelReasons() {
        val userSession = UserSession(context)
        buyerGetCancellationReasonViewModel.getCancelReasons(
                GraphqlHelper.loadRawString(resources, R.raw.get_cancel_reason), userSession.userId, orderId)
    }

    private fun observingCancelReasons() {
        buyerGetCancellationReasonViewModel.cancelReasonResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    val arrayListOfReason = arrayListOf<String>()
                    cancelReasonResponse = it.data.getCancellationReason
                    cancelReasonResponse.reasons.forEach { reasonItem ->
                        arrayListOfReason.add(reasonItem.title)
                    }
                    reasonBottomSheetAdapter = GetCancelReasonBottomSheetAdapter(this).apply {
                        listReason = arrayListOfReason
                        notifyDataSetChanged()
                    }
                }
                is Fail -> {
                    val toasterFail = Toaster
                    view?.let { v ->
                        toasterFail.make(v, getString(R.string.fail_cancellation), Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR, BuyerConsts.ACTION_OK)
                    }
                }
            }
        })
    }

    override fun onReasonClicked(reason: String) {
        bottomSheet.dismiss()
        tf_choose_reason?.textFieldInput?.setText(reason)
        reasonCancel += reason

        if (cancelReasonResponse.reasons.isNotEmpty()) {
            tf_choose_sub_reason?.visible()
            tf_choose_sub_reason?.textFieldInput?.setText("")

            cancelReasonResponse.reasons.forEach {
                if (it.title.equals(reason, true))  {
                    subReasonBottomSheetAdapter = GetCancelSubReasonBottomSheetAdapter(this).apply {
                        listSubReason = it.subReasons
                        notifyDataSetChanged()
                    }

                    tf_choose_sub_reason?.textFiedlLabelText?.text = it.question
                    tf_choose_sub_reason?.textFieldInput?.isFocusable = false
                    tf_choose_sub_reason?.textFieldInput?.isClickable = true
                    tf_choose_sub_reason?.setOnClickListener {
                        showSubReasonBottomSheet()
                    }
                    tf_choose_sub_reason?.textFieldInput?.setOnClickListener {
                        showSubReasonBottomSheet()
                    }
                    tf_choose_sub_reason?.textFieldIcon1?.setOnClickListener {
                        showSubReasonBottomSheet()
                    }
                }
            }
        }
    }

    override fun onSubReasonClicked(rCode: Int, reason: String) {
        bottomSheet.dismiss()
        tf_choose_sub_reason?.textFieldInput?.setText(reason)
        reasonCancel += " - $reason"
        reasonCode = rCode
    }

    private fun submitResultReason() {
        val intent = Intent()
        intent.putExtra(OrderListContants.REASON, reasonCancel)
        intent.putExtra(OrderListContants.REASON_CODE, reasonCode)
        intent.putExtra(MarketPlaceDetailFragment.ACTION_BUTTON_URL, uri)
        activity?.setResult(MarketPlaceDetailFragment.CANCEL_BUYER_REQUEST, intent)
        activity?.finish()
    }
}