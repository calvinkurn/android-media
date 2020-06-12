package com.tokopedia.buyerorder.detail.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.common.util.BuyerConsts
import com.tokopedia.buyerorder.detail.data.Items
import com.tokopedia.buyerorder.detail.view.adapter.BuyerListOfProductsBottomSheetAdapter
import com.tokopedia.buyerorder.detail.view.adapter.BuyerRequestCancelBottomsheetAdapter
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottomsheet_buyer_request_cancel.view.*
import kotlinx.android.synthetic.main.fragment_buyer_request_cancel.*
import java.io.Serializable
import javax.inject.Inject


/**
 * Created by fwidjaja on 08/06/20.
 */
class BuyerRequestCancelFragment: BaseDaggerFragment(), BuyerRequestCancelBottomsheetAdapter.ActionListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var buyerReqCancelBottomSheetAdapter: BuyerRequestCancelBottomsheetAdapter
    private lateinit var buyerListOfProductsBottomSheetAdapter: BuyerListOfProductsBottomSheetAdapter
    private var shopName = ""
    private var invoiceNum = ""
    private var listProductsSerializable : Serializable? = null
    private var listProduct = emptyList<Items>()

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): BuyerRequestCancelFragment {
            return BuyerRequestCancelFragment().apply {
                arguments = Bundle().apply {
                    putString(BuyerConsts.PARAM_SHOP_NAME, bundle.getString(BuyerConsts.PARAM_SHOP_NAME))
                    putString(BuyerConsts.PARAM_INVOICE, bundle.getString(BuyerConsts.PARAM_INVOICE))
                    putSerializable(BuyerConsts.PARAM_LIST_PRODUCT, bundle.getSerializable(BuyerConsts.PARAM_LIST_PRODUCT))
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
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_buyer_request_cancel, container, false)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buyerReqCancelBottomSheetAdapter = BuyerRequestCancelBottomsheetAdapter(this)
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
        tf_choose_reason?.setOnClickListener {
            showReasonBottomSheet()
        }
        tf_choose_reason?.textFieldInput?.setOnClickListener {
            showReasonBottomSheet()
        }
        tf_choose_reason?.textFieldIcon1?.setOnClickListener {
            showReasonBottomSheet()
        }
    }

    private fun showReasonBottomSheet() {
        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_buyer_request_cancel, null).apply {
            rv_cancel?.apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                adapter = buyerReqCancelBottomSheetAdapter
            }
        }

        val bottomSheet = BottomSheetUnify().apply {
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

    override fun onBottomSheetItemClick(key: String) {
        println("++ keclick nih")
    }
}