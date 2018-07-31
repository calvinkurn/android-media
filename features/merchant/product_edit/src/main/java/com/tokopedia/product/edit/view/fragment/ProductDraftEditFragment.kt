package com.tokopedia.product.edit.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tkpd.library.ui.utilities.TkpdProgressDialog
import com.tkpd.library.utils.CommonUtils
import com.tokopedia.core.network.NetworkErrorHelper
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.di.component.DaggerProductDraftComponent
import com.tokopedia.product.edit.common.di.component.ProductComponent
import com.tokopedia.product.edit.common.model.edit.ProductViewModel
import com.tokopedia.product.edit.common.util.ProductStatus
import com.tokopedia.product.edit.di.module.ProductDraftModule
import com.tokopedia.product.edit.utils.convertToProductAddViewModel
import com.tokopedia.product.edit.view.model.ProductAddViewModel
import com.tokopedia.product.edit.view.presenter.ProductDraftPresenterImpl
import com.tokopedia.product.edit.view.presenter.ProductDraftView

open class ProductDraftEditFragment : BaseProductAddEditFragment<ProductDraftPresenterImpl, ProductDraftView>(), ProductDraftView{

    override var statusUpload = ProductStatus.EDIT
    protected val DRAFT_PRODUCT_ID = "DRAFT_PRODUCT_ID"
    private var tkpdProgressDialog: TkpdProgressDialog? = null
    private var draftProductId: Long = 0

    override fun initInjector() {
        DaggerProductDraftComponent
                .builder()
                .productComponent(getComponent(ProductComponent::class.java))
                .productDraftModule(ProductDraftModule())
                .build()
                .inject(this)
    }

    override fun onSuccessLoadProduct(model: ProductViewModel?) {
        hideLoading()
        if (currentProductAddViewModel == null) {
            currentProductAddViewModel = ProductAddViewModel()
        }
        populateView(model?.convertToProductAddViewModel())
    }

    override fun onErrorLoadProduct(throwable: Throwable?) {
        hideLoading()
        CommonUtils.UniversalToast(activity, getString(R.string.product_draft_error_cannot_load_draft))
        activity?.finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        draftProductId = arguments?.getLong(DRAFT_PRODUCT_ID)!!
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        if (savedInstanceState == null) {
            fetchInputData()
        }
        return view
    }

    fun fetchInputData() {
        showLoading()
        presenter.fetchDraftData(draftProductId)
    }

    protected fun showLoading() {
        if (tkpdProgressDialog == null) {
            tkpdProgressDialog = TkpdProgressDialog(activity,
                    TkpdProgressDialog.NORMAL_PROGRESS, getString(R.string.edit_product))
        }
        tkpdProgressDialog!!.showDialog()
    }

    protected fun hideLoading() {
        if (tkpdProgressDialog != null) {
            tkpdProgressDialog!!.dismiss()
        }
    }

    override fun getProductDraftId(): Long {
        return draftProductId
    }

    companion object {
        val DRAFT_PRODUCT_ID = "draft_product_id"

        fun createInstance(draftProductId: Long): Fragment {
            val fragment = ProductDraftEditFragment()
            val args = Bundle()
            args.putLong(DRAFT_PRODUCT_ID, draftProductId)
            fragment.arguments = args
            return fragment
        }
    }

}