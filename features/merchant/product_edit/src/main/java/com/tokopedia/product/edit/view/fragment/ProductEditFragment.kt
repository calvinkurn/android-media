package com.tokopedia.product.edit.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tkpd.library.ui.utilities.TkpdProgressDialog
import com.tokopedia.core.network.NetworkErrorHelper
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.common.di.component.ProductComponent
import com.tokopedia.product.edit.common.model.edit.ProductViewModel
import com.tokopedia.product.edit.common.util.ProductStatus
import com.tokopedia.product.edit.di.component.DaggerProductEditComponent
import com.tokopedia.product.edit.di.module.ProductEditModule
import com.tokopedia.product.edit.utils.convertToProductAddViewModel
import com.tokopedia.product.edit.view.model.ProductAddViewModel
import com.tokopedia.product.edit.view.presenter.ProductEditPresenterImpl
import com.tokopedia.product.edit.view.presenter.ProductEditView

open class ProductEditFragment : BaseProductAddEditFragment<ProductEditPresenterImpl, ProductEditView>(), ProductEditView {

    private var tkpdProgressDialog: TkpdProgressDialog? = null
    private var productId: String = ""
    override var statusUpload: Int = ProductStatus.EDIT

    override fun initInjector() {
        DaggerProductEditComponent
                .builder()
                .productComponent(getComponent(ProductComponent::class.java))
                .productEditModule(ProductEditModule())
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        if (savedInstanceState == null) {
            fetchInputData()
        }
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        productId = arguments?.getString(EDIT_PRODUCT_ID) ?: ""
        super.onCreate(savedInstanceState)
    }

    override fun onErrorFetchEditProduct(throwable: Throwable?) {
        hideLoading()
        NetworkErrorHelper.createSnackbarWithAction(activity) {
            showLoading()
            fetchInputData()
        }.showRetrySnackbar()
    }

    private fun fetchInputData() {
        presenter.fetchEditProductData(productId)
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

    override fun onSuccessLoadProduct(model: ProductViewModel?) {
        hideLoading()
        if (currentProductAddViewModel == null) {
            currentProductAddViewModel = ProductAddViewModel()
        }
        populateView(model?.convertToProductAddViewModel())
    }

    companion object {
        val EDIT_PRODUCT_ID = "edit_product_id"

        fun createInstance(productId: String): ProductEditFragment {
            val fragment = ProductEditFragment()
            val args = Bundle()
            args.putString(EDIT_PRODUCT_ID, productId)
            fragment.arguments = args
            return fragment
        }
    }
}